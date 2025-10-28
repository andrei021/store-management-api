package io.github.andrei021.store.service;

import io.github.andrei021.store.common.dto.request.AddProductRequestDto;
import io.github.andrei021.store.common.dto.response.PaginatedResponseDto;
import io.github.andrei021.store.common.dto.response.ProductResponseDto;
import io.github.andrei021.store.common.exception.InvalidOffsetException;
import io.github.andrei021.store.common.exception.ProductAlreadyExistsException;
import io.github.andrei021.store.common.exception.ProductNotFoundException;
import io.github.andrei021.store.persistence.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private static final int DEFAULT_PAGINATION_LIMIT = 10;
    private static final int MAX_PAGINATION_LIMIT = 50;
    private static final int MIN_PAGINATION_OFFSET = 0;

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

    @Transactional(readOnly = true)
    public PaginatedResponseDto<ProductResponseDto> getPaginatedProducts(int offset, int limit, String baseUrl) {
        validateOffset(offset);
        limit = checkAndGetLimit(limit);

        List<ProductResponseDto> products = productRepository.getPaginatedProducts(offset, limit);

        boolean hasNext = products.size() == limit;
        boolean hasPrevious = offset > 0;

        int nextOffset = offset + limit;
        int prevOffset = Math.max(offset - limit, 0);

        return new PaginatedResponseDto<>(
                products,
                offset,
                limit,
                hasNext ? baseUrl + "?offset=" + nextOffset + "&limit=" + limit : null,
                hasPrevious ? baseUrl + "?offset=" + prevOffset + "&limit=" + limit : null,
                hasNext,
                hasPrevious
        );
    }

    @Transactional
    public ProductResponseDto addProduct(AddProductRequestDto request) {
        try {
            return productRepository.addProduct(request);
        } catch (DataIntegrityViolationException exception) {
            String message = String.format("Product with name=[%s] already exists", request.name());
            throw new ProductAlreadyExistsException(message, exception);
        }
    }

    private void validateOffset(int offset) {
        if (offset < MIN_PAGINATION_OFFSET) {
            throw new InvalidOffsetException(offset);
        }
    }

    private int checkAndGetLimit(int limit) {
        boolean hasChanged = false;

        if (limit <= 0) {
            log.warn("Invalid limit [{}]. Resetting to default {}", limit, DEFAULT_PAGINATION_LIMIT);
            limit = DEFAULT_PAGINATION_LIMIT;
            hasChanged = true;
        } else if (limit > MAX_PAGINATION_LIMIT) {
            log.warn("Limit [{}] exceeds max allowed [{}]. Resetting to {}", limit,
                    MAX_PAGINATION_LIMIT, MAX_PAGINATION_LIMIT);
            limit = MAX_PAGINATION_LIMIT;
            hasChanged = true;
        }

        if (hasChanged) {
            log.info("Using adjusted limit [{}] after validation", limit);
        }

        return limit;
    }
}
