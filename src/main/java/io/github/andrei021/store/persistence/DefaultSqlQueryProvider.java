package io.github.andrei021.store.persistence;

import static io.github.andrei021.store.persistence.constant.ProductTableConstants.PRODUCT_ID_COLUMN;
import static io.github.andrei021.store.persistence.constant.ProductTableConstants.PRODUCT_NAME_NORMALIZED_COLUMN;
import static io.github.andrei021.store.persistence.constant.ProductTableConstants.PRODUCT_TABLE_NAME;

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
}
