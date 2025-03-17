package com.aivle.mini7.controller;

import com.aivle.mini7.client.api.FastApiClient;
import com.aivle.mini7.client.dto.FastApiResponse;
import com.aivle.mini7.client.dto.HospitalResponse;
import com.aivle.mini7.domain.EmergencyRequest;
import com.aivle.mini7.service.EmergencyRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    private final FastApiClient fastApiClient;
    private final EmergencyRequestService emergencyRequestService;

    @Value("${naver.map.id}")
    private String mapId;
    
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("mapId", mapId);
        return "index";
    }

    @PostMapping("/recommend_hospital")
    public ModelAndView recommend_hospital(@RequestParam("request") String request,
                                           @RequestParam("latitude") double latitude,
                                           @RequestParam("longitude") double longitude,
                                           @RequestParam("count") int count) {
        System.out.println("request" + request);
        System.out.println("latitude" + latitude);
        System.out.println("longitude" + longitude);
        // FastApiClient 를 호출한다.
        FastApiResponse response = fastApiClient.getHospital(request, latitude, longitude,count);
        LocalDateTime now = LocalDateTime.now();
        String formattedNow = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ModelAndView mv = new ModelAndView();
        if (response.getPredicted_class() <= 2 && response.getResult() != null && !response.getResult().isEmpty()) {
            EmergencyRequest emergencyRequest = new EmergencyRequest();
            emergencyRequest.setDatetime(formattedNow);
            emergencyRequest.setInputText(request);
            emergencyRequest.setInputLatitude(latitude);
            emergencyRequest.setInputLongitude(longitude);
            emergencyRequest.setEmclass(response.getPredicted_class());
            emergencyRequest.setHospital1(response.getResult().get(0).getHospitalName());
            emergencyRequest.setAddr1(response.getResult().get(0).getAddress());
            emergencyRequest.setTel1(response.getResult().get(0).getPhoneNumber1());
            emergencyRequestService.saveRequest(emergencyRequest);

            mv.setViewName("recommend_hospital");
            mv.addObject("hospitalList", response.getResult());
            mv.addObject("mapId", mapId);
        } else {
            mv.setViewName("personal");
            mv.addObject("message", "개인 건강관리");
        }

        return mv;
    }


}



