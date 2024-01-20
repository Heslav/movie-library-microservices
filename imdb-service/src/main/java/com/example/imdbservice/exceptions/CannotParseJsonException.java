package com.example.imdbservice.exceptions;

public class CannotParseJsonException extends RuntimeException {
    public CannotParseJsonException(String message) {
        super(message);
    }
}
