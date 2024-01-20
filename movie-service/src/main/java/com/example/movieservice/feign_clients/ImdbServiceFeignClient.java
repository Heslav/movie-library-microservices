package com.example.movieservice.feign_clients;

import com.example.movieservice.dto.MovieDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "imdb-service")
public interface ImdbServiceFeignClient {
    @PostMapping("/api/imdb/{movieTitle}/{userRating}")
    MovieDto addMovieImdb(@PathVariable String movieTitle, @PathVariable Integer userRating);
}
