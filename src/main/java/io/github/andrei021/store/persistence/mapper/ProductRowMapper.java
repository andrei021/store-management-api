package io.github.andrei021.store.persistence.mapper;

import io.github.andrei021.store.persistence.model.ProductDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import static io.github.andrei021.store.persistence.constant.ProductTableConstants.PRODUCT_ID_COLUMN;
import static io.github.andrei021.store.persistence.constant.ProductTableConstants.PRODUCT_NAME_COLUMN;
import static io.github.andrei021.store.persistence.constant.ProductTableConstants.PRODUCT_PRICE_COLUMN;
import static io.github.andrei021.store.persistence.constant.ProductTableConstants.PRODUCT_STOCK_COLUMN;

@Component
public class ProductRowMapper implements RowMapper<ProductDto> {

    @Override
    public ProductDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ProductDto(
                rs.getLong(PRODUCT_ID_COLUMN),
                rs.getString(PRODUCT_NAME_COLUMN),
                rs.getBigDecimal(PRODUCT_PRICE_COLUMN),
                rs.getInt(PRODUCT_STOCK_COLUMN)
        );
    }
}
