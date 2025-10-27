package io.github.andrei021.store.service;

import io.github.andrei021.store.common.dto.response.ProductResponseDto;

public interface ProductService {

    ProductResponseDto findById(long id);

    ProductResponseDto findByName(String name);
}
