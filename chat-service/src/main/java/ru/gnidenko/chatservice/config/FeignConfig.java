package ru.gnidenko.chatservice.config;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.gnidenko.chatservice.client.CatFactClient;
import ru.gnidenko.chatservice.client.RandomJokeClient;

@Configuration
@EnableFeignClients
@RequiredArgsConstructor
public class FeignConfig {

    @Bean
    public CatFactClient catFactClient() {
        return Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .target(CatFactClient.class, "https://catfact.ninja");
    }

    @Bean
    public RandomJokeClient randomJokeClient() {
        return Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .target(RandomJokeClient.class, "https://official-joke-api.appspot.com");

    }
}
