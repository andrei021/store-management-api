package io.github.andrei021.store.common.dto.request;

import jakarta.validation.constraints.Positive;

public record BuyProductRequestDto(
        @Positive(message = "Product id must be positive")
        long id
) {}
