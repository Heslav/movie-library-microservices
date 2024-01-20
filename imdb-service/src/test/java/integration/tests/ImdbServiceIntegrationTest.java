package integration.tests;

import com.example.imdbservice.ImdbServiceApplication;
import com.example.imdbservice.exceptions.CannotGetMovieDetailsException;
import com.example.imdbservice.exceptions.ExternalServiceException;
import com.example.imdbservice.model.MovieImdb;
import com.example.imdbservice.service.MovieImdbService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = ImdbServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ImdbServiceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieImdbService movieImdbService;

    @Test
    void testAddMovieFromImdb() throws Exception {
        String movieTitle = "The Shawshank Redemption";
        Integer userRating = 9;
        MovieImdb movieImdb = new MovieImdb(movieTitle, 1994, userRating);
        given(movieImdbService.getMovieDetailsFromImdb(movieTitle, userRating)).willReturn(movieImdb);

        mockMvc.perform(post("/api/imdb/{movieTitle}/{userRating}", movieTitle, userRating))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(movieTitle))
                .andExpect(jsonPath("$.releaseYear").value(1994))
                .andExpect(jsonPath("$.userRating").value(userRating));
    }

    @Test
    void testAddMovieFromImdbThrowsException() throws Exception {
        String movieTitle = "Unknown";
        Integer userRating = 10;
        given(movieImdbService.getMovieDetailsFromImdb(movieTitle, userRating))
                .willThrow(new ExternalServiceException("External API error"));

        mockMvc.perform(post("/api/imdb/{movieTitle}/{userRating}", movieTitle, userRating))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetMovieDetailsFromImdb() throws Exception {
        String movieTitle = "The Shawshank Redemption";
        MovieImdb movieImdb = new MovieImdb(movieTitle, 1994, 9);
        given(movieImdbService.getMovieDetailsFromImdb(anyString(), any())).willReturn(movieImdb);

        mockMvc.perform(get("/api/imdb/{movieTitle}", movieTitle))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(movieTitle))
                .andExpect(jsonPath("$.releaseYear").value(1994))
                .andExpect(jsonPath("$.userRating").value(9));
    }

    @Test
    void testGetMovieDetailsFromImdbThrowsException() throws Exception {
        String movieTitle = "Unknown";
        given(movieImdbService.getMovieDetailsFromImdb(anyString(), any()))
                .willThrow(new CannotGetMovieDetailsException("Cannot get movie details"));

        mockMvc.perform(get("/api/imdb/{movieTitle}", movieTitle))
                .andExpect(status().isInternalServerError());
    }
}
