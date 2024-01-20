package com.example.movieservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "movie_title")
    private String title;
    @Column(name = "release_year")
    private int releaseYear;
    @Column(name = "user_rating")
    @Range(min = 0, max = 10)
    private int userRating;
}
