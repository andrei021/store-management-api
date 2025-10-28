package io.github.andrei021.store.common.dto.response;

import java.time.Instant;

public record ApiResponse<T>(
        T data,
        String statusMessage,
        Instant timestamp
) {}
