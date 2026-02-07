package ru.gnidenko.chatservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gnidenko.chatservice.dto.MessageDto;
import ru.gnidenko.chatservice.exception.NotFoundException;
import ru.gnidenko.chatservice.mapper.MessageMapperImpl;
import ru.gnidenko.chatservice.model.Message;
import ru.gnidenko.chatservice.repo.MessageRepo;
import ru.gnidenko.chatservice.stream.CustomKafkaStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepo repo;

    @Mock
    private CustomKafkaStream stream;

    @Spy
    private MessageMapperImpl mapper;

    @InjectMocks
    private MessageService service;

    @Test
    void testSendMessage(){
        String chatId = "chatId";
        String msgId = "msgId";
        MessageDto dto = new MessageDto();
        dto.setText("text");

        Message msg = new Message();
        msg.setChatId(chatId);
        msg.setId(msgId);
        msg.setText("text");

        Mockito.when(repo.save(Mockito.any())).thenReturn(msg);

        dto = service.sendMessage(chatId, dto);

        Mockito.verify(stream, Mockito.times(1))
            .sendMsg(Mockito.any(), Mockito.any());
        assertEquals(msgId, dto.getId());
    }

    @Test
    void testGetAllMessagesThrowNotFoundException(){
        String chatId = "chatId";

        Mockito.when(repo.findAllByChatId(chatId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getAllMessages(chatId));
    }

    @Test
    void testGetAllMessage(){
        String chatId = "chatId";
        List<Message> messages = new ArrayList<>();
        messages.add(new Message());

        Mockito.when(repo.findAllByChatId(chatId)).thenReturn(Optional.of(messages));

        List<MessageDto> dtos = service.getAllMessages(chatId);

        assertNotNull(dtos);
        assertEquals(messages.size(), dtos.size());
    }

}