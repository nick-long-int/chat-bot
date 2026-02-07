package ru.gnidenko.securityservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gnidenko.securityservice.model.UserCredential;

import java.util.Optional;

@Repository
public interface UserCredentialRepo extends JpaRepository<UserCredential, Long> {
    Optional<UserCredential> findByUsername(String username);
    Optional<UserCredential> findByEmail(String email);
}
