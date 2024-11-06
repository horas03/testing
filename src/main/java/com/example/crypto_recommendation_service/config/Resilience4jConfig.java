package com.example.crypto_recommendation_service.config;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Resilience4jConfig {

    @Bean
    public RateLimiterRegistry rateLimiterRegistry() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(1) // Number of requests allowed in each period
                .limitRefreshPeriod(Duration.ofSeconds(1)) // Refresh period for rate limit
                .timeoutDuration(Duration.ZERO) // No wait time when rate limit is exceeded
                .build();

        return RateLimiterRegistry.of(config);
    }

}
