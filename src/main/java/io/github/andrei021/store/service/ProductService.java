package io.github.andrei021.store.service;

import io.github.andrei021.store.common.exception.ProductNotFoundException;
import io.github.andrei021.store.persistence.model.ProductDto;
import io.github.andrei021.store.persistence.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public ProductDto findById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        String.format("Product not found with id=[%d]", id)
                ));
    }
}
