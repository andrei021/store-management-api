package io.github.andrei021.store.exception.instance;

public class InvalidOffsetException extends RuntimeException {

    public InvalidOffsetException(int offset) {
        super(String.format("Offset must be >= 0. Provided: [%d]", offset));
    }
}
