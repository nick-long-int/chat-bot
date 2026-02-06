package ru.gnidenko.chatservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gnidenko.chatservice.dto.ChatDto;
import ru.gnidenko.chatservice.mapper.ChatMapper;
import ru.gnidenko.chatservice.model.Chat;
import ru.gnidenko.chatservice.repo.ChatRepo;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepo chatRepo;
    private final ChatMapper chatMapper;

    public ChatDto joinChat(Long userId) {
        Optional<Chat> chat = chatRepo.findByUserId(userId);
        if (chat.isEmpty()) {
            Chat newChat = new Chat();
            newChat.setUserId(userId);
            newChat.setCreatedAt(LocalDateTime.now());
            newChat.setUpdatedAt(LocalDateTime.now());
            return chatMapper.toDto(chatRepo.save(newChat));
        }
        return chatMapper.toDto(chat.get());
    }
}
