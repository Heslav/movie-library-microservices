package integration.tests;

import com.example.movieservice.MovieServiceApplication;
import com.example.movieservice.dto.MovieDto;
import com.example.movieservice.exceptions.CannotFindMovieException;
import com.example.movieservice.exceptions.ExternalServiceException;
import com.example.movieservice.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = MovieServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MovieServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;


    @Test
    public void testAddMovie() throws Exception {
        MovieDto movieDto = new MovieDto();
        movieDto.setId(1);
        movieDto.setTitle("Inception");

        Mockito.when(movieService.addMovie(any(MovieDto.class))).thenReturn(movieDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // You can provide a JSON payload if needed
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Inception"));
    }

    @Test
    public void testAddMovieImdb() throws Exception {
        String movieTitle = "The Dark Knight";
        int userRating = 5;

        MovieDto movieDto = new MovieDto();
        movieDto.setId(1);
        movieDto.setTitle(movieTitle);

        Mockito.when(movieService.addMovieImdb(movieTitle, userRating)).thenReturn(movieDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/movies/imdb/{movieTitle}/{userRating}", movieTitle, userRating)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value(movieTitle));
    }

    @Test
    public void testUpdateMovie() throws Exception {
        int movieId = 1;
        MovieDto movieDto = new MovieDto();
        movieDto.setId(movieId);
        movieDto.setTitle("The Matrix");

        Mockito.when(movieService.updateMovie(any(MovieDto.class))).thenReturn(movieDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(movieDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movieId))
                .andExpect(jsonPath("$.title").value("The Matrix"));
    }

    @Test
    public void testGetMovieById() throws Exception {
        int movieId = 1;
        MovieDto movieDto = new MovieDto();
        movieDto.setId(movieId);

        Mockito.when(movieService.getMovieById(movieId)).thenReturn(movieDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/{id}", movieId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movieId));
    }

    @Test
    public void testGetAllMovies() throws Exception {
        MovieDto movieDto = new MovieDto();
        movieDto.setId(1);

        Mockito.when(movieService.getAllMovies()).thenReturn(Collections.singletonList(movieDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    public void testGetMovieRecommendationFromGpt() throws Exception {
        String movieGenre = "Action";
        String recommendation = "Die Hard";

        Mockito.when(movieService.getMovieRecommendation(movieGenre)).thenReturn(recommendation);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/recommendation/{movieGenre}", movieGenre)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(recommendation));
    }

    @Test
    public void testDeleteMovieById() throws Exception {
        int movieId = 1;
        MovieDto movieDto = new MovieDto();
        movieDto.setId(movieId);
        movieDto.setTitle("Terminator");

        Mockito.when(movieService.deleteMovieById(movieId)).thenReturn(movieDto);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/movies/{id}", movieId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movieId))
                .andExpect(jsonPath("$.title").value("Terminator"));
    }

    @Test
    public void testGetMovieByIdNotFound() throws Exception {
        int movieId = 1;

        Mockito.when(movieService.getMovieById(movieId)).thenThrow(new CannotFindMovieException("Movie not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/{id}", movieId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetMovieRecommendationFromGptFailure() throws Exception {
        String movieGenre = "Action";

        Mockito.when(movieService.getMovieRecommendation(movieGenre)).thenThrow(new ExternalServiceException("External service error"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/recommendation/{movieGenre}", movieGenre)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testAddMovieImdbFailure() throws Exception {
        String movieTitle = "The Dark Knight";
        int userRating = 5;

        Mockito.when(movieService.addMovieImdb(movieTitle, userRating)).thenThrow(new ExternalServiceException("External service error"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/movies/imdb/{movieTitle}/{userRating}", movieTitle, userRating)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testUpdateMovieNotFound() throws Exception {
        int movieId = 1;
        MovieDto movieDto = new MovieDto();
        movieDto.setId(movieId);

        Mockito.when(movieService.updateMovie(any(MovieDto.class))).thenThrow(new CannotFindMovieException("Movie not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(movieDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteMovieByIdNotFound() throws Exception {
        int movieId = 1;

        Mockito.when(movieService.deleteMovieById(movieId)).thenThrow(new CannotFindMovieException("Movie not found"));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/movies/{id}", movieId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddMovieFailure() throws Exception {
        Mockito.when(movieService.addMovie(any(MovieDto.class)))
                .thenThrow(new CannotFindMovieException("Test exception"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testGetAllMoviesFailure() throws Exception {
        Mockito.when(movieService.getAllMovies()).thenThrow(new ExternalServiceException("Error retrieving movies,"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
