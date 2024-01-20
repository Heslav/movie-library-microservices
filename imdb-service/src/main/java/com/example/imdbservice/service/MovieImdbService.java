package com.example.imdbservice.service;

import com.example.imdbservice.exceptions.ExternalServiceException;
import com.example.imdbservice.model.MovieImdb;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieImdbService {
    @Value("${IMDB.API.KEY}")
    private String imdbApiKey;
    @Value("${IMDB.API.URL}")
    private String imdbApiUrl;
    private final JsonResponseMapper jsonResponseMapper;
    private final HttpResponseHandler httpResponseHandler;
    public MovieImdb getMovieDetailsFromImdb(String movieTitle, Integer userRating) {
        try {
            String url = imdbApiUrl + movieTitle.replace(" ", "");
            String response = httpResponseHandler.getHttpResponse(url, imdbApiKey);
            return jsonResponseMapper.mapJsonResponseFromImdb(response, userRating);
        } catch (Exception e) {
            log.error("Error fetching movie details from IMDB: {}", e.getMessage());
            throw new ExternalServiceException("Error fetching movie details from IMDB.");
        }
    }
}