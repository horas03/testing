package com.example.crypto_recommendation_service.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MetricsDto {

    private String symbol;

    @JsonProperty("metrics")
    private MetricsDataDto metricsDataDto;

}
