package ru.gnidenko.securityservice.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.gnidenko.securityservice.dto.AuthRequest;
import ru.gnidenko.securityservice.dto.AuthResponse;
import ru.gnidenko.securityservice.exception.DataValidationException;
import ru.gnidenko.securityservice.mapper.UserCredentialMapper;
import ru.gnidenko.securityservice.model.UserCredential;
import ru.gnidenko.securityservice.repo.UserCredentialRepo;
import ru.gnidenko.securityservice.service.assymetric.JwtProducer;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserCredentialRepo repo;
    private final UserCredentialMapper mapper;
    private final JwtProducer jwtProducer;
    private final PasswordEncoder passwordEncoder;

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED
    )
    public AuthResponse register(AuthRequest request) {
        if (repo.findByUsername(request.getUsername()).isPresent()) {
            throw new DataValidationException("Username is already in use");
        }
        if (repo.findByEmail(request.getEmail()).isPresent()) {
            throw new DataValidationException("Email is already in use");
        }
        UserCredential userCredential = mapper.toUserCredential(request);
        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        userCredential.setCreatedAt(LocalDateTime.now());
        userCredential.setUpdatedAt(LocalDateTime.now());

        AuthResponse response = mapper.toAuthRespone(repo.save(userCredential));
        response.setToken(jwtProducer.createToken(new HashMap<>(), request.getUsername()));

        return response;
    }
}
