package com.example.crypto_recommendation_service.services;

import com.example.crypto_recommendation_service.entities.Crypto;
import com.example.crypto_recommendation_service.exceptions.BusinessException;
import com.example.crypto_recommendation_service.repositories.CryptoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DataLoadService {

    private final CryptoRepository cryptoRepository;

    @Transactional
    public void fetchSymbols() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:/prices/*.csv");

            for (Resource resource : resources) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                        resource.getInputStream(), StandardCharsets.UTF_8))) {

                    List<Crypto> cryptos = reader.lines()
                            .skip(1) // Skip header
                            .map(line -> {
                                String[] values = line.split(",");
                                return Crypto.builder()
                                        .timestamp(Long.parseLong(values[0]))
                                        .symbol(values[1])
                                        .price(Double.parseDouble(values[2]))
                                        .build();
                            })
                            .toList();

                    cryptos.forEach(cryptoRepository::save);
                }
            }
        } catch (Exception e) {
            throw new BusinessException("Data cannot be read");
        }
    }

}
