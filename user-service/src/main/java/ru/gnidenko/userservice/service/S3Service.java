package ru.gnidenko.userservice.service;

import com.thoughtworks.xstream.core.util.Base64Encoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.gnidenko.userservice.exception.NotFoundException;
import ru.gnidenko.userservice.model.User;
import ru.gnidenko.userservice.repo.S3Repo;
import ru.gnidenko.userservice.repo.UserRepo;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Repo s3Repo;
    private final UserRepo userRepo;

    @Transactional(
        propagation = Propagation.REQUIRED,
        isolation = Isolation.READ_COMMITTED
    )
    public void upload(MultipartFile file, Long userId) {
        try {
            User user = findUser(userId);
            String key = s3Repo.save(file.getBytes(), file.getOriginalFilename(), userId);
            user.setKey(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(
        readOnly = true
    )
    public byte[] download(Long userId) {
        User user = findUser(userId);
        if (Objects.isNull(user.getKey())) {
            throw new NotFoundException("Key not found");
        }
        try {
            return s3Repo.get(user.getKey());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(
        propagation = Propagation.REQUIRED,
        isolation = Isolation.READ_COMMITTED
    )
    public void delete(Long userId) {
        User user = findUser(userId);
        s3Repo.delete(user.getKey());
        user.setKey(null);
    }

    private User findUser(Long userId) {
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        return user.get();
    }
}
