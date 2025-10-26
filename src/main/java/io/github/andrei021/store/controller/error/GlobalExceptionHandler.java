package io.github.andrei021.store.controller.error;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException exception, WebRequest request) {
        StringBuilder clientMessage = new StringBuilder();
        String endpoint = extractEndpoint(request);

        exception.getConstraintViolations().forEach(violation -> {
            Object invalidValue = violation.getInvalidValue();
            String message = violation.getMessage();

            logger.warn("{} when calling the endpoint [{}], but got [{}]", message, endpoint, invalidValue);

            if (!clientMessage.isEmpty()) {
                clientMessage.append(System.lineSeparator());
            }

            String actualValueMessage = String.format(", but got [%s]", invalidValue);
            clientMessage.append(message).append(actualValueMessage);
        });

        return ResponseEntity.badRequest().body(clientMessage.toString());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException exception, WebRequest request) {
        String paramName = exception.getName();
        Object invalidValue = exception.getValue();
        String expectedType = exception.getRequiredType() != null ? exception.getRequiredType().getSimpleName() : "unknown";
        String endpoint = extractEndpoint(request);

        logger.warn("Invalid parameter type for [{}] when calling the endpoint [{}]: expected [{}], but got [{}]",
                paramName, endpoint, expectedType, invalidValue);

        String message = String.format("Invalid parameter [%s]: expected [%s], but got [%s]",
                paramName, expectedType, invalidValue);

        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingQueryParams(MissingServletRequestParameterException exception, WebRequest request) {
        String paramName = exception.getParameterName();
        String endpoint = extractEndpoint(request);
        String message = String.format("Missing required parameter [%s]. Please check API documentation", paramName);

        logger.warn("Required parameter [{}] is missing for endpoint [{}]", paramName, endpoint);

        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnknown(Exception exception) {
        logger.error("Unexpected error occurred", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred. Please try again later");
    }


    private String extractEndpoint(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}
