package io.github.andrei021.store.persistence.repository;

import io.github.andrei021.store.common.dto.UserDto;
import io.github.andrei021.store.persistence.mapper.UserRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static io.github.andrei021.store.persistence.DefaultSqlQueryProvider.GET_USER_BY_USERNAME_QUERY;
import static io.github.andrei021.store.persistence.DefaultSqlQueryProvider.GET_USER_ROLES_BY_USER_ID_QUERY;
import static io.github.andrei021.store.persistence.constant.UserTableConstants.APP_USER_USERNAME_COLUMN;
import static io.github.andrei021.store.persistence.constant.UserTableConstants.USER_ROLES_USER_ID_COLUMN;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final UserRowMapper userRowMapper;

    public UserRepositoryImpl(NamedParameterJdbcTemplate namedJdbcTemplate, UserRowMapper userRowMapper) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public Optional<UserDto> findByUsername(String username) {
        log.debug("Searching for user with username=[{}]", username);
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(APP_USER_USERNAME_COLUMN, username);

        List<UserDto> users = namedJdbcTemplate.query(
                GET_USER_BY_USERNAME_QUERY,
                params,
                userRowMapper
        );

        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public List<String> findRolesByUserId(Long userId) {
        log.debug("Fetching roles for userId=[{}]", userId);
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(USER_ROLES_USER_ID_COLUMN, userId);

        return namedJdbcTemplate.queryForList(
                GET_USER_ROLES_BY_USER_ID_QUERY,
                params,
                String.class
        );
    }
}
