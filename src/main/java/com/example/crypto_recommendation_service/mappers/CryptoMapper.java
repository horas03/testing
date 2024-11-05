package com.example.crypto_recommendation_service.mappers;

import com.example.crypto_recommendation_service.domain.model.CryptoDto;
import com.example.crypto_recommendation_service.entities.Crypto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CryptoMapper {

    @Mapping(target = "timestamp", expression = "java(crypto.getFormattedTimestamp())")
    CryptoDto toDto(Crypto crypto);
}
