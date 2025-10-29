package io.github.andrei021.store.persistence.repository;

import io.github.andrei021.store.common.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<UserDto> findByUsername(String username);

    List<String> findRolesByUserId(Long userId);
}
