package ru.gnidenko.chatservice.exception;

public class ObjectIsNullException extends RuntimeException {
    public ObjectIsNullException(String message) {
        super(message);
    }
}
