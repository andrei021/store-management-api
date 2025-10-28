package io.github.andrei021.store.persistence.repository;

import io.github.andrei021.store.common.dto.response.ProductResponseDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Optional<ProductResponseDto> findById(long id);

    Optional<ProductResponseDto> findByName(String name);

    List<ProductResponseDto> getPaginatedProducts(int offset, int limit);

    ProductResponseDto createProduct(String name, BigDecimal price, int stock);

    boolean buyProduct(long id);

    boolean changePrice(long id, BigDecimal newPrice);

    boolean deleteProduct(long id);
}
