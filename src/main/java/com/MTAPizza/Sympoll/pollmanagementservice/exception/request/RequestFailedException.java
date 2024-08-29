package com.MTAPizza.Sympoll.pollmanagementservice.exception.request;

public class RequestFailedException extends RuntimeException {
    public RequestFailedException(String message) {
        super(message);
    }
}
