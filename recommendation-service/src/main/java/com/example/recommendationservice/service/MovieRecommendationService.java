package com.example.recommendationservice.service;

import com.example.recommendationservice.exceptions.CannotGetMovieRecommendationException;
import com.example.recommendationservice.model.MovieRecommendation;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieRecommendationService {
    @Value("${GPT.API.KEY}")
    private String gptApiKey;
    @Value("${GPT.API.MODEL}")
    private String model;
    @Value("${GPT.API.COMMAND}")
    private String commandForGpt;

    public ResponseEntity<MovieRecommendation> getMovieRecommendations(String movieGenre) {
        try {
            OpenAiService openAiService = new OpenAiService(this.gptApiKey);
            String movieRecommendationFromGpt = openAiService.createChatCompletion(
                            ChatCompletionRequest.builder()
                                    .messages(List.of(new ChatMessage("user", this.commandForGpt + movieGenre)))
                                    .model(this.model)
                                    .build()
                    ).getChoices().stream()
                    .map(ChatCompletionChoice::getMessage)
                    .map(ChatMessage::getContent)
                    .collect(Collectors.joining());
            MovieRecommendation movieRecommendation = new MovieRecommendation();
            movieRecommendation.setMovieRecommendation(movieRecommendationFromGpt);
            return ResponseEntity.ok(movieRecommendation);
        } catch (Exception ex) {
            throw new CannotGetMovieRecommendationException("Something went wrong during retrieving movie " +
                    "recommendation from open ai api. Make sure you API KEY is valid.");
        }
    }
}
