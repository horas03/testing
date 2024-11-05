package com.example.crypto_recommendation_service.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RedisHash("Crypto")
@Data
@Builder
public class Crypto {

    @Id
    private Long timestamp;

    private String symbol;
    private Double price;

    public LocalDateTime getFormattedTimestamp() {
        if (timestamp != null) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        }
        return null;
    }

}
