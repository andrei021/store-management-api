package io.github.andrei021.store.controller;

import io.github.andrei021.store.common.dto.response.ApiResponse;
import io.github.andrei021.store.common.dto.response.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.Instant;

public final class ControllerUtil {

    public static final String FAILED_REQUEST = "FAILED_REQUEST";

    private ControllerUtil() {
    }

    public static ResponseEntity<ApiResponse<ErrorResponseDto>> buildErrorResponse(
            HttpStatus status,
            String message,
            ServletWebRequest request
    ) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequest().getRequestURI())
                .build();

        return ResponseEntity.status(status)
                .body(new ApiResponse<>(errorResponseDto, FAILED_REQUEST, Instant.now()));
    }
}
