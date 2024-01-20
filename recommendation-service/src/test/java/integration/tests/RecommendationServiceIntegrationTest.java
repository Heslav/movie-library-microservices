package integration.tests;

import com.example.recommendationservice.RecommendationServiceApplication;
import com.example.recommendationservice.exceptions.CannotGetMovieRecommendationException;
import com.example.recommendationservice.model.MovieRecommendation;
import com.example.recommendationservice.service.MovieRecommendationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = RecommendationServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RecommendationServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieRecommendationService movieRecommendationService;

    @Test
    public void testGetMovieRecommendationSuccess() throws Exception {
        String movieGenre = "Action";
        MovieRecommendation recommendation = new MovieRecommendation("Die Hard");

        Mockito.when(movieRecommendationService.getMovieRecommendations(movieGenre))
                .thenReturn(ResponseEntity.ok(recommendation));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/recommendation/{movieGenre}", movieGenre)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieRecommendation").value("Die Hard"));
    }

    @Test
    public void testGetMovieRecommendationFailure() throws Exception {
        String movieGenre = "Action";

        Mockito.when(movieRecommendationService.getMovieRecommendations(movieGenre))
                .thenThrow(new CannotGetMovieRecommendationException("Error fetching movie recommendation"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/recommendation/{movieGenre}", movieGenre)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
