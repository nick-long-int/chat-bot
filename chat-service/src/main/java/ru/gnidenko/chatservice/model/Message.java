package ru.gnidenko.chatservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Message {
    @Id
    private String id;

    @Indexed(unique = true)
    private String chatId;

    private String text;

    private LocalDateTime timestamp;
}
