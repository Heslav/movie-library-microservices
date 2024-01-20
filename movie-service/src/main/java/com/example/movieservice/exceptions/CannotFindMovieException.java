package com.example.movieservice.exceptions;

public class CannotFindMovieException extends RuntimeException {
    public CannotFindMovieException(String message){
        super(message);
    }
}
