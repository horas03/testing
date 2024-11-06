package com.example.crypto_recommendation_service.aop;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimiterAspect {

    private final RateLimiterRegistry rateLimiterRegistry;

    @Around("@annotation(rateLimited)")
    public Object rateLimit(ProceedingJoinPoint joinPoint, RateLimited rateLimited) throws Throwable {
        String rateLimiterName = rateLimited.name();
        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter(rateLimiterName);

        return RateLimiter.decorateCheckedSupplier(rateLimiter, joinPoint::proceed).get();
    }
}
