package ru.gnidenko.apigateway.filter;


import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import ru.gnidenko.apigateway.jwt.JwtValidator;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory {

    private final JwtValidator jwtValidator;

    public AuthFilter(JwtValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (((exchange, chain) -> {
            if (RouteValidator.IS_SECURED.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing auth header");
                }
                String auth = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (auth != null && auth.startsWith("Bearer ")) {
                    auth = auth.substring("Bearer ".length());
                }
                try {
                    jwtValidator.validate(auth);
                } catch (Exception e) {
                    throw new RuntimeException("Unauthorized");
              }
            }
            return chain.filter(exchange);
        }));
    }
}
