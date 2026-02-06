package ru.gnidenko.chatservice.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.gnidenko.chatservice.model.Message;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepo extends MongoRepository<Message, String> {
    Optional<List<Message>> findAllByChatId(String chatId);
}
