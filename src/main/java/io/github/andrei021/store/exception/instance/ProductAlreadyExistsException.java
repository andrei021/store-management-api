package io.github.andrei021.store.exception.instance;

public class ProductAlreadyExistsException extends RuntimeException {

    public ProductAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
