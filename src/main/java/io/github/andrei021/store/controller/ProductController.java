package io.github.andrei021.store.controller;

import io.github.andrei021.store.common.dto.request.BuyProductRequestDto;
import io.github.andrei021.store.common.dto.request.ChangePriceRequestDto;
import io.github.andrei021.store.common.dto.request.CreateProductRequestDto;
import io.github.andrei021.store.common.dto.response.ApiResponse;
import io.github.andrei021.store.common.dto.response.PaginatedResponseDto;
import io.github.andrei021.store.common.dto.response.ProductResponseDto;
import io.github.andrei021.store.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.Instant;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    private static final String WRONG_PARAM_NAME_MSG = "Product name must contain at least one " +
            "letter and can include only letters, digits, dash (-) and underscore (_)";
    private static final String SUCCESS = "SUCCESS";

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Get paginated list of products
     *
     * Example:
     * GET /api/products?offset=0&limit=10
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponseDto<ProductResponseDto>>> getPaginatedProducts(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Offset must be greater than or equal to 0")
            int offset,

            @RequestParam(defaultValue = "10")
            @Positive(message = "Limit must be a positive number")
            int limit,

            ServletWebRequest request
    ) {
        String baseUrl = request.getRequest().getRequestURL().toString();
        PaginatedResponseDto<ProductResponseDto> response =
                productService.getPaginatedProducts(offset, limit, baseUrl);

        return ResponseEntity.ok(new ApiResponse<>(response, SUCCESS, Instant.now()));
    }

    /**
     * GET /api/products/{id}
     * Find a product by ID. Returns 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> findById(
            @Positive(message = "Product id must be positive")
            @PathVariable("id") long id) {

        ProductResponseDto response = productService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(response, SUCCESS, Instant.now()));
    }

    /**
     * GET /api/products?name={name}
     * Find a product by name. Returns 404 if not found
     */
    @GetMapping("/by-name")
    public ResponseEntity<ApiResponse<ProductResponseDto>> findByName(
            @RequestParam("name")
            @Size(max = 255, message = "Product name must be at most 255 characters")
            @Pattern(
                    regexp = "^(?=.*[A-Za-z])[A-Za-z0-9_-]+$",
                    message = WRONG_PARAM_NAME_MSG
            )
            String name) {

        ProductResponseDto response = productService.findByName(name);
        return ResponseEntity.ok(new ApiResponse<>(response, SUCCESS, Instant.now()));
    }

    /**
     * POST /api/products
     * Add a new product
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(
            @Valid @RequestBody CreateProductRequestDto request
    ) {
        ProductResponseDto createdProduct = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(createdProduct, SUCCESS, Instant.now()));
    }

    /**
     * POST /api/products/buy
     * Buy 1 unit of a product by ID. Throws 404 if not found or 409 if out of stock
     */
    @PostMapping("/buy")
    public ResponseEntity<ApiResponse<ProductResponseDto>> buyProduct(
            @Valid @RequestBody BuyProductRequestDto request
    ) {
        ProductResponseDto response = productService.buyProduct(request);
        return ResponseEntity.ok(new ApiResponse<>(response, SUCCESS, Instant.now()));
    }

    /**
     * PUT /api/products/change-price
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
     * DELETE /api/products/{id}
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