package com.example.imdbservice.controller;

import com.example.imdbservice.model.MovieImdb;
import com.example.imdbservice.service.MovieImdbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/imdb")
public class MovieImdbController {

    private final MovieImdbService movieImdbService;

    @PostMapping("/{movieTitle}/{userRating}")
    public ResponseEntity<MovieImdb> addMovieFromImdb(@PathVariable String movieTitle, @PathVariable Integer userRating) {
        try {
            log.info("New movie details from imdb api retrieved successfully in external service and added to the database.");
            MovieImdb movieImdb = movieImdbService.getMovieDetailsFromImdb(movieTitle, userRating);
            return ResponseEntity.ok(movieImdb);
        } catch (Exception e) {
            log.error("Error adding movie from IMDB: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/{movieTitle}")
    public ResponseEntity<MovieImdb> getMovieDetails(@PathVariable String movieTitle, @RequestParam(required = false) Integer userRating) {
        try {
            log.info("New movie details from imdb api retrieved successfully.");
            MovieImdb movieImdb = movieImdbService.getMovieDetailsFromImdb(movieTitle, userRating);
            return ResponseEntity.ok(movieImdb);
        } catch (Exception e) {
            log.error("Error getting movie details from IMDB: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
