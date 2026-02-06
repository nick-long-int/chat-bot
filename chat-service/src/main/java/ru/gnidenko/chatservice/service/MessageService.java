package ru.gnidenko.chatservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gnidenko.chatservice.dto.MessageDto;
import ru.gnidenko.chatservice.exception.NotFoundException;
import ru.gnidenko.chatservice.mapper.MessageMapper;
import ru.gnidenko.chatservice.model.Message;
import ru.gnidenko.chatservice.repo.MessageRepo;
import ru.gnidenko.chatservice.stream.CustomKafkaStream;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageMapper messageMapper;
    private final MessageRepo messageRepo;
    private final CustomKafkaStream stream;


    public MessageDto sendMessage(String chatId, MessageDto messageDto) {
        Message message = messageMapper.messageDtoToMessage(messageDto);
        message.setChatId(chatId);
        message.setTimestamp(LocalDateTime.now());
        stream.sendMsg(messageDto.getText(), chatId);
        return messageMapper.messageToMessageDto(messageRepo.save(message));
    }

    public List<MessageDto> getAllMessages(String chatId) {
        List<Message> messageDtos = messageRepo.findAllByChatId(chatId)
            .orElseThrow(() -> new NotFoundException("not found messages in chat id: " + chatId));
        return messageDtos.stream()
            .map(messageMapper::messageToMessageDto)
            .toList();
    }
}
