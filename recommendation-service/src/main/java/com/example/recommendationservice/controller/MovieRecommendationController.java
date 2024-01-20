package com.example.recommendationservice.controller;

import com.example.recommendationservice.exceptions.CannotGetMovieRecommendationException;
import com.example.recommendationservice.model.MovieRecommendation;
import com.example.recommendationservice.service.MovieRecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/recommendation")
public class MovieRecommendationController {
    private final MovieRecommendationService movieRecommendationService;

    @GetMapping("/{movieGenre}")
    public ResponseEntity<MovieRecommendation> getMovieRecommendation(@PathVariable String movieGenre) {
        try {
            log.info("Movie recommendation retrieved successfully.");
            return movieRecommendationService.getMovieRecommendations(movieGenre);
        } catch (CannotGetMovieRecommendationException ex) {
            log.error("Error occurred when getting movie recommendation.", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
