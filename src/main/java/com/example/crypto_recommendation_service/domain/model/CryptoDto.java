package com.example.crypto_recommendation_service.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CryptoDto {

    private LocalDateTime timestamp;
    private String symbol;
    private Double price;

}
