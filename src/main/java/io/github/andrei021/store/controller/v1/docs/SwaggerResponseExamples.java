package io.github.andrei021.store.controller.v1.docs;

public final class SwaggerResponseExamples {
    public static final String PRODUCT_RESPONSE = """
        {
            "data": { "id": 1, "name": "Product1", "price": 499.99, "stock": 10 },
            "statusMessage": "SUCCESS",
            "timestamp": "2025-10-30T12:00:00Z"
        }
    """;

    public static final String PRODUCT_ALREADY_EXISTS_EXCEPTION = """
        {
            "data": { 
            "status": 409, 
            "error": "CONFLICT", 
            "message": "Product with name=[<value>] already exists", 
            "path": "/api/v1/admin/createProduct" 
            },
            "statusMessage": "FAILED_REQUEST",
            "timestamp": "2025-10-30T12:00:00Z"
        }
    """;

    public static final String PRODUCT_NOT_FOUND_EXCEPTION = """
        {
            "data": { 
                "status": 404,
                "error": "NOT_FOUND",
                "message": "Product not found with id=[<value>]",
                "path": "/api/v1/products/{id}" 
            },
            "statusMessage": "FAILED_REQUEST",
            "timestamp": "2025-10-30T12:00:00Z"
        }
    """;

    public static final String EMPTY_DATA = """
        {
            "data": null,
            "statusMessage": "SUCCESS",
            "timestamp": "2025-10-30T12:00:00Z"
        }
    """;

    public static final String PAGINATED_PRODUCTS_RESPONSE = """
        {
            "data": {
                "content": [
                    { "id": 1, "name": "Product1", "price": 9.99, "stock": 10 },
                    { "id": 2, "name": "Product2", "price": 10.99, "stock": 50 }
                ],
                "offset": 0,
                "limit": 2,
                "nextPage": "https://api.example.com/api/v1/products?offset=2&limit=2",
                "prevPage": null,
                "hasNext": true,
                "hasPrevious": false
            },
            "statusMessage": "SUCCESS",
            "timestamp": "2025-10-30T12:00:00Z"
        }
    """;

    public static final String INVALID_OFFSET_EXCEPTION = """
        {
            "data": {
                "status": 400,
                "error": "BAD_REQUEST",
                "message": "Offset must be greater than or equal to 0",
                "path": "/api/v1/products"
            },
            "statusMessage": "FAILED_REQUEST",
            "timestamp": "2025-10-30T12:00:00Z"
        }
    """;

    public static final String INSUFFICIENT_STOCK_EXCEPTION = """
    {
        "data": {
            "status": 409,
            "error": "CONFLICT",
            "message": "Product with id=[<value>] is out of stock",
            "path": "/api/v1/products/buy"
        },
        "statusMessage": "FAILED_REQUEST",
        "timestamp": "2025-10-30T12:00:00Z"
    }
    """;

    private SwaggerResponseExamples() {
    }
}
