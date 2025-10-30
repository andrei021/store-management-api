package io.github.andrei021.store.exception;

import io.github.andrei021.store.common.dto.response.StoreApiResponse;
import io.github.andrei021.store.common.dto.response.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.Instant;

public final class ExceptionUtil {

    public static final String FAILED_REQUEST = "FAILED_REQUEST";

    private ExceptionUtil() {
    }

    public static ResponseEntity<StoreApiResponse<ErrorResponseDto>> buildErrorResponse(
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
                .body(new StoreApiResponse<>(errorResponseDto, FAILED_REQUEST, Instant.now()));
    }
}
