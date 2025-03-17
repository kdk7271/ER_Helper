package com.aivle.mini7.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "emergency")  // 테이블 이름 매핑
public class EmergencyRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // JPA에서 기본 키 필요

    private String datetime;

    @Column(name = "input_text")
    private String inputText;

    @Column(name = "input_latitude")
    private Double inputLatitude;

    @Column(name = "input_longitude")
    private Double inputLongitude;

    @Column(name = "em_class")
    private Integer emclass;

    private String hospital1;
    private String addr1;
    private String tel1;

    private String hospital2;
    private String addr2;
    private String tel2;

    private String hospital3;
    private String addr3;
    private String tel3;
}
