package com.MTAPizza.Sympoll.pollmanagementservice.exception;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.error.*;
import com.MTAPizza.Sympoll.pollmanagementservice.exception.access.denied.AccessDeniedException;
import com.MTAPizza.Sympoll.pollmanagementservice.exception.not.found.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * A global exception handler for the entire Poll Management service
 */
@ControllerAdvice
public class PollExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(PollExceptionHandler.class);

    /**
     * Handles Illegal arguments exceptions.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<IllegalArgumentResponse> handlePollValidationException(IllegalArgumentException ex, WebRequest request) {
        log.info("Encountered a poll or vote validation exception: {}", ex.getMessage());
        return new ResponseEntity<>(new IllegalArgumentResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles invalid or malformed JSON exceptions.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<JsonParserErrorResponse> handleInvalidJsonException(HttpMessageNotReadableException ex, WebRequest request) {
        log.info("Encountered a request without valid JSON format: {}", ex.getMessage());
        return new ResponseEntity<>(new JsonParserErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles resource not fount exceptions.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResourceNotFoundResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        log.info("Encountered a request without valid resource ID: {}", ex.getMessage());
        return new ResponseEntity<>(new ResourceNotFoundResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles access denied exceptions.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<AccessDeniedErrorResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        log.info("Encountered a request from a user without access to the requested action: {}", ex.getMessage());
        return new ResponseEntity<>(new AccessDeniedErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles unhandled exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralPollErrorResponse> handleGeneralException(Exception ex, WebRequest request) {
        log.info("Encountered a general poll or vote exception: {}", ex.getMessage());
        return new ResponseEntity<>(new GeneralPollErrorResponse(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
