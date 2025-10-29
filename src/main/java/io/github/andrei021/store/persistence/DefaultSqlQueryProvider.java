package io.github.andrei021.store.persistence;

import static io.github.andrei021.store.persistence.constant.ProductTableConstants.PRODUCT_ID_COLUMN;
import static io.github.andrei021.store.persistence.constant.ProductTableConstants.PRODUCT_NAME_COLUMN;
import static io.github.andrei021.store.persistence.constant.ProductTableConstants.PRODUCT_NAME_NORMALIZED_COLUMN;
import static io.github.andrei021.store.persistence.constant.ProductTableConstants.PRODUCT_PRICE_COLUMN;
import static io.github.andrei021.store.persistence.constant.ProductTableConstants.PRODUCT_STOCK_COLUMN;
import static io.github.andrei021.store.persistence.constant.ProductTableConstants.PRODUCT_TABLE_NAME;
import static io.github.andrei021.store.persistence.constant.UserTableConstants.APP_USER_USERNAME_COLUMN;
import static io.github.andrei021.store.persistence.constant.UserTableConstants.USER_ROLES_ROLE_COLUMN;
import static io.github.andrei021.store.persistence.constant.UserTableConstants.USER_ROLES_TABLE_NAME;
import static io.github.andrei021.store.persistence.constant.UserTableConstants.USER_ROLES_USER_ID_COLUMN;
import static io.github.andrei021.store.persistence.constant.UserTableConstants.USER_TABLE_NAME;

public final class DefaultSqlQueryProvider {

    private DefaultSqlQueryProvider() {}

    public static final String GET_PRODUCT_BY_ID_QUERY =
            "SELECT * FROM " + PRODUCT_TABLE_NAME +
                    " WHERE " + PRODUCT_ID_COLUMN + " = :" + PRODUCT_ID_COLUMN;

    public static final String GET_PRODUCT_BY_NAME_QUERY =
            "SELECT * FROM " + PRODUCT_TABLE_NAME +
                    " WHERE " + PRODUCT_NAME_NORMALIZED_COLUMN + " = :" + PRODUCT_NAME_NORMALIZED_COLUMN;

    public static final String GET_PAGINATED_PRODUCTS_QUERY =
            "SELECT * FROM " + PRODUCT_TABLE_NAME +
                    " ORDER BY " + PRODUCT_ID_COLUMN +
                    " ASC LIMIT :limit OFFSET :offset";

    public static final String ADD_PRODUCT_QUERY =
            "INSERT INTO " + PRODUCT_TABLE_NAME + " (" +
                    PRODUCT_NAME_COLUMN + ", " +
                    PRODUCT_NAME_NORMALIZED_COLUMN + ", " +
                    PRODUCT_PRICE_COLUMN + ", " +
                    PRODUCT_STOCK_COLUMN +
                    ") VALUES (:" + PRODUCT_NAME_COLUMN + ", :" + PRODUCT_NAME_NORMALIZED_COLUMN +
                    ", :" + PRODUCT_PRICE_COLUMN + ", :" + PRODUCT_STOCK_COLUMN + ")";

    // Could not use 'RETURNING*' in H2 order to get the entry after it was updated
    // I need to query multiple times
    public static final String BUY_PRODUCT_QUERY =
            "UPDATE " + PRODUCT_TABLE_NAME +
                    " SET " + PRODUCT_STOCK_COLUMN + " = " + PRODUCT_STOCK_COLUMN + " - 1" +
                    " WHERE " + PRODUCT_ID_COLUMN + " = :" + PRODUCT_ID_COLUMN + " AND " + PRODUCT_STOCK_COLUMN + " > 0";

    public static final String CHANGE_PRICE_QUERY =
            "UPDATE " + PRODUCT_TABLE_NAME +
                    " SET " + PRODUCT_PRICE_COLUMN + " = :" + PRODUCT_PRICE_COLUMN +
                    " WHERE " + PRODUCT_ID_COLUMN + " = :" + PRODUCT_ID_COLUMN;

    public static final String DELETE_PRODUCT_QUERY =
            "DELETE FROM " + PRODUCT_TABLE_NAME +
                    " WHERE " + PRODUCT_ID_COLUMN + " = :" + PRODUCT_ID_COLUMN;

    public static final String GET_USER_BY_USERNAME_QUERY =
            "SELECT * FROM " + USER_TABLE_NAME +
                    " WHERE " + APP_USER_USERNAME_COLUMN + " = :" + APP_USER_USERNAME_COLUMN;

    public static final String GET_USER_ROLES_BY_USER_ID_QUERY =
            "SELECT " + USER_ROLES_ROLE_COLUMN + " FROM " + USER_ROLES_TABLE_NAME +
                    " WHERE " + USER_ROLES_USER_ID_COLUMN + " = :" + USER_ROLES_USER_ID_COLUMN;
}
