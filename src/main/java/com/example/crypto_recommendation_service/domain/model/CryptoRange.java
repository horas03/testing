package com.example.crypto_recommendation_service.domain.model;

import com.example.crypto_recommendation_service.entities.Crypto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CryptoRange {

    private String symbol;
    private double range;

    public Crypto toCrypto() {
        return Crypto.builder()
                .symbol(this.symbol)
                .build();
    }

}
