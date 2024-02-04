package com.example.imdbservice.service;

import com.example.imdbservice.exceptions.CannotGetHttpResponseException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class HttpResponseHandlerTest {

    @Test
    void testGetHttpSuccessfulResponse() throws IOException, InterruptedException {
        HttpClient httpClientMock = Mockito.mock(HttpClient.class);
        HttpResponse<String> successfulResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(successfulResponse.statusCode()).thenReturn(200);
        Mockito.when(successfulResponse.body()).thenReturn("Success");

        Mockito.when(httpClientMock.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(successfulResponse);

        HttpResponseHandler httpResponseHandler = new HttpResponseHandler(httpClientMock);

        String result = httpResponseHandler.getHttpResponse("https://example.com/api", "your-api-key");

        assertEquals("Success", result);
    }

    @Test
    void testGetHttpFailedResponse() throws IOException, InterruptedException {
        HttpClient httpClientMock = Mockito.mock(HttpClient.class);
        HttpResponse<String> failedResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(failedResponse.statusCode()).thenReturn(404);
        Mockito.when(failedResponse.body()).thenReturn("Not Found");

        Mockito.when(httpClientMock.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(failedResponse);

        HttpResponseHandler httpResponseHandler = new HttpResponseHandler(httpClientMock);

        assertThrows(CannotGetHttpResponseException.class,
                () -> httpResponseHandler.getHttpResponse("https://example.com/api", "your-api-key"));
    }

    @Test
    void testGetHttpResponseWithExceptionThrown() throws IOException, InterruptedException {
        HttpClient httpClientMock = Mockito.mock(HttpClient.class);
        Mockito.when(httpClientMock.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenThrow(IOException.class);

        HttpResponseHandler httpResponseHandler = new HttpResponseHandler(httpClientMock);

        assertThrows(CannotGetHttpResponseException.class,
                () -> httpResponseHandler.getHttpResponse("https://example.com/api", "your-api-key"));
    }
}