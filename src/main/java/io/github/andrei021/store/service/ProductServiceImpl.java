package io.github.andrei021.store.service;

import io.github.andrei021.store.common.dto.response.ProductResponseDto;
import io.github.andrei021.store.common.exception.ProductNotFoundException;
import io.github.andrei021.store.persistence.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public ProductResponseDto findById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        String.format("Product not found with id=[%d]", id)
                ));
    }

    @Transactional(readOnly = true)
    public ProductResponseDto findByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new ProductNotFoundException(
                        String.format("Product not found with name=[%s]", name)
                ));
    }
}
