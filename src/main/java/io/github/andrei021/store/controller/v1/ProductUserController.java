package io.github.andrei021.store.controller.v1;

import io.github.andrei021.store.common.dto.request.BuyProductRequestDto;
import io.github.andrei021.store.common.dto.response.PaginatedResponseDto;
import io.github.andrei021.store.common.dto.response.ProductResponseDto;
import io.github.andrei021.store.common.dto.response.StoreApiResponse;
import io.github.andrei021.store.controller.v1.docs.GetPaginatedProductsApiDocumentation;
import io.github.andrei021.store.controller.v1.docs.InsufficientStockApiDocumentation;
import io.github.andrei021.store.controller.v1.docs.ProductNotFoundApiDocumentation;
import io.github.andrei021.store.controller.v1.docs.ProductResponseApiDocumentation;
import io.github.andrei021.store.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

import static io.github.andrei021.store.controller.ApiVersion.API_V1;
import static io.github.andrei021.store.controller.ControllerUtil.SUCCESS;
import static io.github.andrei021.store.controller.ControllerUtil.WRONG_PARAM_NAME_MSG;

@RestController
@RequestMapping(API_V1 + "/products")
@Validated
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class ProductUserController {

    private final ProductService productService;

    public ProductUserController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Get paginated list of products
     *
     * Example:
     * GET /api/v1/products?offset=0&limit=10
     */
    @GetMapping
    @GetPaginatedProductsApiDocumentation
    public ResponseEntity<StoreApiResponse<PaginatedResponseDto<ProductResponseDto>>> getPaginatedProducts(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Offset must be greater than or equal to 0")
            int offset,

            @RequestParam(defaultValue = "10")
            @Positive(message = "Limit must be a positive number")
            int limit
    ) {
        PaginatedResponseDto<ProductResponseDto> response = productService.getPaginatedProducts(offset, limit);
        return ResponseEntity.ok(new StoreApiResponse<>(response, SUCCESS, Instant.now()));
    }

    /**
     * GET /api/v1/products/{id}
     * Find a product by ID. Returns 404 if not found
     */
    @GetMapping("/{id}")
    @ProductNotFoundApiDocumentation
    @ProductResponseApiDocumentation
    public ResponseEntity<StoreApiResponse<ProductResponseDto>> findById(
            @Positive(message = "Product id must be positive")
            @PathVariable("id") long id) {

        ProductResponseDto response = productService.findById(id);
        return ResponseEntity.ok(new StoreApiResponse<>(response, SUCCESS, Instant.now()));
    }

    /**
     * GET /api/v1/products?name={name}
     * Find a product by name. Returns 404 if not found
     */
    @GetMapping("/by-name")
    @ProductNotFoundApiDocumentation
    @ProductResponseApiDocumentation
    public ResponseEntity<StoreApiResponse<ProductResponseDto>> findByName(
            @RequestParam("name")
            @Size(max = 255, message = "Product name must be at most 255 characters")
            @Pattern(
                    regexp = "^(?=.*[A-Za-z])[A-Za-z0-9_-]+$",
                    message = WRONG_PARAM_NAME_MSG
            )
            String name) {

        ProductResponseDto response = productService.findByName(name);
        return ResponseEntity.ok(new StoreApiResponse<>(response, SUCCESS, Instant.now()));
    }

    /**
     * POST /api/v1/products/buy
     * Buy 1 unit of a product by ID. Throws 404 if not found or 409 if out of stock
     */
    @PostMapping("/buy")
    @ProductNotFoundApiDocumentation
    @ProductResponseApiDocumentation
    @InsufficientStockApiDocumentation
    public ResponseEntity<StoreApiResponse<ProductResponseDto>> buyProduct(
            @Valid @RequestBody BuyProductRequestDto request
    ) {
        ProductResponseDto response = productService.buyProduct(request);
        return ResponseEntity.ok(new StoreApiResponse<>(response, SUCCESS, Instant.now()));
    }
}