package ru.gnidenko.chatservice.util;

import org.springframework.stereotype.Component;
import ru.gnidenko.chatservice.exception.DataValidationException;

import java.util.Objects;

@Component
public class ValidationProcessor {

    public <T> void checkIsNotNull(T obj, String message){
        if (Objects.isNull(obj)){
            throw new DataValidationException(message);
        }
    }
}
