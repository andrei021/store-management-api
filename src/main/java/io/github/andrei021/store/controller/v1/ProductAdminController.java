package io.github.andrei021.store.controller.v1;


import io.github.andrei021.store.common.dto.request.ChangePriceRequestDto;
import io.github.andrei021.store.common.dto.request.CreateProductRequestDto;
import io.github.andrei021.store.common.dto.response.ApiResponse;
import io.github.andrei021.store.common.dto.response.ProductResponseDto;
import io.github.andrei021.store.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

import static io.github.andrei021.store.controller.ControllerUtil.SUCCESS;
import static io.github.andrei021.store.controller.ApiVersion.API_V1;

@RestController
@RequestMapping(API_V1 + "/admin")
@Validated
@PreAuthorize("hasAnyRole('ADMIN')")
public class ProductAdminController {

    private final ProductService productService;

    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * POST /api/v1/admin/createProduct
     * Add a new product
     */
    @PostMapping("/createProduct")
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(
            @Valid @RequestBody CreateProductRequestDto request
    ) {
        ProductResponseDto createdProduct = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(createdProduct, SUCCESS, Instant.now()));
    }

    /**
     * PUT /api/v1/admin/change-price
     * Change the price of a product by ID
     */
    @PutMapping("/change-price")
    public ResponseEntity<ApiResponse<ProductResponseDto>> changePrice(
            @Valid @RequestBody ChangePriceRequestDto request
    ) {
        ProductResponseDto response = productService.changePrice(request.id(), request.price());
        return ResponseEntity.ok(new ApiResponse<>(response, SUCCESS, Instant.now()));
    }

    /**
     * DELETE /api/v1/admin/{id}
     * Delete a product by ID. Returns 404 if product not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Positive(message = "Product id must be positive")
            @PathVariable("id") long id) {

        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
