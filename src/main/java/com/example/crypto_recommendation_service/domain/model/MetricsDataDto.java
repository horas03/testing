package com.example.crypto_recommendation_service.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MetricsDataDto {

    private Double max;
    private LocalDateTime newest;
    private LocalDateTime oldest;
    private Double min;

}
