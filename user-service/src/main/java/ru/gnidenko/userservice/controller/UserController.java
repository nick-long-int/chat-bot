package ru.gnidenko.userservice.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gnidenko.userservice.dto.UserDto;
import ru.gnidenko.userservice.exception.DataValidationException;
import ru.gnidenko.userservice.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id, HttpServletResponse response) {
        if (!getSetRolesFromClaims().contains("ADMIN")) {
            throw new BadCredentialsException("You are not allowed");
        }
        checkBodyIsNotNull(id);
        response.setStatus(HttpServletResponse.SC_OK);
        userService.deleteUser(id);
    }

    @GetMapping("/me")
    public UserDto getUser(HttpServletResponse response) {
        Long currentId = getUserIdFromClaims();
        response.setStatus(HttpServletResponse.SC_OK);
        return userService.findUser(currentId);
    }

    @GetMapping
    public List<UserDto> getAllUsers(HttpServletResponse response) {
        if (!getSetRolesFromClaims().contains("ADMIN")) {
            throw new BadCredentialsException("You are not allowed");
        }
        response.setStatus(HttpServletResponse.SC_OK);
        return userService.findAllUsers();
    }

    @PutMapping
    public UserDto updateUser(@RequestBody UserDto userDto, HttpServletResponse response) {
        checkBodyIsNotNull(userDto);
        Long currentId = getUserIdFromClaims();
        response.setStatus(HttpServletResponse.SC_OK);
        return userService.updateUser(userDto, currentId);
    }

    private <T> void checkBodyIsNotNull(T body) {
        if (Objects.isNull(body)) {
            throw new DataValidationException("body is null");
        }
    }

    private Long getUserIdFromClaims() {
        return getJwtFromPrincipal().getClaim("userId");
    }

    private List<String> getSetRolesFromClaims() {
        return getJwtFromPrincipal().getClaim("roles");
    }

    private Jwt getJwtFromPrincipal() {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
