package ru.gnidenko.chatservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gnidenko.chatservice.dto.MessageDto;
import ru.gnidenko.chatservice.service.MessageService;
import ru.gnidenko.chatservice.util.ValidationProcessor;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final ValidationProcessor validation;

    @PostMapping("/{chatId}")
    public MessageDto sendMessage(@PathVariable String chatId, @RequestBody MessageDto messageDto) {
        validation.checkIsNotNull(chatId, "chatId must not be null");
        validation.checkIsNotNull(messageDto, "message must not be null");

        return messageService.sendMessage(chatId, messageDto);
    }

    @GetMapping("/{chatId}")
    public List<MessageDto> getMessages(@PathVariable String chatId) {
        validation.checkIsNotNull(chatId, "chatId must not be null");
        return messageService.getAllMessages(chatId);
    }
}
