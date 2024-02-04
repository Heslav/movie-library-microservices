package com.example.imdbservice.service;

import com.example.imdbservice.exceptions.ExternalServiceException;
import com.example.imdbservice.model.MovieImdb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieImdbServiceTest {


    @InjectMocks
    private MovieImdbService movieImdbService;

    @Mock
    private JsonResponseMapper jsonResponseMapper;

    @Mock
    private HttpResponseHandler httpResponseHandler;

    @BeforeEach
    public void setUp() {
        movieImdbService = new MovieImdbService(jsonResponseMapper, httpResponseHandler);
    }

    @Test
    public void testGetMovieDetailsFromImdb() {
        String movieTitle = "Test Movie";
        Integer userRating = 5;
        String apiResponse = "{\"d\":[{\"l\":\"The Movie\",\"y\":2022}]}";
        MovieImdb expectedMovie = new MovieImdb("The Movie", 2022, userRating);

        when(httpResponseHandler.getHttpResponse(any(), any())).thenReturn(apiResponse);
        when(jsonResponseMapper.mapJsonResponseFromImdb(any(), any())).thenReturn(expectedMovie);

        MovieImdb actualMovie = movieImdbService.getMovieDetailsFromImdb(movieTitle, userRating);

        assertEquals(expectedMovie, actualMovie);
    }

    @Test
    public void testGetMovieDetailsFromImdbWithExceptionThrown() {
        String movieTitle = "Test Movie";
        Integer userRating = 5;
        String errorMessage = "Error fetching movie details from IMDB.";

        when(httpResponseHandler.getHttpResponse(any(), any()))
                .thenThrow(new RuntimeException("Unexpected error occurred."));

        Exception exception = assertThrows(ExternalServiceException.class, () -> {
            movieImdbService.getMovieDetailsFromImdb(movieTitle, userRating);
        });

        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(errorMessage), "Expected exception message to contain '" + errorMessage + "'");
    }
}
