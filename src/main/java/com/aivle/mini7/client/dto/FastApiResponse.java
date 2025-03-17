package com.aivle.mini7.client.dto;

import lombok.Data;

import java.util.List;

@Data
public class FastApiResponse {
    private List<HospitalResponse> result;
    private Integer predicted_class;
}
