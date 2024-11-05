package com.example.crypto_recommendation_service.config;

import com.example.crypto_recommendation_service.services.DataLoadService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class StartupComponent {

    private final DataLoadService dataLoadService;

    @PostConstruct
    public void loadCsvData() {
        dataLoadService.fetchSymbols();
    }
}
