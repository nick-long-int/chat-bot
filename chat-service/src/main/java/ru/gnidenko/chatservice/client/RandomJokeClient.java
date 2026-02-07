package ru.gnidenko.chatservice.client;

import feign.RequestLine;
import ru.gnidenko.chatservice.dto.feign.RandomJokeDto;

public interface RandomJokeClient {
    @RequestLine("GET /random_joke")
    RandomJokeDto getRandomJoke();
}
