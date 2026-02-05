package ru.gnidenko.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateRequestUserDto {
    @NotBlank(message = "username не должен быть пустым")
    @Size(
        min = 5,
        max = 50
    )
    private String username;

    @NotBlank(message = "password не должен быть пустм")
    @Size(
        min = 8,
        max = 255
    )
    private String password;

    @Email
    @NotBlank
    private String email;
}
