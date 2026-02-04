package ru.gnidenko.userservice.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.gnidenko.userservice.dto.CreateRequestUserDto;
import ru.gnidenko.userservice.dto.UserDto;
import ru.gnidenko.userservice.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    User toUser(CreateRequestUserDto userDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(UserDto userDto, @MappingTarget User user);
}
