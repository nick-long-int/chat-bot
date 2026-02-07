package ru.gnidenko.securityservice.service.assymetric;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gnidenko.securityservice.model.UserCredential;
import ru.gnidenko.securityservice.model.UserRole;

import java.security.PrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtProducer {

    private final PrivateKey privateKey;

    public String createToken(UserCredential userCredential) {
        Map<String, Object> claims = new HashMap<>();
        Set<String> roles = userCredential.getRoles()
            .stream()
            .map(UserRole::getRole)
            .collect(Collectors.toSet());
        claims.put("roles", roles);

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userCredential.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
            .signWith(privateKey)
            .compact();
    }

}
