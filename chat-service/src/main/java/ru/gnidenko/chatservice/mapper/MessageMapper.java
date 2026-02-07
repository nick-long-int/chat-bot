package ru.gnidenko.chatservice.mapper;

import org.mapstruct.Mapper;
import ru.gnidenko.chatservice.dto.MessageDto;
import ru.gnidenko.chatservice.model.Message;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    Message messageDtoToMessage(MessageDto messageDto);
    MessageDto messageToMessageDto(Message message);

}
