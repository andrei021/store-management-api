package io.github.andrei021.store.controller.error;

import io.github.andrei021.store.common.dto.response.ErrorResponseDto;
import io.github.andrei021.store.common.exception.InvalidOffsetException;
import io.github.andrei021.store.common.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.Instant;

import static io.github.andrei021.store.controller.ControllerUtil.buildErrorResponse;

@Order(1)
@RestControllerAdvice
@Slf4j
public class ProductExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleProductNotFound(
            ProductNotFoundException exception,
            ServletWebRequest request
    ) {
        String exceptionMessage = exception.getMessage();
        log.warn("Product not found: {}", exceptionMessage);
        return buildErrorResponse(HttpStatus.NOT_FOUND, exceptionMessage, request);
    }

    @ExceptionHandler(InvalidOffsetException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidOffset(
            InvalidOffsetException exception,
            ServletWebRequest request
    ) {
        String exceptionMessage = exception.getMessage();
        log.warn("Invalid offset: {}", exceptionMessage);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exceptionMessage, request);
    }
}
