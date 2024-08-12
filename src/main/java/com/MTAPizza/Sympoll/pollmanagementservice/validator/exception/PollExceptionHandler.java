package com.MTAPizza.Sympoll.pollmanagementservice.validator.exception;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.error.GeneralPollError;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.error.IllegalPollArgumentError;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.error.JsonParserError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    public ResponseEntity<IllegalPollArgumentError> handlePollValidationException(IllegalArgumentException ex, WebRequest request) {
        return new ResponseEntity<>(new IllegalPollArgumentError(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions when JSON is invalid or malformed
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<JsonParserError> handleInvalidJsonException(HttpMessageNotReadableException ex, WebRequest request) {
        return new ResponseEntity<>(new JsonParserError(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles unhandled exceptions only
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralPollError> handleGeneralException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(new GeneralPollError(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
