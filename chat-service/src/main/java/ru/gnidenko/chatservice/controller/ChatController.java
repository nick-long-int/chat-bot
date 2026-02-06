package ru.gnidenko.chatservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.gnidenko.chatservice.dto.ChatDto;
import ru.gnidenko.chatservice.exception.ObjectIsNullException;
import ru.gnidenko.chatservice.service.ChatService;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatDto joinChat(@RequestParam(value = "userId") Long userId){
        checkIsNotNull(userId, "userId must not be null");
        return chatService.joinChat(userId);
    }


    private <T> void checkIsNotNull(T obj, String message){
        if (Objects.isNull(obj)){
            throw new ObjectIsNullException(message);
        }
    }

}
