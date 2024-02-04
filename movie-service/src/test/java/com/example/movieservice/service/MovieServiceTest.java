package com.example.movieservice.service;

import com.example.movieservice.dto.MovieDto;
import com.example.movieservice.exceptions.CannotAddMovieException;
import com.example.movieservice.exceptions.CannotFindMovieException;
import com.example.movieservice.exceptions.ExternalServiceException;
import com.example.movieservice.feign_clients.ImdbServiceFeignClient;
import com.example.movieservice.feign_clients.RecommendationServiceFeignClient;
import com.example.movieservice.mapper.MovieMapper;
import com.example.movieservice.model.Movie;
import com.example.movieservice.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {
    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ImdbServiceFeignClient imdbServiceFeignClient;

    @Mock
    private RecommendationServiceFeignClient recommendationServiceFeignClient;

    @Mock
    private MovieMapper movieMapper;

    @BeforeEach
    public void setUp() {
        movieService = new MovieService(movieRepository, imdbServiceFeignClient, recommendationServiceFeignClient, movieMapper);
    }
    @Test
    public void testGetMovieById() {
        Integer id = 1;
        String movieTitle = "Shrek";
        int releaseYear = 2002;
        int userRating = 10;
        MovieDto movieDto = new MovieDto(null, movieTitle, releaseYear, userRating);
        Movie movie = new Movie(1, movieTitle, releaseYear, userRating);

        when(movieRepository.findById(id)).thenReturn(Optional.of(movie));
        when(movieMapper.mapToMovieDto(movie)).thenReturn(movieDto);

        MovieDto actualMovieDto = movieService.getMovieById(id);

        assertEquals(movieDto, actualMovieDto);
    }
    @Test
    public void testGetAllMovies() {
        List<Movie> movies = List.of(new Movie(1, "Inception", 2010, 9));
        when(movieRepository.findAll()).thenReturn(movies);
        when(movieMapper.mapToMovieDto(any())).thenReturn(new MovieDto(1, "Inception", 2010, 9));

        List<MovieDto> result = movieService.getAllMovies();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    public void testGetMovieRecommendationSuccessful() {
        String movieGenre = "Action";
        when(recommendationServiceFeignClient.getMovieRecommendation(movieGenre)).thenReturn("Inception");

        String result = movieService.getMovieRecommendation(movieGenre);

        assertEquals("Inception", result);
    }

    @Test
    public void testGetMovieRecommendationExceptionThrown() {
        String movieGenre = "Action";
        when(recommendationServiceFeignClient.getMovieRecommendation(movieGenre))
                .thenThrow(new RuntimeException("Simulated exception"));

        assertThrows(ExternalServiceException.class, () -> movieService.getMovieRecommendation(movieGenre));
    }

    @Test
    public void testAddMovieSuccessful() {
        MovieDto movieDto = new MovieDto(null, "Inception", 2010, 9);
        Movie savedMovie = new Movie(1, "Inception", 2010, 9);
        when(movieMapper.mapToMovie(movieDto)).thenReturn(savedMovie);
        when(movieRepository.save(savedMovie)).thenReturn(savedMovie);
        when(movieMapper.mapToMovieDto(savedMovie)).thenReturn(new MovieDto(1, "Inception", 2010, 9));

        MovieDto result = movieService.addMovie(movieDto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Inception", result.getTitle());
        assertEquals(2010, result.getReleaseYear());
        assertEquals(9, result.getUserRating());
    }

    @Test
    public void testAddMovieExceptionThrown() {
        MovieDto movieDto = new MovieDto(null, "Inception", 2010, 9);
        when(movieMapper.mapToMovie(movieDto)).thenReturn(new Movie(1, "Inception", 2010, 9));
        when(movieRepository.save(any())).thenThrow(new RuntimeException("Simulated exception"));

        assertThrows(CannotAddMovieException.class, () -> movieService.addMovie(movieDto));
    }

    @Test
    public void testUpdateMovie() {
        MovieDto movieDto = new MovieDto(1, "Inception", 2010, 9);
        Movie existingMovie = new Movie(1, "OldTitle", 2000, 8);
        when(movieRepository.findById(1)).thenReturn(Optional.of(existingMovie));

        MovieDto result = movieService.updateMovie(movieDto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Inception", result.getTitle());
        assertEquals(2010, result.getReleaseYear());
        assertEquals(9, result.getUserRating());
    }
    @Test
    public void testUpdateMovieExceptionThrown() {
        MovieDto movieDto = new MovieDto(1, "Inception", 2010, 9);
        when(movieRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CannotFindMovieException.class, () -> movieService.updateMovie(movieDto));
    }

    @Test
    public void testDeleteMovieByIdSuccessful() {
        Integer id = 1;
        Movie existingMovie = new Movie(1, "Inception", 2010, 9);
        when(movieRepository.findById(id)).thenReturn(Optional.of(existingMovie));
        when(movieMapper.mapToMovieDto(existingMovie)).thenReturn(new MovieDto(1, "Inception", 2010, 9));

        MovieDto result = movieService.deleteMovieById(id);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Inception", result.getTitle());
        assertEquals(2010, result.getReleaseYear());
        assertEquals(9, result.getUserRating());
        verify(movieRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteMovieByIdExceptionThrown() {
        Integer id = 1;
        when(movieRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CannotFindMovieException.class, () -> movieService.deleteMovieById(id));
        verify(movieRepository, never()).deleteById(id);
    }
}
