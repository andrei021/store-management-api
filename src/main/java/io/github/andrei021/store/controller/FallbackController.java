package io.github.andrei021.store.controller;

import io.github.andrei021.store.common.dto.response.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.Instant;

@RestController
@RequestMapping("/api")
public class FallbackController {

    @RequestMapping("/**")
    public ResponseEntity<ErrorResponseDto> handleUnknownEndpoint(ServletWebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponseDto.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message("The endpoint is not provided by this API. Please check the API documentation")
                .path(request.getRequest().getRequestURI())
                .build()
        );
    }
}

