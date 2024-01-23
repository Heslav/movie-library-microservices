package com.example.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscoveryServerRouteConfig {
    @Bean
    public RouteLocator discoveryServerRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/eureka/web")
                        .uri("http://localhost:8761"))
                .build();
    }

    @Bean
    public RouteLocator discoveryServerStaticRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/eureka/**")
                        .uri("http://localhost:8761"))
                .build();
    }
}
