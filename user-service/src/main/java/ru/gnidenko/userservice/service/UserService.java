package ru.gnidenko.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.gnidenko.userservice.dto.CreateRequestUserDto;
import ru.gnidenko.userservice.dto.UserDto;
import ru.gnidenko.userservice.exception.NotFoundException;
import ru.gnidenko.userservice.exception.FieldExistsException;
import ru.gnidenko.userservice.mapper.UserMapper;
import ru.gnidenko.userservice.model.User;
import ru.gnidenko.userservice.repo.UserRepo;
import ru.gnidenko.userservice.repo.UserRoleRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final UserRoleRepo roleRepo;
    private final UserMapper userMapper;

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED
    )
    public UserDto addUser(CreateRequestUserDto userDto) {
        checkUsernameNotExists(userDto.getUsername());
        checkEmailNotExists(userDto.getEmail());

        User user = userMapper.toUser(userDto);
        user.setRoles(Set.of(roleRepo.findByRole("USER")
            .orElseThrow(() -> new NotFoundException("Role not found"))));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        user = userRepo.save(user);
        log.info("Username was created with id: {} and username: {}", user.getId(), userDto.getUsername());
        return userMapper.toUserDto(user);

    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED
    )
    public void deleteUser(Long id) {
        User userToDelete = userRepo.findById(id).orElseThrow(
            () -> new NotFoundException("User not found with id: " + id));
        userRepo.delete(userToDelete);
    }

    @Transactional(
        readOnly = true
    )
    public UserDto findUser(Long id) {
        User user = userRepo.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        return userMapper.toUserDto(user);
    }

    @Transactional(
        readOnly = true
    )
    public List<UserDto> findAllUsers() {
        return userRepo.findAll()
            .stream()
            .map(userMapper::toUserDto)
            .toList();
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED
    )
    public UserDto updateUser(UserDto userDto, Long id) {
        if (Objects.nonNull(userDto.getUsername())){
            checkUsernameNotExists(userDto.getUsername());
        }
        if (Objects.nonNull(userDto.getEmail())){
            checkEmailNotExists(userDto.getEmail());
        }

        User user = userRepo.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        userMapper.updateUser(userDto, user);
        user.setUpdatedAt(LocalDateTime.now());
        return userMapper.toUserDto(user);
    }

    private void checkUsernameNotExists(String username) {
        if (userRepo.findByUsername(username).isPresent()) {
            throw new FieldExistsException(username + " already exists");
        }
    }

    private void checkEmailNotExists(String email) {
        if(userRepo.findByEmail(email).isPresent()) {
            throw new FieldExistsException(email + " already exists");
        }
    }

}
