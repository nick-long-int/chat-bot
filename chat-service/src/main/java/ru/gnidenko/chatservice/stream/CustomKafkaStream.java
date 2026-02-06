package ru.gnidenko.chatservice.stream;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomKafkaStream {

    private final StreamBridge streamBridge;
    @Value("${commands.topic}")
    private String topic;

    public void sendMsg(String command, String chatId) {
        Message<String> message = MessageBuilder
            .withPayload(command)
            .setHeader("chatId", chatId)
            .build();
        streamBridge.send(topic, message);
    }
}
