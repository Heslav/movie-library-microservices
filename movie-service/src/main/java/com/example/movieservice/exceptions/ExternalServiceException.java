package com.example.movieservice.exceptions;

public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String message){
        super(message);
    }
}
