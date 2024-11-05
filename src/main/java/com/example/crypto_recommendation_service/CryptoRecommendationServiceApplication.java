package com.example.crypto_recommendation_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class CryptoRecommendationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoRecommendationServiceApplication.class, args);
	}

}