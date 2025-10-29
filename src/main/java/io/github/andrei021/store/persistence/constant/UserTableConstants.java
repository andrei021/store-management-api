package io.github.andrei021.store.persistence.constant;

public final class UserTableConstants {

    public static final String USER_TABLE_NAME = "app_user";
    public static final String USER_ROLES_TABLE_NAME = "user_roles";

    public static final String APP_USER_ID_COLUMN = "id";
    public static final String APP_USER_USERNAME_COLUMN = "username";
    public static final String APP_USER_PASSWORD_COLUMN = "password";

    public static final String USER_ROLES_USER_ID_COLUMN = "user_id";
    public static final String USER_ROLES_ROLE_COLUMN = "role";

    private UserTableConstants() {
    }
}
