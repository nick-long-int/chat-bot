package ru.gnidenko.chatservice.client;

import feign.RequestLine;
import ru.gnidenko.chatservice.dto.feign.CatFactDto;

public interface CatFactClient {
    @RequestLine("GET /fact")
    CatFactDto getFact();
}
