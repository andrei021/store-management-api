package io.github.andrei021.store.controller;

import io.github.andrei021.store.persistence.model.ProductDto;
import io.github.andrei021.store.service.ProductService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * GET /api/products/{id}
     * Find a product by ID. Returns 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(
            @Positive(message = "Product id must be positive")
            @PathVariable("id") long id) {

        ProductDto product = productService.findById(id);
        return ResponseEntity.ok(product);
    }
}