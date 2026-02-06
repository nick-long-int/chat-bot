package ru.gnidenko.chatservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.gnidenko.chatservice.dto.ChatDto;
import ru.gnidenko.chatservice.service.ChatService;
import ru.gnidenko.chatservice.util.ValidationProcessor;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ValidationProcessor validation;

    @PostMapping
    public ChatDto joinChat(@RequestParam(value = "userId") Long userId){
        validation.checkIsNotNull(userId, "userId must not be null");
        return chatService.joinChat(userId);
    }
}
