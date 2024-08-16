package com.MTAPizza.Sympoll.pollmanagementservice.validator.exception;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.error.GeneralPollError;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.error.IllegalArgumentError;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.error.JsonParserError;
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
     * Handles Illegal arguments exceptions only
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<IllegalArgumentError> handlePollValidationException(IllegalArgumentException ex, WebRequest request) {
        log.info("Encountered a poll or vote validation exception: {}", ex.getMessage());
        return new ResponseEntity<>(new IllegalArgumentError(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions when JSON is invalid or malformed
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<JsonParserError> handleInvalidJsonException(HttpMessageNotReadableException ex, WebRequest request) {
        log.info("Encountered a request body without valid JSON format: {}", ex.getMessage());
        return new ResponseEntity<>(new JsonParserError(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles unhandled exceptions only
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralPollError> handleGeneralException(Exception ex, WebRequest request) {
        log.info("Encountered a general poll or vote exception: {}", ex.getMessage());
        return new ResponseEntity<>(new GeneralPollError(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
