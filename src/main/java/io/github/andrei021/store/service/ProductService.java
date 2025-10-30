package io.github.andrei021.store.service;

import io.github.andrei021.store.common.dto.request.CreateProductRequestDto;
import io.github.andrei021.store.common.dto.request.BuyProductRequestDto;
import io.github.andrei021.store.common.dto.response.PaginatedResponseDto;
import io.github.andrei021.store.common.dto.response.ProductResponseDto;

import java.math.BigDecimal;

public interface ProductService {

    ProductResponseDto findById(long id);

    ProductResponseDto findByName(String name);

    PaginatedResponseDto<ProductResponseDto> getPaginatedProducts(int offset, int limit);

    ProductResponseDto createProduct(CreateProductRequestDto request);

    ProductResponseDto buyProduct(BuyProductRequestDto request);

    ProductResponseDto changePrice(long id, BigDecimal newPrice);

    void deleteProduct(long id);
}
