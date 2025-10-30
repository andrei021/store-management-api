package io.github.andrei021.store.controller.v1.docs;

import io.github.andrei021.store.common.dto.response.StoreApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
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
                responseCode = "409",
                description = "Insufficient stock to complete purchase",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = StoreApiResponse.class),
                        examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                name = "Insufficient Stock Exception Example",
                                value = SwaggerResponseExamples.INSUFFICIENT_STOCK_EXCEPTION
                        )
                )
        )
})
public @interface InsufficientStockApiDocumentation {
}
