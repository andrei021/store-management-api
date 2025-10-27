package io.github.andrei021.store.controller;

import io.github.andrei021.store.common.dto.response.ProductResponseDto;
import io.github.andrei021.store.service.ProductService;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<ProductResponseDto> findById(
            @Positive(message = "Product id must be positive")
            @PathVariable("id") long id) {

        ProductResponseDto product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * GET /api/products?name={name}
     * Find a product by name. Returns 404 if not found
     */
    @GetMapping
    public ResponseEntity<ProductResponseDto> findByName(
            @RequestParam("name")
            @Size(max = 255, message = "Product name must be at most 255 characters")
            @Pattern(
                    regexp = "^(?=.*[A-Za-z])[A-Za-z0-9_-]+$",
                    message = "Product name must contain at least one letter and can include only letters, digits, dash (-) and underscore (_)"
            )
            String name) {

        ProductResponseDto product = productService.findByName(name);
        return ResponseEntity.ok(product);
    }
}