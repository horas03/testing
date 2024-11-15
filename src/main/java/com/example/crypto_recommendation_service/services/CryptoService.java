package com.example.crypto_recommendation_service.services;

import com.example.crypto_recommendation_service.domain.model.CryptoRange;
import com.example.crypto_recommendation_service.domain.model.MetricsDto;
import com.example.crypto_recommendation_service.domain.model.MetricsDataDto;
import com.example.crypto_recommendation_service.entities.Crypto;
import com.example.crypto_recommendation_service.entities.Timeframe;
import com.example.crypto_recommendation_service.exceptions.BusinessException;
import com.example.crypto_recommendation_service.repositories.CryptoRepository;
import io.lettuce.core.internal.LettuceLists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CryptoService {

    private final CryptoRepository cryptoRepository;

    public List<CryptoRange> getAllCryptosSortedByNormalizedRange(Timeframe timeframe) {
        List<Crypto> cryptoList = filterByTimeframe(LettuceLists.newList(cryptoRepository.findAll()), timeframe.getLabel());

        return cryptoList.stream()
                .collect(Collectors.groupingBy(Crypto::getSymbol))
                .entrySet().stream()
                .map(entry -> new CryptoRange(entry.getKey(), getNormalizedRange(entry.getValue())))
                .sorted(Comparator.comparingDouble(CryptoRange::getRange).reversed())
                .toList();
    }

    public MetricsDto getCryptoMetrics(String symbol, Timeframe timeframe) {
        List<Crypto> cryptoList = filterByTimeframe(LettuceLists.newList(cryptoRepository.findAll())
                .stream().filter(c -> c.getSymbol().equals(symbol)).toList(), timeframe.getLabel());

        if (cryptoList.isEmpty()) {
            throw new BusinessException("There is no data for symbol " + symbol);
        }
                    Map<String, Double> minMax = getMinAndMax(cryptoList);
                    double min = minMax.get("min");
                    double max = minMax.get("max");
        LocalDateTime oldestDate = cryptoList.stream()
                .map(Crypto::getFormattedTimestamp)
                .min(Comparator.naturalOrder())
                .orElseThrow(() -> new BusinessException("Unable to find the oldest date"));
        LocalDateTime newestDate = cryptoList.stream()
                .map(Crypto::getFormattedTimestamp)
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new BusinessException("Unable to find the newest date"));

        return new MetricsDto(symbol, MetricsDataDto.builder()
                            .max(max)
                            .min(min)
                            .newest(newestDate)
                            .oldest(oldestDate)
                            .build());
    }

    public CryptoRange getCryptoWithHighestNormalizedRange(LocalDate specificDate) {
        List<Crypto> cryptoList = LettuceLists.newList(cryptoRepository.findAll()).stream()
                .filter(c -> c.getFormattedTimestamp().toLocalDate().isEqual(specificDate))
                .toList();

        if (cryptoList.isEmpty()) {
            throw new BusinessException("No crypto data found for the specified date: " + specificDate);
        }

        return cryptoList.stream()
                .collect(Collectors.groupingBy(Crypto::getSymbol))
                .entrySet().stream()
                .map(entry -> new CryptoRange(entry.getKey(), getNormalizedRange(entry.getValue())))
                .max(Comparator.comparingDouble(CryptoRange::getRange))
                .orElseThrow(() -> new BusinessException("No crypto data found with a calculated normalized range for the specified date: " + specificDate));
    }

    private List<Crypto> filterByTimeframe(List<Crypto> cryptos, String timeframe) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = switch (timeframe.toLowerCase()) {
            case "1-day" -> now.minusDays(1);
            case "1-week" -> now.minusWeeks(1);
            case "1-month" -> now.minusMonths(1);
            case "6-months" -> now.minusMonths(6);
            case "1-year" -> now.minusYears(1);
            case "5-years" -> now.minusYears(5);
            default -> throw new IllegalArgumentException("Invalid timeframe");
        };

        return cryptos.stream()
                .filter(c -> c.getFormattedTimestamp().isAfter(start))
                .toList();
    }

    private Map<String, Double> getMinAndMax(List<Crypto> cryptos) {
        return Map.of(
                "min", cryptos.stream().mapToDouble(Crypto::getPrice).min().orElse(Double.NaN),
                "max", cryptos.stream().mapToDouble(Crypto::getPrice).max().orElse(Double.NaN)
        );
    }

    private double getNormalizedRange(List<Crypto> cryptos) {
        Map<String, Double> minMax = getMinAndMax(cryptos);
        return (minMax.get("max") - minMax.get("min")) / minMax.get("min");
    }

}
