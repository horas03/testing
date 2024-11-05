package com.example.crypto_recommendation_service.repositories;

import com.example.crypto_recommendation_service.entities.Crypto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoRepository extends CrudRepository<Crypto, Long> {

}

