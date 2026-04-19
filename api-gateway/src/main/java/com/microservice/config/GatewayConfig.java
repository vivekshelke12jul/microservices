package com.microservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Public auth routes — NO JWT filter
                .route("auth-service-public", r -> r
                        .path("/auth/**")
                        .uri("lb://AUTH-SERVICE"))

                // Protected routes — JWT filter applied
                .route("article-service", r -> r
                        .path("/articles/**")
                        .filters(f -> f.filter(jwtAuthFilter))
                        .uri("lb://ARTICLE-SERVICE"))
                .build();
    }
}