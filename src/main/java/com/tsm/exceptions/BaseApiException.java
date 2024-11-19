package com.tsm.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Abstract base class for all custom API exceptions.
 * 
 * <p>This class serves as a foundation for creating custom exceptions that will be used in the application.
 * All custom exceptions should inherit from this class to ensure they have an associated HTTP status and
 * are easily handled by a global exception handler.</p>
 * 
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Provides a consistent structure for all custom API exceptions.</li>
 *   <li>Each exception contains an HTTP status code to indicate the type of error.</li>
 *   <li>Extends {@link RuntimeException} to provide unchecked exceptions that don't require explicit handling in
 *       method signatures.</li>
 * </ul>
 * 
 * <p>All custom exceptions will extend this class to inherit the behavior of associating an {@link HttpStatus} 
 * with the exception, making them easier to manage in global exception handling mechanisms like 
 * {@link GlobalExceptionHandler}.</p>
 * 
 * @see GlobalExceptionHandler
 */

public abstract class BaseApiException extends RuntimeException {
    private final HttpStatus status;

    protected BaseApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}