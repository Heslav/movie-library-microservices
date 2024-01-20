package com.example.imdbservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieImdb {
    private String title;
    private int releaseYear;
    private Integer userRating;
}
