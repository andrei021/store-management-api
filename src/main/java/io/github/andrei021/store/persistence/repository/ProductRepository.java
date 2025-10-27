package io.github.andrei021.store.persistence.repository;

import io.github.andrei021.store.common.dto.response.ProductResponseDto;

import java.util.Optional;

public interface ProductRepository {

    Optional<ProductResponseDto> findById(long id);

    Optional<ProductResponseDto> findByName(String name);
}
