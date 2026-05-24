package com.smartshop.gateway;

import static org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions.circuitBreaker;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

import java.net.URI;

import org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class GatewayConfig {

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute() {
        return route("order-service")
            .route(path("/api/orders/**"), http())
            .filter(LoadBalancerFilterFunctions.lb("ORDER-SERVICE"))
            .filter(circuitBreaker("orderCircuitBreaker",
                URI.create("forward:/fallback/order")))
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userServiceRoute() {
        return route("user-service")
            .route(path("/api/users/**"), http())
            .filter(LoadBalancerFilterFunctions.lb("USER-SERVICE"))
            .filter(circuitBreaker("userCircuitBreaker",
                URI.create("forward:/fallback/user")))
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> productServiceRoute() {
        return route("product-service")
            .route(path("/api/products/**"), http())
            .filter(LoadBalancerFilterFunctions.lb("PRODUCT-SERVICE"))
            .filter(circuitBreaker("productCircuitBreaker",
                URI.create("forward:/fallback/product")))
            .build();
    }
}