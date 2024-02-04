package com.example.recommendationservice.service;

import com.example.recommendationservice.exceptions.CannotGetMovieRecommendationException;
import com.example.recommendationservice.model.MovieRecommendation;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MovieRecommendationServiceTest {

    @Test
    void getMovieRecommendations_Success() {
        OpenAiService openAiService = mock(OpenAiService.class);
        String apiKey = "testApiKey";
        String model = "testModel";
        String commandForGpt = "generateMovieRecommendationForGenre";
        String movieGenre = "action";


        when(openAiService.createChatCompletion(ArgumentMatchers.any(ChatCompletionRequest.class)))
                .thenReturn(createMockChatCompletionResult("Recommended action movies"));

        MovieRecommendationService movieRecommendationService = new MovieRecommendationService();
        movieRecommendationService.gptApiKey = apiKey;
        movieRecommendationService.model = model;
        movieRecommendationService.commandForGpt = commandForGpt;

        try {
            ResponseEntity<MovieRecommendation> result = movieRecommendationService.getMovieRecommendations(movieGenre);
            assertEquals("Recommended action movies", result.getBody().getMovieRecommendation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    void getMovieRecommendations_Failure() {
        OpenAiService openAiService = mock(OpenAiService.class);
        String apiKey = "invalidApiKey";
        String model = "testModel";
        String commandForGpt = "generateMovieRecommendationForGenre";
        String movieGenre = "action";

        when(openAiService.createChatCompletion(ArgumentMatchers.any(ChatCompletionRequest.class)))
                .thenThrow(new RuntimeException("API Key is invalid"));

        MovieRecommendationService movieRecommendationService = new MovieRecommendationService();
        movieRecommendationService.gptApiKey = apiKey;
        movieRecommendationService.model = model;
        movieRecommendationService.commandForGpt = commandForGpt;

        try {
            movieRecommendationService.getMovieRecommendations(movieGenre);
        } catch (CannotGetMovieRecommendationException ex) {
            assertEquals("Something went wrong during retrieving movie recommendation from open ai api. " +
                    "Make sure you API KEY is valid.", ex.getMessage());
        }
    }

    private ChatCompletionResult createMockChatCompletionResult(String content) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(content);

        ChatCompletionChoice chatCompletionChoice = new ChatCompletionChoice();
        chatCompletionChoice.setMessage(chatMessage);

        ChatCompletionResult chatCompletionResult = new ChatCompletionResult();
        chatCompletionResult.setChoices(List.of(chatCompletionChoice));

        return chatCompletionResult;
    }
}
