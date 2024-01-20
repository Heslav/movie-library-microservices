package com.example.movieservice.mapper;

import com.example.movieservice.dto.MovieDto;
import com.example.movieservice.model.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface MovieMapper {
    @Mapping(target = "id", ignore = true)
    Movie mapToMovie(MovieDto movieDto);

    MovieDto mapToMovieDto(Movie movie);

    void mapMovieDtoToMovie(MovieDto movieDto, @MappingTarget Movie movie);
}
