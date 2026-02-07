package ru.gnidenko.securityservice.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gnidenko.securityservice.dto.AuthRequest;
import ru.gnidenko.securityservice.dto.AuthResponse;
import ru.gnidenko.securityservice.model.UserCredential;
import ru.gnidenko.securityservice.service.AuthService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authManager;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody AuthRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.register(request);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return authResponse;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        if (auth.isAuthenticated()){
            UserCredential userCredential = (UserCredential) auth.getPrincipal();
            return authService.login(userCredential);
        }
        throw new BadCredentialsException("Bad credentials");
    }
}
