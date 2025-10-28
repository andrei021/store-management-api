package io.github.andrei021.store.service;

import io.github.andrei021.store.common.dto.request.AddProductRequestDto;
import io.github.andrei021.store.common.dto.request.BuyProductRequestDto;
import io.github.andrei021.store.common.dto.response.PaginatedResponseDto;
import io.github.andrei021.store.common.dto.response.ProductResponseDto;

public interface ProductService {

    ProductResponseDto findById(long id);

    ProductResponseDto findByName(String name);

    PaginatedResponseDto<ProductResponseDto> getPaginatedProducts(int offset, int limit, String baseUrl);

    ProductResponseDto createProduct(AddProductRequestDto request);

    ProductResponseDto buyProduct(BuyProductRequestDto request);
}
