package com.example.crypto_recommendation_service.controllers;

import com.example.crypto_recommendation_service.aop.RateLimited;
import com.example.crypto_recommendation_service.domain.model.CryptoRange;
import com.example.crypto_recommendation_service.domain.model.MetricsDto;
import com.example.crypto_recommendation_service.entities.Timeframe;
import com.example.crypto_recommendation_service.services.CryptoService;
import com.example.crypto_recommendation_service.services.DataLoadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/crypto")
@RequiredArgsConstructor
public class CryptoController {

    private final CryptoService cryptoService;
    private final DataLoadService dataLoadService;

    @RateLimited(name = "customRateLimiter")
    @GetMapping("/sorted-by-normalized-range")
    public List<CryptoRange> getCryptosSortedByNormalizedRange(@RequestParam Timeframe timeframe) {
        return cryptoService.getAllCryptosSortedByNormalizedRange(timeframe);
    }

    @RateLimited(name = "customRateLimiter")
    @GetMapping("/{symbol}/metrics")
    public MetricsDto getCryptoMetrics(@PathVariable String symbol, @RequestParam Timeframe timeframe) {
        return cryptoService.getCryptoMetrics(symbol, timeframe);
    }

    @RateLimited(name = "customRateLimiter")
    @GetMapping("/highest-range")
    public CryptoRange getCryptoWithHighestNormalizedRange(@RequestParam String date) {
        return cryptoService.getCryptoWithHighestNormalizedRange(LocalDate.parse(date));
    }

    @RateLimited(name = "customRateLimiter")
    @PatchMapping("/reload-data")
    public void fetchSymbols() {
        dataLoadService.fetchSymbols();
    }

}

