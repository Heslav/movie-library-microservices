package com.example.recommendationservice.exceptions;

public class CannotGetMovieRecommendationException extends RuntimeException{
    public CannotGetMovieRecommendationException(String message){
        super(message);
    }
}
