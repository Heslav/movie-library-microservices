package com.example.imdbservice.service;

import com.example.imdbservice.exceptions.CannotParseJsonException;
import com.example.imdbservice.model.MovieImdb;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

class JsonResponseMapperTest {

    @Test
    void testMapJsonSuccessfulResponseFromImdb() throws JsonProcessingException {
        ObjectMapper objectMapperMock = Mockito.mock(ObjectMapper.class);
        JsonNode jsonNodeMock = Mockito.mock(JsonNode.class);

        Mockito.when(jsonNodeMock.get("l")).thenReturn(jsonNodeMock);
        Mockito.when(jsonNodeMock.asText()).thenReturn("Movie Title");

        Mockito.when(jsonNodeMock.get("y")).thenReturn(jsonNodeMock);
        Mockito.when(jsonNodeMock.asInt()).thenReturn(2000);

        Mockito.when(objectMapperMock.readTree(anyString())).thenReturn(Mockito.mock(JsonNode.class));
        Mockito.when(objectMapperMock.readTree(anyString()).get("d")).thenReturn(Mockito.mock(JsonNode.class));
        Mockito.when(objectMapperMock.readTree(anyString()).get("d").get(0)).thenReturn(jsonNodeMock);

        JsonResponseMapper jsonResponseMapper = new JsonResponseMapper(objectMapperMock);

        MovieImdb result = jsonResponseMapper.mapJsonResponseFromImdb("{\"d\":[{\"l\":\"Movie Title\",\"y\":2000}]}", 5);

        assertEquals("Movie Title", result.getTitle());
        assertEquals(2000, result.getReleaseYear());
        assertEquals(5, result.getUserRating());
    }

    @Test
    void testMapJsonResponseFromImdbWithJsonProcessingException() throws Exception {
        ObjectMapper objectMapperMock = Mockito.mock(ObjectMapper.class);
        Mockito.when(objectMapperMock.readTree(anyString())).thenThrow(JsonProcessingException.class);

        JsonResponseMapper jsonResponseMapper = new JsonResponseMapper(objectMapperMock);

        assertThrows(CannotParseJsonException.class,
                () -> jsonResponseMapper.mapJsonResponseFromImdb("Invalid JSON", 5));
    }
}
