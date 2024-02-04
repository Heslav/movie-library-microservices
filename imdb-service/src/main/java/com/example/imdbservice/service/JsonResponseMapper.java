package com.example.imdbservice.service;

import com.example.imdbservice.exceptions.CannotParseJsonException;
import com.example.imdbservice.model.MovieImdb;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JsonResponseMapper {

    private final ObjectMapper objectMapper;

    public MovieImdb mapJsonResponseFromImdb(String response, Integer userRating) {

        JsonNode firstMovieFromResponse;
        try {
            firstMovieFromResponse = objectMapper.readTree(response).get("d").get(0);
        } catch (JsonProcessingException e) {
            throw new CannotParseJsonException("Cannot properly map Json response from Imdb API. Try again with different title.");
        }
        String movieTitle = firstMovieFromResponse.get("l").asText();
        int releaseYear = firstMovieFromResponse.get("y").asInt();
        return new MovieImdb(movieTitle, releaseYear, userRating);
    }
}
