package com.example.imdbservice.exceptions;

public class CannotGetMovieDetailsException extends RuntimeException {
    public CannotGetMovieDetailsException(String message) {
        super(message);
    }
}
