package ru.gnidenko.chatservice.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import ru.gnidenko.chatservice.handler.commands.Command;
import ru.gnidenko.chatservice.repo.MessageRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CommandHandler {

    private final List<Command> commands;
    private final MessageRepo repo;

    @Bean
    public Consumer<Message<String>> consumer() {
        return message -> {
            String command = message.getPayload();
            String chatId = message.getHeaders().get("chatId", String.class);
            commands.stream()
                .filter(c -> c.isApplicable(command))
                .findFirst()
                .ifPresentOrElse(c -> {
                        String text = c.execute(chatId);
                        ru.gnidenko.chatservice.model.Message msg = new ru.gnidenko.chatservice.model.Message();
                        msg.setChatId(chatId);
                        msg.setText(text);
                        msg.setTimestamp(LocalDateTime.now());
                        repo.save(msg);
                    },
                    () -> {
                        ru.gnidenko.chatservice.model.Message msg = new ru.gnidenko.chatservice.model.Message();
                        msg.setChatId(chatId);
                        msg.setTimestamp(LocalDateTime.now());
                        StringBuilder builder = new StringBuilder();
                        builder.append("Доступые команды:\n");
                        commands
                            .forEach(c ->
                                builder.append(c.getCommand())
                                    .append(" - ")
                                    .append(c.getDescription())
                                    .append("\n"));
                        msg.setText(builder.toString());
                        repo.save(msg);
                    });
        };
    }

}
