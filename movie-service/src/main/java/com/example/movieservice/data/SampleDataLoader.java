package com.example.movieservice.data;

import com.example.movieservice.model.Movie;
import com.example.movieservice.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SampleDataLoader implements CommandLineRunner {

    private final MovieRepository movieRepository;

    @Override
    public void run(String... args) throws Exception {
        movieRepository.save(new Movie(1, "Nightcrawler", 2014, 9));
        movieRepository.save(new Movie(2, "American Psycho", 2000, 8));
        movieRepository.save(new Movie(3, "Taxi Driver", 1976, 10));
    }
}
