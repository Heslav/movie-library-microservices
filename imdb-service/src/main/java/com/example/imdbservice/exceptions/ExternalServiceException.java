package com.example.imdbservice.exceptions;

public class ExternalServiceException extends RuntimeException{
    public ExternalServiceException(String message) {
        super(message);
    }
}
