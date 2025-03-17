package com.aivle.mini7.repository;


import com.aivle.mini7.domain.EmergencyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface EmergencyRequestRepository extends JpaRepository<EmergencyRequest, Integer> {
    @Query("SELECT e FROM EmergencyRequest e WHERE e.datetime BETWEEN :start AND :end")
    List<EmergencyRequest> findByDatetimeBetween(@Param("start") String start, @Param("end") String end);
}