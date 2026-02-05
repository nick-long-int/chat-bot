package ru.gnidenko.userservice.exception;

public class FieldExistsException extends RuntimeException {
    public FieldExistsException(String message) {
        super(message);
    }
}
