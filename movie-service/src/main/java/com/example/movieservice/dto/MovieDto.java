package com.example.movieservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDto {
    private Integer id;
    private String title;
    private int releaseYear;
    private int userRating;
}
