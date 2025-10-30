package io.github.andrei021.store.exception.handler;

import io.github.andrei021.store.common.dto.response.StoreApiResponse;
import io.github.andrei021.store.common.dto.response.ErrorResponseDto;
import io.github.andrei021.store.exception.instance.InsufficientStockException;
import io.github.andrei021.store.exception.instance.InvalidOffsetException;
import io.github.andrei021.store.exception.instance.ProductAlreadyExistsException;
import io.github.andrei021.store.exception.instance.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import static io.github.andrei021.store.exception.ExceptionUtil.buildErrorResponse;

@Order(1)
@RestControllerAdvice
@Slf4j
public class ProductExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<StoreApiResponse<ErrorResponseDto>> handleProductNotFound(
            ProductNotFoundException exception,
            ServletWebRequest request
    ) {
        String exceptionMessage = exception.getMessage();
        log.warn("Product not found: {}", exceptionMessage);
        return buildErrorResponse(HttpStatus.NOT_FOUND, exceptionMessage, request);
    }

    @ExceptionHandler(InvalidOffsetException.class)
    public ResponseEntity<StoreApiResponse<ErrorResponseDto>>handleInvalidOffset(
            InvalidOffsetException exception,
            ServletWebRequest request
    ) {
        String exceptionMessage = exception.getMessage();
        log.warn("Invalid offset: {}", exceptionMessage);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exceptionMessage, request);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<StoreApiResponse<ErrorResponseDto>> handleProductAlreadyExists(
            ProductAlreadyExistsException exception,
            ServletWebRequest request
    ) {
        String exceptionMessage = exception.getMessage();
        log.warn("Product already exists: {}", exceptionMessage, exception);
        return buildErrorResponse(HttpStatus.CONFLICT, exceptionMessage, request);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<StoreApiResponse<ErrorResponseDto>> handleInsufficientStock(
            InsufficientStockException exception,
            ServletWebRequest request
    ) {
        String exceptionMessage = exception.getMessage();
        log.warn("Insufficient stock: {}", exceptionMessage);
        return buildErrorResponse(HttpStatus.CONFLICT, exceptionMessage, request);
    }
}
