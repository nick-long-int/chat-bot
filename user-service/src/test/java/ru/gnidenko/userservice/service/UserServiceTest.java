package ru.gnidenko.userservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gnidenko.userservice.dto.CreateRequestUserDto;
import ru.gnidenko.userservice.dto.UserDto;
import ru.gnidenko.userservice.exception.NotFoundException;
import ru.gnidenko.userservice.exception.UsernameExistsException;
import ru.gnidenko.userservice.mapper.UserMapperImpl;
import ru.gnidenko.userservice.model.User;
import ru.gnidenko.userservice.model.UserRole;
import ru.gnidenko.userservice.repo.UserRepo;
import ru.gnidenko.userservice.repo.UserRoleRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void testAddUserThrowsUsernameExistsException() {
        CreateRequestUserDto requestUserDto = new CreateRequestUserDto();
        requestUserDto.setUsername("username");

        User user = new User();
        user.setUsername("username");

        Mockito.when(userRepo.findByUsername(requestUserDto.getUsername()))
            .thenReturn(Optional.of(user));

        assertThrows(UsernameExistsException.class,
            () -> userService.addUser(requestUserDto));
    }

    @Test
    void testAddUserThrowsNotFoundExceptionIfRoleNotExists() {
        CreateRequestUserDto requestUserDto = new CreateRequestUserDto();
        requestUserDto.setUsername("username");

        Mockito.when(userRoleRepo.findByRole("USER")).thenThrow(new NotFoundException(requestUserDto.getUsername()));

        assertThrows(NotFoundException.class, () -> userService.addUser(requestUserDto));
    }

    @Test
    void testAddUserSucceeds() {
        CreateRequestUserDto requestUserDto = new CreateRequestUserDto();
        requestUserDto.setUsername("username");
        requestUserDto.setPassword("password");
        requestUserDto.setEmail("email");

        UserRole userRole = new UserRole();
        userRole.setRole("USER");
        userRole.setId(1L);

        User user = new User();
        user.setId(1L);

        Mockito.when(userRepo.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(userRoleRepo.findByRole("USER")).thenReturn(Optional.of(userRole));
        Mockito.when(userRepo.save(Mockito.any())).thenReturn(user);

        assertNotNull(userService.addUser(requestUserDto));
        assertEquals(1L, user.getId());
    }

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
    void testFindAllUsers(){
        Mockito.when(userRepo.findAll()).thenReturn(List.of(
            new User(), new User(), new User(), new User()
        ));

        List< UserDto> dtos = userService.findAllUsers();

        Mockito.verify(userRepo, Mockito.times(1)).findAll();
        assertEquals(4, dtos.size());
    }

    @Test
    void testUpdateUserWithUsernameWhichExists() {
        UserDto userDto = new UserDto();
        userDto.setUsername("username");
        Long id = 1L;

        Mockito.when(userRepo.findByUsername("username")).thenReturn(Optional.of(new User()));

        assertThrows(UsernameExistsException.class, () -> userService.updateUser(userDto, id));
    }

    @Test
    void testUpdateUserThrowsNotFoundException(){
        UserDto userDto = new UserDto();
        Long id = 1L;

        Mockito.when(userRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.updateUser(userDto, id));
    }

    @Test
    void testUpdateUserSucceeds(){
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