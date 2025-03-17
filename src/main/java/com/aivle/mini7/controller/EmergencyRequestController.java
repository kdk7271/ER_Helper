package com.aivle.mini7.controller;

import com.aivle.mini7.domain.EmergencyRequest;
import com.aivle.mini7.service.EmergencyRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EmergencyRequestController {

    private final EmergencyRequestService service;

    @GetMapping("/admintest")
    public ModelAndView getAllRequests() {
        // 서비스에서 데이터 가져오기
        List<EmergencyRequest> requests = service.getAllRequests();
        // ModelAndView 객체 생성
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admintest"); // 반환할 HTML 템플릿 이름
        modelAndView.addObject("requests", requests); // 모델에 데이터 추가
        return modelAndView;
    }
    @GetMapping("/admintest/search")
    public ModelAndView searchByTimeRange(@RequestParam("start") String start,
                                          @RequestParam("end") String end) {
        LocalDate endDate = LocalDate.parse(end).plusDays(1);
        String adjustedEnd = endDate.toString();
        List<EmergencyRequest> filteredRequests = service.getRequestsByTimeRange(start, adjustedEnd);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admintest");
        modelAndView.addObject("requests", filteredRequests);
        return modelAndView;
    }
    @PostMapping("/admintest/clear")
    public ResponseEntity<Void> clearAllRequests() {
        log.info("Clearing all emergency requests...");
        service.deleteAllRequests();
        return ResponseEntity.status(302) // HTTP 302 Found
                .header("Location", "/admintest") // 리다이렉트 URL
                .build();
    }
    @PostMapping("/admintest/delete/{id}")
    public ResponseEntity<Void> deleteRequestById(@PathVariable Integer id) {
        log.info("Deleting request with ID: {}", id);
        service.deleteRequestById(id);
        return ResponseEntity.status(302) // HTTP 302 Found
                .header("Location", "/admintest") // 리다이렉트 URL
                .build();
    }
}
