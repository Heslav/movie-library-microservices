package com.example.movieservice.service;

import com.example.movieservice.feign_clients.ImdbServiceFeignClient;
import com.example.movieservice.feign_clients.RecommendationServiceFeignClient;
import com.example.movieservice.dto.MovieDto;
import com.example.movieservice.exceptions.CannotAddMovieException;
import com.example.movieservice.exceptions.CannotFindMovieException;
import com.example.movieservice.exceptions.ExternalServiceException;
import com.example.movieservice.mapper.MovieMapper;
import com.example.movieservice.model.Movie;
import com.example.movieservice.repository.MovieRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final ImdbServiceFeignClient imdbServiceFeignClient;
    private final RecommendationServiceFeignClient recommendationServiceFeignClient;
    private final MovieMapper movieMapper;

    //GET:
    public MovieDto getMovieById(Integer id) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        return optionalMovie.map(movieMapper::mapToMovieDto)
                .orElseThrow(() -> new CannotFindMovieException("Movie with id: " + id + " was not found."));
    }

    public List<MovieDto> getAllMovies() {
        try {
            List<Movie> movies = movieRepository.findAll();
            return movies.stream().map(movieMapper::mapToMovieDto).toList();
        } catch (Exception e) {
            log.error("Error retrieving movies: {}", e.getMessage());
            throw new CannotFindMovieException("Error retrieving movies.");
        }
    }

    @CircuitBreaker(name = "movieRecommendation", fallbackMethod = "fallbackForGetMovieRecommendation")
    public String getMovieRecommendation(String movieGenre) {
        try {
            return recommendationServiceFeignClient.getMovieRecommendation(movieGenre);
        } catch (Exception e) {
            throw new ExternalServiceException("Error occurred when getting movie recommendation.");
        }
    }

    //POST:
    public MovieDto addMovie(MovieDto movieDto) {
        try {
            Movie movie = movieMapper.mapToMovie(movieDto);
            Movie savedMovie = movieRepository.save(movie);
            return movieMapper.mapToMovieDto(savedMovie);
        } catch (Exception e) {
            log.error("Error adding movie: {}", e.getMessage());
            throw new CannotAddMovieException("Something went wrong while adding your movie." +
                    " Please make sure all fields are correct.");
        }
    }

    @CircuitBreaker(name = "movieImdb", fallbackMethod = "fallbackForAddMovieImdb")
    public MovieDto addMovieImdb(String movieTitle, Integer userRating) {
        try {
            MovieDto newMovie = imdbServiceFeignClient.addMovieImdb(movieTitle, userRating);
            movieRepository.save(movieMapper.mapToMovie(newMovie));
            return newMovie;
        } catch (Exception e) {
            throw new ExternalServiceException("Error occurred when adding movie to IMDB.");
        }
    }

    //PUT:
    public MovieDto updateMovie(MovieDto movieDto) {
        Movie movie = movieRepository.findById(movieDto.getId()).orElseThrow(() ->
                new CannotFindMovieException("Movie with id: " + movieDto.getId() + " not found."));
        movieMapper.mapMovieDtoToMovie(movieDto, movie);
        movieRepository.save(movie);
        return movieDto;
    }

    //DELETE:
    public MovieDto deleteMovieById(Integer id) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if (optionalMovie.isPresent()) {
            Movie movie = optionalMovie.get();
            movieRepository.deleteById(id);
            return movieMapper.mapToMovieDto(movie);
        } else {
            throw new CannotFindMovieException("Movie with id: " + id + " was not found.");
        }
    }

    //FALLBACKS:
    public String fallbackForGetMovieRecommendation(String movieGenre, Exception e) {
        log.error("Fallback for getMovieRecommendation. Cause: ", e);
        return "We are sorry, our service is down. Consider watching: Killers of the flower moon.";
    }

    public MovieDto fallbackForAddMovieImdb(String movieTitle, Integer userRating, Exception e) {
        log.error("Fallback for addMovieImdb. Cause: ", e);
        return new MovieDto(666, "The Human Centipede", 1410, 10);
    }
}
