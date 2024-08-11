package com.tsm.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BaseApiException.class)
    public ResponseEntity<Map<String, Object>> handleBaseApiException(BaseApiException ex, WebRequest request) {
        LOGGER.error("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", ex.getStatus().value());
        errorResponse.put("error", ex.getStatus().getReasonPhrase());
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("path", request.getDescription(false));
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            LOGGER.error("Validation error in field '{}': {}", fieldName, errorMessage);
        });
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.put("message", "Validation failed");
        errorResponse.put("details", errors);
        errorResponse.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex, WebRequest request) {
    //     LOGGER.error("Error: {}", ex.getMessage());
    //     Map<String, Object> errorResponse = new HashMap<>();
    //     errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    //     errorResponse.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    //     errorResponse.put("message", "An unexpected error occurred. Please try again later.");
    //     errorResponse.put("timestamp", LocalDateTime.now());
    //     errorResponse.put("path", request.getDescription(false));
    //     return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    // }
}
