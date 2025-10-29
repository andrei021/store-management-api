package io.github.andrei021.store.common.dto;

import java.util.List;

public record UserDto(Long id, String username, String password, List<String> roles) {

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                '}';
    }
}

