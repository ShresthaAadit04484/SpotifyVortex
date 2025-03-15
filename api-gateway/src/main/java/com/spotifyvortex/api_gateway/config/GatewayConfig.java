package com.spotifyvortex.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            // Alternative way to define routes programmatically
            .route("user-service-route", r -> r
                .path("/users/**")
                // .filters(f -> f
                    // .rewritePath("/api/users/(?<segment>.*)", "/users/${segment}"))
                    // .addRequestHeader("X-Gateway-Source", "api-gateway"))
                .uri("lb://user-service"))
            
           // You can add more routes for other services here
            .route("auth-service-route", r -> r
                .path("/api/auth/**")
                .uri("lb://auth-service"))
            .build();
    }
}
