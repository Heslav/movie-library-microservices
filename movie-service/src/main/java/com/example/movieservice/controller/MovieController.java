package com.example.movieservice.controller;

import com.example.movieservice.dto.MovieDto;
import com.example.movieservice.exceptions.CannotFindMovieException;
import com.example.movieservice.exceptions.ExternalServiceException;
import com.example.movieservice.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Integer id) {
        try {
            MovieDto movie = movieService.getMovieById(id);
            log.info("Movie found with id: {}", id);
            return new ResponseEntity<>(movie, HttpStatus.OK);
        } catch (CannotFindMovieException ex) {
            log.error("Movie not found with id: {}", id, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        try {
            List<MovieDto> movies = movieService.getAllMovies();
            log.info("Retrieved all movies");
            return new ResponseEntity<>(movies, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving movies: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/recommendation/{movieGenre}")
    public ResponseEntity<String> getMovieRecommendationFromGpt(@PathVariable String movieGenre) {
        try {
            String movieRecommendation = movieService.getMovieRecommendation(movieGenre);
            log.info("New movie recommendation from external service: {} ", movieRecommendation);
            return ResponseEntity.ok(movieRecommendation);
        } catch (ExternalServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving movie recommendation: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<MovieDto> addMovie(@RequestBody MovieDto movieDto) {
        try {
            MovieDto savedMovieDto = movieService.addMovie(movieDto);
            log.info("Added movie with title: {}", savedMovieDto.getTitle());
            return new ResponseEntity<>(savedMovieDto, HttpStatus.CREATED);
        } catch (CannotFindMovieException e) {
            log.error("Error adding movie: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/imdb/{movieTitle}/{userRating}")
    public ResponseEntity<MovieDto> addMovieImdb(@PathVariable String movieTitle, @PathVariable Integer userRating) {
        try {
            MovieDto movieDto = movieService.addMovieImdb(movieTitle, userRating);
            log.info("Added movie from IMDB with title: {}.", movieTitle);
            return ResponseEntity.ok(movieDto);
        } catch (ExternalServiceException e) {
            log.error("Error adding movie from IMDB: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MovieDto());
        }
    }

    @PutMapping
    public ResponseEntity<MovieDto> updateMovie(@RequestBody MovieDto movieDto) {
        try {
            MovieDto updatedMovieDto = movieService.updateMovie(movieDto);
            log.info("Updated movie with id: {}", movieDto.getId());
            return new ResponseEntity<>(updatedMovieDto, HttpStatus.OK);
        } catch (CannotFindMovieException ex) {
            log.error("Cannot update movie. Movie not found with id: {}", movieDto.getId(), ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MovieDto> deleteMovieById(@PathVariable Integer id) {
        try {
            MovieDto deletedMovieDto = movieService.deleteMovieById(id);
            log.info("Deleted movie with id: {}", id);
            return new ResponseEntity<>(deletedMovieDto, HttpStatus.OK);
        } catch (CannotFindMovieException ex) {
            log.error("Cannot delete movie. Movie not found with id: {}", id, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
