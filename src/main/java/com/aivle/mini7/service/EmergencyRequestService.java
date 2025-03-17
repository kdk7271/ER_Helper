package com.aivle.mini7.service;

import com.aivle.mini7.domain.EmergencyRequest;
import com.aivle.mini7.repository.EmergencyRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmergencyRequestService {
    public List<EmergencyRequest> getRequestsByTimeRange(String start, String end) {
        return repository.findByDatetimeBetween(start, end);
    }

    private final EmergencyRequestRepository repository;
    public EmergencyRequest saveRequest(EmergencyRequest request) {
        return repository.save(request);
    }
    // 모든 데이터 가져오기
    public List<EmergencyRequest> getAllRequests() {
        return repository.findAll();
    }
    public void deleteAllRequests() {
        repository.deleteAll();
    }
    public void deleteRequestById(Integer id) {
        repository.deleteById(id);
    }

}