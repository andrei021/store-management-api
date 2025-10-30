package io.github.andrei021.store.controller.v1.docs;

import io.github.andrei021.store.common.dto.response.StoreApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Paginated list of products retrieved successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = StoreApiResponse.class),
                        examples = @ExampleObject(value = SwaggerResponseExamples.PAGINATED_PRODUCTS_RESPONSE)
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid offset",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = StoreApiResponse.class),
                        examples = @ExampleObject(value = SwaggerResponseExamples.INVALID_OFFSET_EXCEPTION)
                )
        )
})
public @interface GetPaginatedProductsApiDocumentation {
}
