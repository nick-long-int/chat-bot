package ru.gnidenko.userservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gnidenko.userservice.model.UserRole;

import java.util.Optional;

@Repository
public interface UserRoleRepo extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByRole(String role);
}
