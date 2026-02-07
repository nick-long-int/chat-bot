package ru.gnidenko.chatservice.mapper;

import org.mapstruct.Mapper;
import ru.gnidenko.chatservice.dto.ChatDto;
import ru.gnidenko.chatservice.model.Chat;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    ChatDto toDto(Chat chat);

}
