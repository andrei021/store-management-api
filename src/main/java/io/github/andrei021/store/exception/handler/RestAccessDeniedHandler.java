package io.github.andrei021.store.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.andrei021.store.common.dto.response.ApiResponse;
import io.github.andrei021.store.common.dto.response.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

import static io.github.andrei021.store.exception.ExceptionUtil.FAILED_REQUEST;

@Component
@Slf4j
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private static final String FORBIDDEN = "FORBIDDEN";

    private final ObjectMapper mapper;

    public RestAccessDeniedHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        log.warn("There are not enough rights to access this resource: [{}]", accessDeniedException.getMessage());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .status(HttpServletResponse.SC_FORBIDDEN)
                .error(FORBIDDEN)
                .message(accessDeniedException.getMessage())
                .path(request.getRequestURI())
                .build();

        mapper.writeValue(response.getOutputStream(), new ApiResponse<>(
                errorResponse,
                FAILED_REQUEST,
                Instant.now()
        ));
    }
}
