package com.example.imdbservice.exceptions;

public class CannotGetHttpResponseException extends RuntimeException {
    public CannotGetHttpResponseException(String message){
        super(message);
    }
}
