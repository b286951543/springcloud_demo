package com.nacos.filter;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
        // ws://localhost:9999/ws2/v1/trainingSocket?courseId=2 只有带有 courseId=2 参数的才会被转发到指定uri
        return builder.routes()
                .route("netty2", p -> p.path("/ws2/**").and().query("courseId", "2")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb:ws://app-netty2")
//                        .order(-1)
                )
                .build();
    }
}
