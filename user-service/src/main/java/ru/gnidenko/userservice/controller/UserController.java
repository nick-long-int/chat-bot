package ru.gnidenko.userservice.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gnidenko.userservice.dto.CreateRequestUserDto;
import ru.gnidenko.userservice.dto.UserDto;
import ru.gnidenko.userservice.exception.DataValidationException;
import ru.gnidenko.userservice.service.UserService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto addUser(@Valid @RequestBody CreateRequestUserDto userDto, HttpServletResponse response) {
        checkBodyIsNotNull(userDto);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return userService.addUser(userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id, HttpServletResponse response) {
        checkBodyIsNotNull(id);
        response.setStatus(HttpServletResponse.SC_OK);
        userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id, HttpServletResponse response) {
        checkBodyIsNotNull(id);
        response.setStatus(HttpServletResponse.SC_OK);
        return userService.findUser(id);
    }

    @GetMapping
    public List<UserDto> getAllUsers(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        return userService.findAllUsers();
    }

    private <T> void checkBodyIsNotNull(T body){
        if (Objects.isNull(body)){
            throw new DataValidationException("body is null");
        }
    }

}
