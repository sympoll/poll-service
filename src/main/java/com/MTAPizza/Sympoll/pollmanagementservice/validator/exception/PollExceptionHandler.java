package com.MTAPizza.Sympoll.pollmanagementservice.validator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * A global exception handler for the entire Poll Management service
 */
@ControllerAdvice
public class PollExceptionHandler {

    /**
     * Handles Illegal arguments exceptions only
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handlePollValidationException(IllegalArgumentException ex, WebRequest request) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
