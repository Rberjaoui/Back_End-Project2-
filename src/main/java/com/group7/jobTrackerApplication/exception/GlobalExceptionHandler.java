package com.group7.jobTrackerApplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * Global exception handler for the application.
 * Maps custom exceptions to their corresponding HTTP status codes and returns a JSON error response.
 *
 * @author Raphael Berjaoui
 * @version 0.1.0
 * @since 2026-03-01
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException and returns a 404 Not Found response.
     *
     * @param ex the exception containing the error message
     * @return a 404 response with the error message
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    /**
     * Handles NotAuthenticatedException and returns a 401 Unauthorized response.
     *
     * @param ex the exception containing the error message
     * @return a 401 response with the error message
     */
    @ExceptionHandler(NotAuthenticatedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorized(NotAuthenticatedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", ex.getMessage()));
    }

    /**
     * Handles ForbiddenException and returns a 403 Forbidden response.
     *
     * @param ex the exception containing the error message
     * @return a 403 response with the error message
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, String>> handleForbidden(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", ex.getMessage()));
    }

    /**
     * Handles all unhandled exceptions and returns a 500 Internal Server Error response.
     *
     * @param ex the exception containing the error message
     * @return a 500 response with the error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", ex.getClass().getSimpleName() + ": " + ex.getMessage()));
    }
}