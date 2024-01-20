package com.example.movieservice.exceptions;

public class CannotAddMovieException extends RuntimeException {
    public CannotAddMovieException(String message){
        super(message);
    }
}
