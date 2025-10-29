package io.github.andrei021.store.persistence.mapper;

import io.github.andrei021.store.common.dto.UserDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static io.github.andrei021.store.persistence.constant.UserTableConstants.APP_USER_ID_COLUMN;
import static io.github.andrei021.store.persistence.constant.UserTableConstants.APP_USER_PASSWORD_COLUMN;
import static io.github.andrei021.store.persistence.constant.UserTableConstants.APP_USER_USERNAME_COLUMN;

@Component
public class UserRowMapper implements RowMapper<UserDto> {

    @Override
    public UserDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new UserDto(
                rs.getLong(APP_USER_ID_COLUMN),
                rs.getString(APP_USER_USERNAME_COLUMN),
                rs.getString(APP_USER_PASSWORD_COLUMN),
                List.of()
        );
    }
}
