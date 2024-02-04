package com.example.imdbservice.service;

import com.example.imdbservice.exceptions.CannotGetHttpResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class HttpResponseHandler {
    private final HttpClient httpClient;

    public String getHttpResponse(String url, String apiKey) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .header("X-RapidAPI-Key", apiKey)
                    .header("X-RapidAPI-Host", "imdb8.p.rapidapi.com")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new CannotGetHttpResponseException("Failed to get a successful response. Status code: "
                        + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new CannotGetHttpResponseException("Error while sending HTTP request: " + e.getMessage());
        }
    }
}
