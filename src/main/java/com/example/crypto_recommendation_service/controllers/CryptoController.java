package com.example.crypto_recommendation_service.controllers;

import com.example.crypto_recommendation_service.domain.model.CryptoDto;
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

    @GetMapping("/sorted-by-normalized-range")
    public List<CryptoDto> getCryptosSortedByNormalizedRange(@RequestParam Timeframe timeframe) {
        return cryptoService.getAllCryptosSortedByNormalizedRange(timeframe);
    }

    @GetMapping("/{symbol}/metrics")
    public MetricsDto getCryptoMetrics(@PathVariable String symbol, @RequestParam Timeframe timeframe) {
        return cryptoService.getCryptoMetrics(symbol, timeframe);
    }

    @GetMapping("/highest-range")
    public CryptoDto getCryptoWithHighestNormalizedRange(@RequestParam String date) {
        return cryptoService.getCryptoWithHighestNormalizedRange(LocalDate.parse(date));
    }

    @PatchMapping("/reload-data")
    public void fetchSymbols() {
        dataLoadService.fetchSymbols();
    }

}

