package ru.gnidenko.chatservice.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.gnidenko.chatservice.model.Chat;

import java.util.Optional;

@Repository
public interface ChatRepo extends MongoRepository<Chat, String> {
    Optional<Chat> findByUserId(Long userId);
}
