package io.github.andrei021.store.controller.error;

import io.github.andrei021.store.common.dto.response.ErrorResponseDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static io.github.andrei021.store.controller.ControllerUtil.buildErrorResponse;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolation(
            ConstraintViolationException exception,
            ServletWebRequest request
    ) {
        StringBuilder clientMessage = new StringBuilder();
        String requestUri = request.getRequest().getRequestURI();

        exception.getConstraintViolations().forEach(violation -> {
            Object invalidValue = violation.getInvalidValue();
            String message = violation.getMessage();

            log.warn("{} when calling the endpoint [{}], but got [{}]", message, requestUri, invalidValue);

            if (!clientMessage.isEmpty()) {
                clientMessage.append(";");
            }

            String actualValueMessage = String.format(", but got the value [%s]", invalidValue);
            clientMessage.append(message).append(actualValueMessage);
        });

        return buildErrorResponse(HttpStatus.BAD_REQUEST, clientMessage.toString(), request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception,
            ServletWebRequest request
    ) {
        String requestUri = request.getRequest().getRequestURI();
        String paramName = exception.getName();
        Object invalidValue = exception.getValue();
        String expectedType = exception.getRequiredType() != null ?
                exception.getRequiredType().getSimpleName() : "unknown";

        String message = String.format(
                "Invalid parameter type for [%s] when " +
                        "calling the endpoint [%s]: expected [%s], " +
                        "but got the value [%s]",
                paramName, requestUri, expectedType, invalidValue
        );

        log.warn(message);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingQueryParams(
            MissingServletRequestParameterException exception,
            ServletWebRequest request
    ) {
        String paramName = exception.getParameterName();
        String requestUri = request.getRequest().getRequestURI();
        String message = String.format("Missing required parameter [%s]. " +
                "Please check the API documentation", paramName);

        log.warn("Required parameter [{}] is missing for endpoint [{}]", paramName, requestUri);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleUnknown(
            Exception exception,
            ServletWebRequest request
    ) {
        log.error("Unexpected error occurred", exception);
        String message = "An unexpected error occurred. Please try again later";
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, request);
    }
}
