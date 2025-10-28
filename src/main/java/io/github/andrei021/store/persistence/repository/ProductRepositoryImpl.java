package io.github.andrei021.store.persistence.repository;

import io.github.andrei021.store.common.dto.response.ProductResponseDto;
import io.github.andrei021.store.persistence.DefaultSqlQueryProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static io.github.andrei021.store.persistence.DefaultSqlQueryProvider.ADD_PRODUCT_QUERY;
import static io.github.andrei021.store.persistence.DefaultSqlQueryProvider.BUY_PRODUCT_QUERY;
import static io.github.andrei021.store.persistence.DefaultSqlQueryProvider.DELETE_PRODUCT_QUERY;
import static io.github.andrei021.store.persistence.DefaultSqlQueryProvider.GET_PAGINATED_PRODUCTS_QUERY;
import static io.github.andrei021.store.persistence.DefaultSqlQueryProvider.GET_PRODUCT_BY_ID_QUERY;
import static io.github.andrei021.store.persistence.DefaultSqlQueryProvider.GET_PRODUCT_BY_NAME_QUERY;
import static io.github.andrei021.store.persistence.constant.ProductTableConstants.PRODUCT_ID_COLUMN;
import static io.github.andrei021.store.persistence.constant.ProductTableConstants.PRODUCT_NAME_COLUMN;
import static io.github.andrei021.store.persistence.constant.ProductTableConstants.PRODUCT_NAME_NORMALIZED_COLUMN;
import static io.github.andrei021.store.persistence.constant.ProductTableConstants.PRODUCT_PRICE_COLUMN;
import static io.github.andrei021.store.persistence.constant.ProductTableConstants.PRODUCT_STOCK_COLUMN;

@Repository
@Slf4j
public class ProductRepositoryImpl implements ProductRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final RowMapper<ProductResponseDto> rowMapper;

    public ProductRepositoryImpl(NamedParameterJdbcTemplate namedJdbcTemplate, RowMapper<ProductResponseDto> rowMapper) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public Optional<ProductResponseDto> findById(long id) {
        log.debug("Searching for product with id=[{}]", id);
        MapSqlParameterSource params = new MapSqlParameterSource(PRODUCT_ID_COLUMN, id);
        return searchForProduct(GET_PRODUCT_BY_ID_QUERY, params);
    }

    @Override
    public Optional<ProductResponseDto> findByName(String name) {
        log.debug("Searching for product with name=[{}]", name);
        String normalizedName = name.trim().toUpperCase();
        MapSqlParameterSource params = new MapSqlParameterSource(PRODUCT_NAME_NORMALIZED_COLUMN, normalizedName);
        return searchForProduct(GET_PRODUCT_BY_NAME_QUERY, params);
    }

    @Override
    public List<ProductResponseDto> getPaginatedProducts(int offset, int limit) {
        log.debug("Fetching products with offset=[{}] and limit=[{}]", offset, limit);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("offset", offset)
                .addValue("limit", limit);

        List<ProductResponseDto> products = namedJdbcTemplate.query(GET_PAGINATED_PRODUCTS_QUERY, params, rowMapper);

        log.info("Fetched [{}] products (offset={}, limit={})", products.size(), offset, limit);
        return products;
    }

    @Override
    public ProductResponseDto createProduct(String name, BigDecimal price, int stock) {
        log.debug("Adding new product with name=[{}], price=[{}], stock=[{}]", name, price, stock);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PRODUCT_NAME_COLUMN, name)
                .addValue(PRODUCT_NAME_NORMALIZED_COLUMN, name.trim().toUpperCase())
                .addValue(PRODUCT_PRICE_COLUMN, price)
                .addValue(PRODUCT_STOCK_COLUMN, stock);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbcTemplate.update(ADD_PRODUCT_QUERY, params, keyHolder, new String[]{PRODUCT_ID_COLUMN});

        long generatedId = keyHolder.getKey().longValue();
        log.info("Inserted product with id=[{}] and name=[{}]", generatedId, name);
        return new ProductResponseDto(generatedId, name, price, stock);
    }

    @Override
    public boolean buyProduct(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource(PRODUCT_ID_COLUMN, id);
        int updated = namedJdbcTemplate.update(BUY_PRODUCT_QUERY, params);

        if (updated > 0) {
            log.info("Successfully bought 1 unit of product with id=[{}]", id);
            return true;
        } else {
            log.info("Failed to buy product with id=[{}] (out of stock or not found)", id);
            return false;
        }
    }

    @Override
    public boolean changePrice(long id, BigDecimal newPrice) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PRODUCT_ID_COLUMN, id)
                .addValue(PRODUCT_PRICE_COLUMN, newPrice);

        int updated = namedJdbcTemplate.update(DefaultSqlQueryProvider.CHANGE_PRICE_QUERY, params);

        if (updated > 0) {
            log.info("Successfully changed price for product with id=[{}] to [{}]", id, newPrice);
            return true;
        } else {
            log.info("Failed to change price for product with id=[{}] (product not found)", id);
            return false;
        }
    }

    @Override
    public boolean deleteProduct(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource(PRODUCT_ID_COLUMN, id);
        int deleted = namedJdbcTemplate.update(DELETE_PRODUCT_QUERY, params);

        if (deleted == 0) {
            log.warn("No product found with id=[{}] to delete", id);
            return false;
        }

        log.info("Successfully deleted product with id=[{}]", id);
        return true;
    }

    private Optional<ProductResponseDto> searchForProduct(String sql, MapSqlParameterSource params) {
        try {
            ProductResponseDto product = namedJdbcTemplate.queryForObject(sql, params, rowMapper);
            log.info("Found product with id=[{}] and name=[{}]", product.id(), product.name());
            return Optional.ofNullable(product);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
