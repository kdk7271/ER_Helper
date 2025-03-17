import os
from typing import Union

from fastapi import FastAPI, logger

from openai import OpenAI
import json

import openai
from transformers import AutoTokenizer, AutoModelForSequenceClassification, Trainer, TrainingArguments, EarlyStoppingCallback
import torch
import torch.nn.functional as F

from haversine import haversine
import pandas as pd
import requests


app = FastAPI()

#pip install openai

#pip install transformers
#pip install torch

#pip install haversine
#pip install pandas

#pip install fastapi
#pip install "uvicorn[standard]"

# step0 (input : 음성,위치)
# ——— 위는 사전에 주어진다 ———
# step1 (input : 음성 , output : 음성요약)
# step2 (input : 음성요약[step1의 output), output : 응급code)
# step3 (input :  응급코드[step3의 output], 위치[step0에서 넘어온 것]  , output :응급실목록들) 

@app.get("/")
def read_root():
    return {"Hello": "World"}

@app.get("/deploy_test")
def read_root():
    return {"deploy": "success"}

# 0. load key file------------------
def load_file(filepath):
    with open(filepath, 'r') as file:
        return file.readline().strip()
# 1-1 audio2text--------------------
def audio_to_text(audio_path, filename):
    client = OpenAI()
    audio_file = open(audio_path + filename, "rb")
    transcript = client.audio.transcriptions.create(
                    file=audio_file,
                    model="whisper-1",
                    language="ko",
                    response_format="text",
                    temperature=0.0)
    return transcript

# 1-2 text2summary------------------
def text_summary(input_text):
    # OpenAI 클라이언트 생성
    client = OpenAI()

    system_role = '''당신은 응급상황에 대한 텍스트에서 핵심 내용을 훌륭하게 요약해주는 어시스턴트입니다.
    응답은 다음의 형식을 지켜주세요.
    {"summary": \"텍스트 요약\",
    "keyword" : \"핵심 키워드(3가지)\"}
    '''
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[
            {
                "role": "system",
                "content": system_role
            },
            {
                "role": "user",
                "content": input_text
            }
        ]
    )
    answer = response.choices[0].message.content
    parsed_answer = json.loads(answer)

    summary = parsed_answer["summary"]
    keyword = parsed_answer["keyword"]

    return summary + ', ' + keyword


# 2. model prediction------------------
def predict(text, model, tokenizer):
    # 입력 문장 토크나이징
    inputs = tokenizer(text, return_tensors="pt", truncation=True, padding=True)
    inputs = {key: value for key, value in inputs.items()}  # 각 텐서를 GPU로 이동

    # 모델 예측
    with torch.no_grad():
        outputs = model(**inputs)

    # 로짓을 소프트맥스로 변환하여 확률 계산
    logits = outputs.logits
    probabilities = logits.softmax(dim=1)

    # 가장 높은 확률을 가진 클래스 선택
    pred = torch.argmax(probabilities, dim=-1).item()

    return pred, probabilities


# 3-1. get_distance------------------
def get_dist(start_lat, start_lng, dest_lat, dest_lng, c_id, c_key):
    url = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving"
    headers = {
        "X-NCP-APIGW-API-KEY-ID": c_id,
        "X-NCP-APIGW-API-KEY": c_key,
    }
    params = {
        "start": f"{start_lng},{start_lat}",  # 출발지 (경도, 위도)
        "goal": f"{dest_lng},{dest_lat}",    # 목적지 (경도, 위도)
        "option": "trafast"  # 실시간 빠른 길 옵션
    }

    response = requests.get(url, headers=headers, params=params)
    print(response)
    data = response.json()

    # 'route'와 'trafast' 키가 존재하는지 확인하고 예외 처리
    try:
        dist = data['route']['trafast'][0]['summary']['distance']  # m(미터)
        dist = dist / 1000  # km로 변환
    except KeyError as e:
        print(f"응답 데이터에서 예상되는 키를 찾을 수 없음: {e}")
        return None

    return dist, data['route']['trafast'][0]['path']


# 3-2. recommendation------------------
def recommend_hospital3(path, emergency, start_lat, start_lng, c_id, c_key, count):

    # 위도 경도에 ± 0.5 범위에서 먼저 조회
    temp = emergency.loc[emergency['위도'].between(start_lat-0.05, start_lat+0.05) & emergency['경도'].between(start_lng-0.05, start_lng+0.05)].copy()
    # display(temp)

    # 거리 계산
    temp[['거리', '경로']] = temp.apply(lambda x: pd.Series(get_dist(start_lat, start_lng, x['위도'], x['경도'], c_id, c_key)), axis=1)
    sorted_temp =  temp.sort_values(by='거리').head(count)
    # 결과를 JSON 형태로 변환
    result_json = sorted_temp.apply(lambda row: {
        "hospitalName": row["병원이름"],
        "address": row["주소"],
        "emergencyMedicalInstitutionType": row["응급의료기관 종류"],
        "phoneNumber1": row["전화번호 1"],
        "phoneNumber3": row["전화번호 3"],
        "latitude": row["위도"],
        "longitude": row["경도"],
        "distance": row["거리"],
        "route": row["경로"]
    }, axis=1).tolist()
    return result_json

def getCode(request):
    path=''
    # 모델, 토크나이저 로드
    save_directory = path + "fine_tuned_bert"
    model = AutoModelForSequenceClassification.from_pretrained(save_directory)
    tokenizer = AutoTokenizer.from_pretrained(save_directory)

    summary = text_summary(request)
    predicted_class, _ = predict(summary, model, tokenizer)
    return predicted_class



# API 키
openai.api_key = load_file('api_key.txt')
os.environ['OPENAI_API_KEY'] = openai.api_key

#교통사고가 났어요 머리에 피가 많이나고 있습니다.
#(37.538435245262626, 126.98982802695484) 
# request : 응급상황 latitude: 위도 longitude: 경도

@app.get("/hospital_by_module")
def get_hospital(request: str,latitude: float,longitude: float, count: int):
    path=''
    map_key = load_file(path+'map_key.txt')
    map_key = json.loads(map_key)
    c_id, c_key = map_key['c_id'], map_key['c_key']

    emergency = pd.read_csv(path+'output_emergency_room.csv')

    # 모델, 토크나이저 로드
    save_directory = path + "fine_tuned_bert"
    model = AutoModelForSequenceClassification.from_pretrained(save_directory)
    tokenizer = AutoTokenizer.from_pretrained(save_directory)

    # 처리
    # result = audio_to_text(audio_path, filename)
    summary = text_summary(request)
    predicted_class, _ = predict(summary, model, tokenizer)

    result = None
    if predicted_class <= 2 :
        result = recommend_hospital3(path, emergency, latitude, longitude, c_id, c_key, count)
        print(result)
    else :
        print('개인 건강관리')

    return {"result": result, "predicted_class": predicted_class+1}
