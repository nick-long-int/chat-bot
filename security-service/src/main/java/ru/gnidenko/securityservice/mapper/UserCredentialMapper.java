package ru.gnidenko.securityservice.mapper;

import org.mapstruct.Mapper;
import ru.gnidenko.securityservice.dto.AuthRequest;
import ru.gnidenko.securityservice.dto.AuthResponse;
import ru.gnidenko.securityservice.model.UserCredential;

@Mapper(componentModel = "spring")
public interface UserCredentialMapper {

    UserCredential toUserCredential(AuthRequest authRequest);
    AuthResponse toAuthRespone(UserCredential userCredential);

}
