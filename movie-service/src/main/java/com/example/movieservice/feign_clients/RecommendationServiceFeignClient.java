package com.example.movieservice.feign_clients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "recommendation-service")
public interface RecommendationServiceFeignClient {
    @GetMapping("/api/recommendation/{movieGenre}")
    String getMovieRecommendation(@PathVariable String movieGenre);
}
