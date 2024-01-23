package com.example.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MovieServiceRouteConfig {
    @Bean
    public RouteLocator movieServiceRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/api/movies/**")
                        .uri("lb://movie-service"))
                .build();
    }
}
