package ru.gnidenko.chatservice.handler.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gnidenko.chatservice.client.CatFactClient;

@Component
@RequiredArgsConstructor
public class CatFactCommand implements Command {
    private final String command = "/cats";
    private final String description = "Команда для получения забавных фактов о котятах :3";

    private final CatFactClient client;

    @Override
    public boolean isApplicable(String command) {
        return this.command.equals(command);
    }

    @Override
    public String execute(String chatId) {
        return client.getFact().getFact();
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
