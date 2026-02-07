package ru.gnidenko.securityservice.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gnidenko.securityservice.dto.AuthRequest;
import ru.gnidenko.securityservice.dto.AuthResponse;
import ru.gnidenko.securityservice.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody AuthRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.register(request);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return authResponse;
    }
}
