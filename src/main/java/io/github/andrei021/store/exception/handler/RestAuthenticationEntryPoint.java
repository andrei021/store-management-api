package io.github.andrei021.store.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.andrei021.store.common.dto.response.StoreApiResponse;
import io.github.andrei021.store.common.dto.response.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

import static io.github.andrei021.store.exception.ExceptionUtil.FAILED_REQUEST;

@Component
@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String UNAUTHORIZED = "UNAUTHORIZED";

    private final ObjectMapper mapper;

    public RestAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        log.error("Unauthorized error: [{}]", authException.getMessage());
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .error(UNAUTHORIZED)
                .message(authException.getMessage())
                .path(request.getRequestURI())
                .build();

        mapper.writeValue(response.getOutputStream(), new StoreApiResponse<>(
                errorResponse,
                FAILED_REQUEST,
                Instant.now()
        ));
    }
}

