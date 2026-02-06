package ru.gnidenko.chatservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gnidenko.chatservice.dto.ChatDto;
import ru.gnidenko.chatservice.mapper.ChatMapperImpl;
import ru.gnidenko.chatservice.model.Chat;
import ru.gnidenko.chatservice.repo.ChatRepo;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatRepo chatRepo;

    @Spy
    private ChatMapperImpl chatMapper;

    @InjectMocks
    private ChatService chatService;


    @Test
    void testCreateNewChat() {
        Long userId = 1L;
        Chat newChat = new Chat();
        newChat.setUserId(userId);

        Mockito.when(chatRepo.findByUserId(Mockito.anyLong())).thenReturn(Optional.empty());
        Mockito.when(chatRepo.save(Mockito.any(Chat.class))).thenReturn(newChat);

        ChatDto dto = chatService.joinChat(userId);

        assertNotNull(dto);
        assertEquals(dto.getUserId(), userId);
    }

    @Test
    void testJoinExistingChat() {
        Long userId = 1L;
        Chat existingChat = new Chat();
        existingChat.setUserId(userId);
        existingChat.setCreatedAt(LocalDateTime.MIN);

        Mockito.when(chatRepo.findByUserId(Mockito.anyLong())).thenReturn(Optional.of(existingChat));

        chatService.joinChat(userId);

        assertEquals(existingChat.getUserId(), userId);
        assertEquals(existingChat.getCreatedAt(), LocalDateTime.MIN);
    }

}