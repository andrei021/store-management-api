package io.github.andrei021.store;

import io.github.andrei021.store.common.dto.request.BuyProductRequestDto;
import io.github.andrei021.store.common.dto.response.ApiResponse;
import io.github.andrei021.store.common.dto.response.PaginatedResponseDto;
import io.github.andrei021.store.common.dto.response.ProductResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GetAndBuyProductIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void getAndBuyProductHappyFlowTest() {
		restTemplate = restTemplate.withBasicAuth("user", "user");
		ResponseEntity<ApiResponse<PaginatedResponseDto<ProductResponseDto>>> paginatedResponse =
				restTemplate.exchange(
						"/api/v1/products?offset=0&limit=10",
						HttpMethod.GET,
						null,
						new ParameterizedTypeReference<>() {}
				);

		assertEquals(HttpStatus.OK, paginatedResponse.getStatusCode());
		assertNotNull(paginatedResponse.getBody());
		assertFalse(paginatedResponse.getBody().data().content().isEmpty());

		ProductResponseDto product = paginatedResponse.getBody().data().content().getFirst();

		BuyProductRequestDto request = new BuyProductRequestDto(product.id());
		ResponseEntity<ApiResponse<ProductResponseDto>> buyResponse =
				restTemplate.exchange(
						"/api/v1/products/buy",
						HttpMethod.POST,
						new HttpEntity<>(request),
						new ParameterizedTypeReference<>() {}
				);

		assertEquals(HttpStatus.OK, buyResponse.getStatusCode());
		assertNotNull(buyResponse.getBody());

		ProductResponseDto updatedProduct = buyResponse.getBody().data();
		assertEquals(product.id(), updatedProduct.id());
		assertEquals(product.stock() - 1, updatedProduct.stock());
	}
}
