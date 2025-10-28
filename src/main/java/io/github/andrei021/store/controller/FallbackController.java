package io.github.andrei021.store.controller;

import io.github.andrei021.store.common.dto.response.ApiResponse;
import io.github.andrei021.store.common.dto.response.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import static io.github.andrei021.store.controller.ControllerUtil.buildErrorResponse;

@RestController
@RequestMapping("/api")
public class FallbackController {

    @RequestMapping("/**")
    public ResponseEntity<ApiResponse<ErrorResponseDto>> handleUnknownEndpoint(ServletWebRequest request) {
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                "The endpoint is not provided by this API. Please check the API documentation",
                request
        );
    }
}

