package ru.gnidenko.apigateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> OPEN_API_ENDPOINTS =
        List.of("/security-service/**");
    public static Predicate<ServerHttpRequest> IS_SECURED =
        serverHttpRequest -> OPEN_API_ENDPOINTS
            .stream()
            .noneMatch(uri->
                serverHttpRequest.getURI().toString()
                    .equals(uri));
}
