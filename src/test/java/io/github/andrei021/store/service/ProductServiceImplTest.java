package io.github.andrei021.store.service;

import io.github.andrei021.store.common.dto.request.BuyProductRequestDto;
import io.github.andrei021.store.common.dto.request.CreateProductRequestDto;
import io.github.andrei021.store.common.dto.response.PaginatedResponseDto;
import io.github.andrei021.store.common.dto.response.ProductResponseDto;
import io.github.andrei021.store.exception.instance.InsufficientStockException;
import io.github.andrei021.store.exception.instance.InvalidOffsetException;
import io.github.andrei021.store.exception.instance.ProductAlreadyExistsException;
import io.github.andrei021.store.exception.instance.ProductNotFoundException;
import io.github.andrei021.store.persistence.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    private static final String NOT_NULL_PRODUCT_MSG = "Product should not be null";
    private static final String ID_SHOULD_MATCH_MSG = "Product id should match";
    private static final String NAME_SHOULD_MATCH_MSG = "Product name should match";
    private static final String PRICE_SHOULD_MATCH_MSG = "Product price should match";
    private static final String STOCK_SHOULD_MATCH_MSG = "Product stock should match";

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Nested
    @DisplayName("findById()")
    class FindByIdTests {

        @Test
        @DisplayName("findById should return ProductResponseDto when product exists")
        void findById_shouldReturnProductResponseDto_whenItExistsInDb() {
            long id = 1L;
            ProductResponseDto product = buildProduct(id, "p1", BigDecimal.valueOf(15.15), 1);
            when(productRepository.findById(id)).thenReturn(Optional.of(product));

            ProductResponseDto actualProduct = productService.findById(id);
            Mockito.verify(productRepository).findById(id);

            assertAll(
                    () -> assertNotNull(actualProduct, NOT_NULL_PRODUCT_MSG),
                    () -> assertEquals(id, actualProduct.id(), ID_SHOULD_MATCH_MSG),
                    () -> assertEquals("p1", actualProduct.name(), NAME_SHOULD_MATCH_MSG),
                    () -> assertEquals(0, BigDecimal.valueOf(15.15).compareTo(actualProduct.price()), PRICE_SHOULD_MATCH_MSG),
                    () -> assertEquals(1, actualProduct.stock(), STOCK_SHOULD_MATCH_MSG)
            );
        }

        @Test
        @DisplayName("findById should throw ProductNotFoundException when product does not exist")
        void findById_shouldThrowProductNotFoundException_whenItDoesNotExistInDb() {
            long id = 99L;
            when(productRepository.findById(id)).thenReturn(Optional.empty());

            ProductNotFoundException exception = assertThrows(
                    ProductNotFoundException.class,
                    () -> productService.findById(id),
                    "Expected findById() to throw, but it didn't"
            );

            Mockito.verify(productRepository).findById(id);
            assertTrue(exception.getMessage().contains(String.valueOf(id)),
                    "Exception message should contain the product id");

        }
    }

    @Nested
    @DisplayName("findByName()")
    class FindByNameTests {

        @Test
        @DisplayName("findByName should return ProductResponseDto when product exists")
        void findByName_shouldReturnProductResponseDto_whenItExistsInDb() {
            String name = "p1";
            ProductResponseDto product = buildProduct(1L, name, BigDecimal.valueOf(15.15), 1);
            when(productRepository.findByName(name)).thenReturn(Optional.of(product));

            ProductResponseDto actualProduct = productService.findByName(name);
            Mockito.verify(productRepository).findByName(name);

            assertAll(
                    () -> assertNotNull(actualProduct, NOT_NULL_PRODUCT_MSG),
                    () -> assertEquals(1L, actualProduct.id(), ID_SHOULD_MATCH_MSG),
                    () -> assertEquals(name, actualProduct.name(), NAME_SHOULD_MATCH_MSG),
                    () -> assertEquals(0, BigDecimal.valueOf(15.15).compareTo(actualProduct.price()), PRICE_SHOULD_MATCH_MSG),
                    () -> assertEquals(1, actualProduct.stock(), STOCK_SHOULD_MATCH_MSG)
            );
        }

        @Test
        @DisplayName("findByName should throw ProductNotFoundException when product does not exist")
        void findByName_shouldThrowProductNotFoundException_whenItDoesNotExistInDb() {
            String name = "nonexistent";
            when(productRepository.findByName(name)).thenReturn(Optional.empty());

            ProductNotFoundException exception = assertThrows(
                    ProductNotFoundException.class,
                    () -> productService.findByName(name),
                    "Expected findByName() to throw, but it didn't"
            );

            Mockito.verify(productRepository).findByName(name);
            assertTrue(exception.getMessage().contains(name),
                    "Exception message should contain the product name");
        }
    }

    @Nested
    @DisplayName("getPaginatedProducts()")
    class GetPaginatedProductsTests {

        @Test
        @DisplayName("getPaginatedProducts should return PaginatedResponseDto when offset and limit are valid")
        void getPaginatedProducts_shouldReturnPaginatedResponse_whenValidOffsetAndLimit() {
            int offset = 0;
            int limit = 2;
            String baseUrl = "/api/products";

            List<ProductResponseDto> products = List.of(
                    buildProduct(1L, "p1", BigDecimal.valueOf(10.0), 5),
                    buildProduct(2L, "p2", BigDecimal.valueOf(20.0), 3)
            );
            when(productRepository.getPaginatedProducts(offset, limit)).thenReturn(products);

            PaginatedResponseDto<ProductResponseDto> response =
                    productService.getPaginatedProducts(offset, limit, baseUrl);
            Mockito.verify(productRepository).getPaginatedProducts(offset, limit);

            assertAll(
                    () -> assertNotNull(response),
                    () -> assertEquals(products, response.content()),
                    () -> assertEquals(offset, response.offset()),
                    () -> assertEquals(limit, response.limit()),
                    () -> assertEquals(baseUrl + "?offset=2&limit=2", response.nextPage()),
                    () -> assertNull(response.prevPage()),
                    () -> assertTrue(response.hasNext()),
                    () -> assertFalse(response.hasPrevious())
            );
        }

        @Test
        @DisplayName("getPaginatedProducts should throw InvalidOffsetException when offset is negative")
        void getPaginatedProducts_shouldThrowInvalidOffsetException_whenOffsetIsNegative() {
            int offset = -1;
            int limit = 5;

            InvalidOffsetException exception = assertThrows(
                    InvalidOffsetException.class,
                    () -> productService.getPaginatedProducts(offset, limit, "/api/products")
            );

            Mockito.verifyNoInteractions(productRepository);
            assertTrue(exception.getMessage().contains(String.valueOf(offset)));
        }

        @Test
        @DisplayName("getPaginatedProducts should adjust limit to default if limit is zero or negative")
        void getPaginatedProducts_shouldAdjustLimitToDefault_whenLimitIsInvalid() {
            int offset = 0;
            int limit = 0;

            int defaultLimit = 10;
            List<ProductResponseDto> products = List.of(
                    buildProduct(1L, "p1", BigDecimal.valueOf(10.0), 5)
            );
            when(productRepository.getPaginatedProducts(offset, defaultLimit)).thenReturn(products);

            PaginatedResponseDto<ProductResponseDto> response =
                    productService.getPaginatedProducts(offset, limit, "/api/products");
            Mockito.verify(productRepository).getPaginatedProducts(offset, defaultLimit);

            assertEquals(defaultLimit, response.limit());
        }

        @Test
        @DisplayName("getPaginatedProducts should adjust limit to max if limit exceeds MAX_PAGINATION_LIMIT")
        void getPaginatedProducts_shouldAdjustLimitToMax_whenLimitExceedsMax() {
            int offset = 0;
            int limit = 999;

            int maxLimit = 50;
            List<ProductResponseDto> products = List.of(
                    buildProduct(1L, "p1", BigDecimal.valueOf(10.0), 5)
            );
            when(productRepository.getPaginatedProducts(offset, maxLimit)).thenReturn(products);

            PaginatedResponseDto<ProductResponseDto> response =
                    productService.getPaginatedProducts(offset, limit, "/api/products");
            Mockito.verify(productRepository).getPaginatedProducts(offset, maxLimit);

            assertEquals(maxLimit, response.limit());
        }
    }

    @Nested
    @DisplayName("createProduct()")
    class CreateProductTests {

        @Test
        @DisplayName("createProduct should return ProductResponseDto when product is successfully created")
        void createProduct_shouldReturnProductResponseDto_whenSuccessful() {
            CreateProductRequestDto request = new CreateProductRequestDto("p1", BigDecimal.valueOf(15.15), 5);
            ProductResponseDto product = buildProduct(1L, request.name(), request.price(), request.stock());
            when(productRepository.createProduct(request.name(), request.price(), request.stock())).thenReturn(product);

            ProductResponseDto actualProduct = productService.createProduct(request);
            Mockito.verify(productRepository).createProduct(request.name(), request.price(), request.stock());

            assertAll(
                    () -> assertNotNull(actualProduct, NOT_NULL_PRODUCT_MSG),
                    () -> assertEquals(1L, actualProduct.id(), ID_SHOULD_MATCH_MSG),
                    () -> assertEquals(request.name(), actualProduct.name(), NAME_SHOULD_MATCH_MSG),
                    () -> assertEquals(0, request.price().compareTo(actualProduct.price()), PRICE_SHOULD_MATCH_MSG),
                    () -> assertEquals(request.stock(), actualProduct.stock(), STOCK_SHOULD_MATCH_MSG)
            );
        }

        @Test
        @DisplayName("createProduct should throw ProductAlreadyExistsException when name already exists")
        void createProduct_shouldThrowProductAlreadyExistsException_whenNameExists() {
            CreateProductRequestDto request = new CreateProductRequestDto("p1", BigDecimal.valueOf(15.15), 5);
            when(productRepository.createProduct(request.name(), request.price(), request.stock()))
                    .thenThrow(new DataIntegrityViolationException("Already exists"));

            ProductAlreadyExistsException exception = assertThrows(
                    ProductAlreadyExistsException.class,
                    () -> productService.createProduct(request)
            );

            Mockito.verify(productRepository).createProduct(request.name(), request.price(), request.stock());
            String expectedMessage = String.format("Product with name=[%s] already exists", request.name());
            assertEquals(expectedMessage, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("buyProduct()")
    class BuyProductTests {

        @Test
        @DisplayName("buyProduct should return ProductResponseDto when product is successfully bought")
        void buyProduct_shouldReturnProductResponseDto_whenSuccessful() {
            long id = 1L;
            BuyProductRequestDto request = new BuyProductRequestDto(id);
            ProductResponseDto product = buildProduct(id, "p1", BigDecimal.valueOf(15.15), 5);

            when(productRepository.buyProduct(id)).thenReturn(true);
            when(productRepository.findById(id)).thenReturn(Optional.of(product));

            ProductResponseDto actualProduct = productService.buyProduct(request);
            Mockito.verify(productRepository).buyProduct(id);
            Mockito.verify(productRepository, Mockito.times(1)).findById(id);

            assertAll(
                    () -> assertNotNull(actualProduct, NOT_NULL_PRODUCT_MSG),
                    () -> assertEquals(id, actualProduct.id(), ID_SHOULD_MATCH_MSG),
                    () -> assertEquals("p1", actualProduct.name(), NAME_SHOULD_MATCH_MSG),
                    () -> assertEquals(0, BigDecimal.valueOf(15.15).compareTo(actualProduct.price()), PRICE_SHOULD_MATCH_MSG),
                    () -> assertEquals(5, actualProduct.stock(), STOCK_SHOULD_MATCH_MSG)
            );
        }

        @Test
        @DisplayName("buyProduct should throw InsufficientStockException when product exists but stock is zero")
        void buyProduct_shouldThrowInsufficientStockException_whenOutOfStock() {
            long id = 1L;
            BuyProductRequestDto request = new BuyProductRequestDto(id);
            ProductResponseDto product = buildProduct(id, "p1", BigDecimal.valueOf(15.15), 0);

            when(productRepository.buyProduct(id)).thenReturn(false);
            when(productRepository.findById(id)).thenReturn(Optional.of(product));

            InsufficientStockException exception = assertThrows(
                    InsufficientStockException.class,
                    () -> productService.buyProduct(request)
            );

            Mockito.verify(productRepository).buyProduct(id);
            Mockito.verify(productRepository).findById(id);

            String expectedMessage = String.format("Product with id=[%d] is out of stock", id);
            assertEquals(expectedMessage, exception.getMessage());
        }

        @Test
        @DisplayName("buyProduct should throw ProductNotFoundException when product does not exist")
        void buyProduct_shouldThrowProductNotFoundException_whenProductDoesNotExist() {
            long id = 99L;
            BuyProductRequestDto request = new BuyProductRequestDto(id);

            when(productRepository.buyProduct(id)).thenReturn(false);
            when(productRepository.findById(id)).thenReturn(Optional.empty());

            ProductNotFoundException exception = assertThrows(
                    ProductNotFoundException.class,
                    () -> productService.buyProduct(request)
            );

            Mockito.verify(productRepository).buyProduct(id);
            Mockito.verify(productRepository).findById(id);

            String expectedMessage = String.format("Product not found with id=[%d]", id);
            assertEquals(expectedMessage, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("changePrice()")
    class ChangePriceTests {

        @Test
        @DisplayName("changePrice should return ProductResponseDto when product is successfully updated")
        void changePrice_shouldReturnProductResponseDto_whenSuccessful() {
            long id = 1L;
            BigDecimal newPrice = BigDecimal.valueOf(20.50);
            ProductResponseDto updatedProduct = buildProduct(id, "p1", newPrice, 5);

            when(productRepository.changePrice(id, newPrice)).thenReturn(true);
            when(productRepository.findById(id)).thenReturn(Optional.of(updatedProduct));

            ProductResponseDto actualProduct = productService.changePrice(id, newPrice);
            Mockito.verify(productRepository).changePrice(id, newPrice);
            Mockito.verify(productRepository).findById(id);

            assertAll(
                    () -> assertNotNull(actualProduct, NOT_NULL_PRODUCT_MSG),
                    () -> assertEquals(id, actualProduct.id(), ID_SHOULD_MATCH_MSG),
                    () -> assertEquals("p1", actualProduct.name(), NAME_SHOULD_MATCH_MSG),
                    () -> assertEquals(0, newPrice.compareTo(actualProduct.price()), PRICE_SHOULD_MATCH_MSG),
                    () -> assertEquals(5, actualProduct.stock(), STOCK_SHOULD_MATCH_MSG)
            );
        }

        @Test
        @DisplayName("changePrice should throw ProductNotFoundException when product does not exist")
        void changePrice_shouldThrowProductNotFoundException_whenProductDoesNotExist() {
            long id = 99L;
            BigDecimal newPrice = BigDecimal.valueOf(20.50);

            when(productRepository.changePrice(id, newPrice)).thenReturn(false);

            ProductNotFoundException exception = assertThrows(
                    ProductNotFoundException.class,
                    () -> productService.changePrice(id, newPrice)
            );

            Mockito.verify(productRepository).changePrice(id, newPrice);
            String expectedMessage = String.format("Product not found with id=[%d]", id);
            assertEquals(expectedMessage, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("deleteProduct()")
    class DeleteProductTests {

        @Test
        @DisplayName("deleteProduct should complete successfully when product exists")
        void deleteProduct_shouldCompleteSuccessfully_whenProductExists() {
            long id = 1L;
            when(productRepository.deleteProduct(id)).thenReturn(true);
            assertDoesNotThrow(() -> productService.deleteProduct(1L));
            Mockito.verify(productRepository).deleteProduct(id);
        }

        @Test
        @DisplayName("deleteProduct should throw ProductNotFoundException when product does not exist")
        void deleteProduct_shouldThrowProductNotFoundException_whenProductDoesNotExist() {
            long id = 99L;

            when(productRepository.deleteProduct(id)).thenReturn(false);
            ProductNotFoundException exception = assertThrows(
                    ProductNotFoundException.class,
                    () -> productService.deleteProduct(id)
            );

            Mockito.verify(productRepository).deleteProduct(id);
            String expectedMessage = String.format("Product not found with id=[%d]", id);
            assertEquals(expectedMessage, exception.getMessage());
        }
    }

    private ProductResponseDto buildProduct(long id, String name, BigDecimal price, int stock) {
        return new ProductResponseDto(
                id,
                name,
                price,
                stock
        );
    }
}
