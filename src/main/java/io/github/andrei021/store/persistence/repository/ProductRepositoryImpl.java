package io.github.andrei021.store.persistence.repository;

import io.github.andrei021.store.common.dto.response.ProductResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static io.github.andrei021.store.persistence.DefaultSqlQueryProvider.GET_PAGINATED_PRODUCTS_QUERY;
import static io.github.andrei021.store.persistence.DefaultSqlQueryProvider.GET_PRODUCT_BY_ID_QUERY;
import static io.github.andrei021.store.persistence.DefaultSqlQueryProvider.GET_PRODUCT_BY_NAME_QUERY;
import static io.github.andrei021.store.persistence.constant.ProductTableConstants.PRODUCT_ID_COLUMN;
import static io.github.andrei021.store.persistence.constant.ProductTableConstants.PRODUCT_NAME_NORMALIZED_COLUMN;

@Repository
@Slf4j
public class ProductRepositoryImpl implements ProductRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final RowMapper<ProductResponseDto> rowMapper;

    public ProductRepositoryImpl(NamedParameterJdbcTemplate namedJdbcTemplate, RowMapper<ProductResponseDto> rowMapper) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.rowMapper = rowMapper;
    }

    public Optional<ProductResponseDto> findById(long id) {
        log.debug("Searching for product with id=[{}]", id);
        MapSqlParameterSource params = new MapSqlParameterSource(PRODUCT_ID_COLUMN, id);
        return searchForProduct(GET_PRODUCT_BY_ID_QUERY, params);
    }

    public Optional<ProductResponseDto> findByName(String name) {
        log.debug("Searching for product with name=[{}]", name);
        String normalizedName = name.trim().toUpperCase();
        MapSqlParameterSource params = new MapSqlParameterSource(PRODUCT_NAME_NORMALIZED_COLUMN, normalizedName);
        return searchForProduct(GET_PRODUCT_BY_NAME_QUERY, params);
    }

    public List<ProductResponseDto> getPaginatedProducts(int offset, int limit) {
        log.debug("Fetching products with offset=[{}] and limit=[{}]", offset, limit);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("offset", offset)
                .addValue("limit", limit);

        List<ProductResponseDto> products = namedJdbcTemplate.query(GET_PAGINATED_PRODUCTS_QUERY, params, rowMapper);

        log.info("Fetched [{}] products (offset={}, limit={})", products.size(), offset, limit);
        return products;
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
