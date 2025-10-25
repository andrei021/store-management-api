package io.github.andrei021.store.persistence.repository;

import io.github.andrei021.store.persistence.model.ProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static io.github.andrei021.store.persistence.constant.ProductTableConstants.PRODUCT_ID_COLUMN;
import static io.github.andrei021.store.persistence.constant.ProductTableConstants.PRODUCT_TABLE_NAME;

@Repository
public class ProductRepository {

    private static final String SQL_FIND_BY_ID =
            "SELECT * FROM " + PRODUCT_TABLE_NAME + " WHERE " + PRODUCT_ID_COLUMN + " = :" + PRODUCT_ID_COLUMN;

    private static final Logger logger = LoggerFactory.getLogger(ProductRepository.class);

    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final RowMapper<ProductDto> rowMapper;

    public ProductRepository(NamedParameterJdbcTemplate namedJdbcTemplate, RowMapper<ProductDto> rowMapper) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.rowMapper = rowMapper;
    }

    public Optional<ProductDto> findById(long id) {
        logger.debug("Searching for product with id=[{}]", id);
        MapSqlParameterSource params = new MapSqlParameterSource(PRODUCT_ID_COLUMN, id);

        try {
            ProductDto product = namedJdbcTemplate.queryForObject(SQL_FIND_BY_ID, params, rowMapper);
            logger.info("Found product with id=[{}] and name=[{}]", product.getId(), product.getName());
            return Optional.ofNullable(product);
        } catch (EmptyResultDataAccessException e) {
            logger.warn("No product found having the id=[{}]", id);
            return Optional.empty();
        }
    }
}
