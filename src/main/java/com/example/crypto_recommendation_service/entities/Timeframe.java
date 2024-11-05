package com.example.crypto_recommendation_service.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Timeframe {
    ONE_DAY("1-day"),
    ONE_WEEK("1-week"),
    ONE_MONTH("1-month"),
    SIX_MONTHS("6-months"),
    ONE_YEAR("1-year"),
    FIVE_YEARS("5-years");

    private final String label;
}

