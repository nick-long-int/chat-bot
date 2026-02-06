package ru.gnidenko.chatservice.handler.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gnidenko.chatservice.client.RandomJokeClient;
import ru.gnidenko.chatservice.dto.feign.RandomJokeDto;

@Component
@RequiredArgsConstructor
public class RandomJokeCommand implements Command {

    private final String command = "/joke";
    private final String description = "Получение рандомной шутки";

    private final RandomJokeClient client;


    @Override
    public boolean isApplicable(String command) {
        return this.command.equals(command);
    }

    @Override
    public String execute(String chatId) {
        RandomJokeDto dto = client.getRandomJoke();
        return dto.getSetup() + "\n" + dto.getPunchline();
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
