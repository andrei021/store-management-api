package io.github.andrei021.store.common.dto.response;

import java.math.BigDecimal;

public record ProductResponseDto(
        long id,
        String name,
        BigDecimal price,
        int stock
) {}
