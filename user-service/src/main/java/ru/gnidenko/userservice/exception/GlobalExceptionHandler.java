package ru.gnidenko.userservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.gnidenko.userservice.exception.dto.ErrorResponseDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> response = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            response.put(fieldName, errorMessage);
        });
        log.error("Ошибка валидации параметров регистрации", ex);
        return response;
    }

    @ExceptionHandler(DataValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleDataValidationException(DataValidationException ex) {
        log.error(ex.getMessage(), ex);
        return ErrorResponseDto.builder()
            .message(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .status(400)
            .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleNotFoundException(NotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return ErrorResponseDto.builder()
            .message(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .status(404)
            .build();
    }

    @ExceptionHandler(FieldExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleUsernameExistsException(FieldExistsException ex) {
        log.error(ex.getMessage(), ex);
        return ErrorResponseDto.builder()
            .message(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .status(400)
            .build();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleRuntimeException(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return ErrorResponseDto.builder()
            .message(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .status(400)
            .build();
    }

}
