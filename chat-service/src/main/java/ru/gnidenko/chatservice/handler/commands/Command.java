package ru.gnidenko.chatservice.handler.commands;

public interface Command {
    boolean isApplicable(String command);
    String execute(String chatId);
    String getCommand();
    String getDescription();
}
