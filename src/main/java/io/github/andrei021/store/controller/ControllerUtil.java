package io.github.andrei021.store.controller;

import io.github.andrei021.store.common.dto.response.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.Instant;

public final class ControllerUtil {

    private ControllerUtil() {
    }

    public static ResponseEntity<ErrorResponseDto> buildErrorResponse(
            HttpStatus status,
            String message,
            ServletWebRequest request
    ) {
        return ResponseEntity.status(status).body(ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequest().getRequestURI())
                .build()
        );
    }
}
