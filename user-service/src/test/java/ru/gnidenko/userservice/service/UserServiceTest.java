package ru.gnidenko.userservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gnidenko.userservice.dto.UserDto;
import ru.gnidenko.userservice.exception.FieldExistsException;
import ru.gnidenko.userservice.exception.NotFoundException;
import ru.gnidenko.userservice.mapper.UserMapperImpl;
import ru.gnidenko.userservice.model.User;
import ru.gnidenko.userservice.repo.UserRepo;
import ru.gnidenko.userservice.repo.UserRoleRepo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private UserRoleRepo userRoleRepo;

    @Spy
    private UserMapperImpl userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void testDeleteUserWithIdWhichNotExists() {
        Long id = 1L;

        Mockito.when(userRepo.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.deleteUser(id));
        Mockito.verify(userRepo, Mockito.times(1)).findById(id);
    }

    @Test
    void testDeleteUserSucceeds() {
        Long id = 1L;
        Mockito.when(userRepo.findById(id)).thenReturn(Optional.of(new User()));
        userService.deleteUser(id);
        Mockito.verify(userRepo, Mockito.times(1)).findById(id);
    }

    @Test
    void testFindUserWithIdWhichNotExists() {
        Long id = 1L;
        Mockito.when(userRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.findUser(id));
    }

    @Test
    void testFindUserSucceeds() {
        Long id = 1L;
        User user = new User();
        user.setId(1L);
        Mockito.when(userRepo.findById(id)).thenReturn(Optional.of(user));

        userService.findUser(id);

        Mockito.verify(userRepo, Mockito.times(1)).findById(id);
        assertEquals(1L, user.getId());
    }

    @Test
    void testFindAllUsers() {
        Mockito.when(userRepo.findAll()).thenReturn(List.of(
            new User(), new User(), new User(), new User()
        ));

        List<UserDto> dtos = userService.findAllUsers();

        Mockito.verify(userRepo, Mockito.times(1)).findAll();
        assertEquals(4, dtos.size());
    }

    @Test
    void testUpdateUserWithUsernameWhichExists() {
        UserDto userDto = new UserDto();
        userDto.setUsername("username");
        Long id = 1L;

        Mockito.when(userRepo.findByUsername("username")).thenReturn(Optional.of(new User()));

        assertThrows(FieldExistsException.class, () -> userService.updateUser(userDto, id));
    }

    @Test
    void testUpdateUserThrowsNotFoundException() {
        UserDto userDto = new UserDto();
        Long id = 1L;

        Mockito.when(userRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.updateUser(userDto, id));
    }

    @Test
    void testUpdateUserThrowsEmailExistsException() {
        UserDto userDto = new UserDto();
        userDto.setEmail("email");
        Long id = 1L;

        Mockito.when(userRepo.findByEmail("email")).thenReturn(Optional.of(Mockito.mock(User.class)));

        assertThrows(FieldExistsException.class, () -> userService.updateUser(userDto, id));
    }

    @Test
    void testUpdateUserSucceeds() {
        User user = new User();
        user.setUsername("username");
        UserDto userDto = new UserDto();
        userDto.setUsername("updated");
        Long id = 1L;

        Mockito.when(userRepo.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(userRepo.findById(id)).thenReturn(Optional.of(user));

        userService.updateUser(userDto, id);

        assertEquals("updated", user.getUsername());
    }
}