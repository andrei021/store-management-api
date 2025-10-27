package io.github.andrei021.store.common.dto.response;

import java.util.List;

public record PaginatedResponseDto<T>(
    List<T> content,
    int offset,
    int limit,
    String nextPage,
    String prevPage,
    boolean hasNext,
    boolean hasPrevious
) {}
