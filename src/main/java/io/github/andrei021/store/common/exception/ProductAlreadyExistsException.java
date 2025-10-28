package io.github.andrei021.store.common.exception;

public class ProductAlreadyExistsException extends RuntimeException {

    public ProductAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
