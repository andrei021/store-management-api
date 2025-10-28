package io.github.andrei021.store.controller;

import io.github.andrei021.store.common.dto.request.ChangePriceRequestDto;
import io.github.andrei021.store.common.dto.request.CreateProductRequestDto;
import io.github.andrei021.store.common.dto.request.BuyProductRequestDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    private static final String WRONG_PARAM_NAME_MSG = "Product name must contain at least one " +
            "letter and can include only letters, digits, dash (-) and underscore (_)";

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
    public ResponseEntity<PaginatedResponseDto<ProductResponseDto>> getPaginatedProducts(
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

        return ResponseEntity.ok(response);
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
    @GetMapping("/by-name")
    public ResponseEntity<ProductResponseDto> findByName(
            @RequestParam("name")
            @Size(max = 255, message = "Product name must be at most 255 characters")
            @Pattern(
                    regexp = "^(?=.*[A-Za-z])[A-Za-z0-9_-]+$",
                    message = WRONG_PARAM_NAME_MSG
            )
            String name) {

        ProductResponseDto product = productService.findByName(name);
        return ResponseEntity.ok(product);
    }

    /**
     * POST /api/products
     * Add a new product
     */
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
            @Valid @RequestBody CreateProductRequestDto request
    ) {
        ProductResponseDto createdProduct = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    /**
     * POST /api/products/buy
     * Buy 1 unit of a product by ID. Throws 404 if not found or 409 if out of stock
     */
    @PostMapping("/buy")
    public ResponseEntity<ProductResponseDto> buyProduct(
            @Valid @RequestBody BuyProductRequestDto request
    ) {
        ProductResponseDto boughtProduct = productService.buyProduct(request);
        return ResponseEntity.ok(boughtProduct);
    }

    /**
     * PUT /api/products/change-price
     * Change the price of a product by ID
     */
    @PutMapping("/change-price")
    public ResponseEntity<ProductResponseDto> changePrice(
            @Valid @RequestBody ChangePriceRequestDto request
    ) {
        ProductResponseDto updatedProduct = productService.changePrice(request.id(), request.price());
        return ResponseEntity.ok(updatedProduct);
    }
}