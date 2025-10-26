package io.github.andrei021.store.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FallbackController {

    @RequestMapping("/**")
    public ResponseEntity<String> handleUnknownEndpoint(HttpServletRequest request) {
        String endpoint = request.getRequestURI();
        String message = String.format(
                "Endpoint [%s] is not provided by this API. Please check the API documentation",
                endpoint
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
}

