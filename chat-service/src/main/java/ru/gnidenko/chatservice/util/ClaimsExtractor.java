package ru.gnidenko.chatservice.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class ClaimsExtractor {
    public Long getUserIdFromClaims() {
        Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        Jwt principal = (Jwt) authentication.getPrincipal();
        return principal.getClaim("userId");
    }
}
