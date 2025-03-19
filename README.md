# 응급상황 인식 및 응급실 연계 서비스 포탈

## 프로젝트 기간

**2024.12.18~2024.12.26**


## 👨‍🔧Team

| 이름 | 역할 | 개발 영역 |
|--------|--------|--------|
| 김대길   | 조장, 발표   | • 프론트엔드 <br> • 벡엔드   |
| 고영진   | PPT 제작   | • 프론트엔드 <br> • 벡엔드   |
| 김지섭   | 코드테스트   | • FAST API 서버 구축   |

## 언어모델(BERT) 파인튜닝 프로젝트

https://github.com/kdk7271/mini_proj_6

## 🧐프로젝트 개요

* BERT 언어 모델을 응급상황을 5단계의 중증도로 분류하도록 파인튜닝하였다.
* 분류된 중증도에 따라 사용자 근처의 병원을 안내하는 서비스 포탈을 구현하였다.

## 💻 적용 기술

**BE**

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/python-3776AB?style=for-the-badge&logo=python&logoColor=white"> <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/FAST API-009688?style=for-the-badge&logo=fastapi&logoColor=white"> <img src="https://img.shields.io/badge/h2-4479A1?style=for-the-badge&logo=&logoColor=white">


**FE**

<img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white"> <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white"> <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"> <img src="https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white"> <img src="https://img.shields.io/badge/NaverMaps-03C75A?style=for-the-badge&logo=naver&logoColor=white"> 

**AI**

<img src="https://img.shields.io/badge/Keras-D00000?style=for-the-badge&logo=keras&logoColor=white"> <img src="https://img.shields.io/badge/Open AI-412991?style=for-the-badge&logo=openai&logoColor=white"> <img src="https://img.shields.io/badge/Pandas-150458?style=for-the-badge&logo=pandas&logoColor=white"> 

**ETC**

 <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"> <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"> <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"> <img src="https://img.shields.io/badge/Microsoft Azure-0854C1?style=for-the-badge&logo=&logoColor=white">
 

## ⚙️ 주요 기능

![Image](https://github.com/user-attachments/assets/ef8b9e74-d662-4278-9655-9f7aa059ac46)

## 🛠️ 시스템 구조

![Image](https://github.com/user-attachments/assets/e75a6e0f-2477-47a9-bf66-ecfa1caa7576)

## 인공지능 서버 구조

![Image](https://github.com/user-attachments/assets/e5041d0d-21e8-4904-9f5b-0207a9a5e89a)

## 상세 기능 설명

**가. 증상 및 위치 입력**

![Image](https://github.com/user-attachments/assets/669dc6c3-4f92-48c0-8ae4-76e2e33e9988)

1️⃣ 사용자 증상 입력 : 텍스트로 사용자 증상를 입력합니다.

2️⃣ 사용자 증상 입력(음성) : 음성을 통해 사용자 증상을 입력합니다.

3️⃣ 사용자 위치 입력: 위도, 경도를 입력해 사용자 

4️⃣ 안내받을 병원 개수 지정 : 안내받을 병원의 개수를 지정한다.

5️⃣ 사용자 위치 입력(Naver Maps api): Naver Maps api 를 사용하여 사용자 위치를 직접 지정한다.

6️⃣ 사용자 위치 입력(주소 입력) : 주소를 입력해 사용자 위치를 지정한다.

**나. 추천 병원 안내**

![Image](https://github.com/user-attachments/assets/f64c15ba-62e3-43bf-b29e-6b9608757c6a)

1️⃣ 추천 병원 안내(거리순) : 거리순으로 추천된 병원의 정보를 안내합니다. 

2️⃣ 지도 정보 확인 : 현재 위치, 병원위치 등의 정보를 확인할 수 있습니다.

3️⃣ 병원 경로 안내 : 현재 위치 부터 안내된 병원까지의 경로를 표시합니다.

**관리자 페이지**

![Image](https://github.com/user-attachments/assets/32dc30a1-abe4-46f5-95ba-d1b8eb907648)

1️⃣ 기간별 사용 로그 검색 : 서비스 사용 로그를 기간별로 검색합니다.

2️⃣ 사용 로그확인 : 사용자들의 사용 로그를 확인, 삭제가 가능합니다.

## 배포

**가. Docker**


![Image](https://github.com/user-attachments/assets/2cc9f089-206d-4e8e-ae85-c9d39e6e0c29)

* Spring 서버와 Fast Api 서버를 도커에 이미지 배포하였습니다.

**나. Azure**

**나-1 Spring 서버**

![Image](https://github.com/user-attachments/assets/48bf0a27-dae8-4aa8-aad7-162ab6e7e49b)

* Spring 서버를 Azure App Service 를 통해 배포하였습니다.

**나-2 Fast Api 서버**

![Image](https://github.com/user-attachments/assets/67b0cd86-c78b-47f3-bc06-64ccc5d0031f)

* Fast Api 서버를 Azure App Service 를 통해 배포하였습니다.




