package com.MTAPizza.Sympoll.pollmanagementservice.exception.not.found;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
