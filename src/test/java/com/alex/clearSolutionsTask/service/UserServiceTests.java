package com.alex.clearSolutionsTask.service;

import com.alex.clearSolutionsTask.dto.UserCreateDTO;
import com.alex.clearSolutionsTask.dto.UserDTO;
import com.alex.clearSolutionsTask.dto.UserPatchDTO;
import com.alex.clearSolutionsTask.dto.UserUpdateDTO;
import com.alex.clearSolutionsTask.entity.User;
import com.alex.clearSolutionsTask.exceptions.DataNotFoundException;
import com.alex.clearSolutionsTask.exceptions.InvalidDataException;
import com.alex.clearSolutionsTask.mapper.UserMapper;
import com.alex.clearSolutionsTask.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetAllUsers() {
        List<User> userList = createUserList();

        List<UserDTO> userDTOList = createUserDTOList();

        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.toDTO(userList)).thenReturn(userDTOList);

        List<UserDTO> result = userService.getAllUsers();

        verify(userRepository).findAll();
        verify(userMapper).toDTO(userList);

        assertEquals(userDTOList, result);
    }

    @Test
    public void testGetUserById() {
        User user = createUserList().get(0);
        UserDTO userDTO = createUserDTOList().get(0);

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(1L);

        verify(userRepository).findById(1L);
        verify(userMapper).toDto(user);

        assertEquals(userDTO, result);
    }

    @Test
    public void testSearchUsersByDateOfBirth() {
        LocalDate from = LocalDate.of(1990, 1, 1);
        LocalDate to = LocalDate.of(1992, 3, 3);

        List<User> userList = createUserList();

        List<UserDTO> userDTOList = createUserDTOList();

        when(userRepository.findAllByDateOfBirthIsBetween(from, to)).thenReturn(userList);
        when(userMapper.toDTO(userList)).thenReturn(userDTOList);

        List<UserDTO> result = userService.searchUsersByDateOfBirth(from, to);

        verify(userRepository).findAllByDateOfBirthIsBetween(from, to);
        verify(userMapper).toDTO(userList);

        assertEquals(userDTOList, result);
    }

    @Test
    public void testGetUserById_WhenUserNotFound_ShouldThrowException() {
        Long id = 23L;

        when(userRepository.findById(id)).thenThrow(new DataNotFoundException("User with id " + id + " not found"));

        assertThrows(DataNotFoundException.class, () -> userService.getUserById(id));

        verify(userRepository).findById(id);
    }

    @Test
    public void testCreateUser() {
        UserCreateDTO userCreateDTO = new UserCreateDTO(
                "john@example.com",
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "address1",
                "123456789");

        User userToSave = new User(
                1L,
                "john@example.com",
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "address1",
                "123456789"
        );

        User savedUser = createUserList().get(0);

        UserDTO expectedUserDTO = createUserDTOList().get(0);

        when(userRepository.existsByEmail(userCreateDTO.email())).thenReturn(false);
        when(userMapper.toEntity(userCreateDTO)).thenReturn(userToSave);
        when(userRepository.save(userToSave)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(expectedUserDTO);

        UserDTO result = userService.createUser(userCreateDTO);

        verify(userRepository).existsByEmail(userCreateDTO.email());
        verify(userMapper).toEntity(userCreateDTO);
        verify(userRepository).save(userToSave);
        verify(userMapper).toDto(savedUser);

        assertEquals(expectedUserDTO, result);
    }

    @Test
    public void testCreateUser_WhenUserEmailExists_ShouldThrowException() {
        UserCreateDTO userCreateDTO = new UserCreateDTO(
                "john@example.com",
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "address1",
                "123456789");

        when(userRepository.existsByEmail(userCreateDTO.email())).thenReturn(true);

        assertThrows(InvalidDataException.class, () -> userService.createUser(userCreateDTO));

        verify(userRepository).existsByEmail(userCreateDTO.email());
    }


    @Test
    public void testUpdateUser() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(
                1L,
                "updated_john@example.com",
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "updated_address1",
                "123456789");

        User updatedUser = createUserList().get(0);

        updatedUser.setEmail("updated_john@example.com");
        updatedUser.setAddress("updated_address1");

        UserDTO expextedUserDTO = new UserDTO(
                1L,
                "updated_john@example.com",
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "updated_address1",
                "123456789"
        );

        when(userRepository.existsById(userUpdateDTO.id())).thenReturn(true);
        when(userMapper.toEntity(userUpdateDTO)).thenReturn(updatedUser);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(expextedUserDTO);

        UserDTO result = userService.updateUser(userUpdateDTO);

        verify(userRepository).existsById(userUpdateDTO.id());
        verify(userMapper).toEntity(userUpdateDTO);
        verify(userRepository).save(updatedUser);
        verify(userMapper).toDto(updatedUser);

        assertEquals(expextedUserDTO, result);
    }

    @Test
    public void testUpdateUser_WhenUserNotFound_ShouldThrowException() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(
                123L,
                "",
                "",
                "",
                null,
                "",
                "");

        when(userRepository.existsById(userUpdateDTO.id())).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> userService.updateUser(userUpdateDTO));
    }

    @Test
    public void testPatchUser() {
        UserPatchDTO userPatchDTO = new UserPatchDTO(
                2L,
                "patched_john@example.com",
                null,
                null,
                LocalDate.of(1990, 2, 2),
                "patched_address1",
                null
        );

        User existingUser = createUserList().get(0);

        User patchedUser = createUserList().get(0);
        patchedUser.setEmail("patched_john@example.com");
        patchedUser.setAddress("patched_address1");
        patchedUser.setDateOfBirth(LocalDate.of(1990, 2, 2));

        UserDTO expectedUserDTO = new UserDTO(
                2L,
                "patched_john@example.com",
                "John",
                "Doe",
                LocalDate.of(1990, 2, 2),
                "patched_address1",
                "123456789"
        );

        when(userRepository.findById(2L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsById(2L)).thenReturn(true);
        when(userMapper.toEntity(userPatchDTO)).thenReturn(patchedUser);
        when(userRepository.save(any(User.class))).thenReturn(patchedUser);
        when(userMapper.toDto(any(User.class))).thenReturn(expectedUserDTO);

        UserDTO result = userService.patchUser(userPatchDTO);

        verify(userRepository).findById(2L);
        verify(userMapper).toEntity(userPatchDTO);
        verify(userRepository).save(existingUser);
        verify(userMapper).toDto(patchedUser);

        assertEquals(expectedUserDTO.id(), result.id());
        assertEquals(expectedUserDTO.email(), result.email());
        assertEquals(expectedUserDTO.address(), result.address());
    }

    @Test
    public void testPatchUser_WhenUserNotFound_ShouldThrowException() {
        UserPatchDTO userPatchDTO = new UserPatchDTO(
                123L,
                "",
                "",
                "",
                null,
                "",
                ""
        );

        when(userRepository.findById(userPatchDTO.id())).thenThrow(
                new DataNotFoundException("User with id " + userPatchDTO.id() + " not found")
        );

        assertThrows(DataNotFoundException.class, () -> userService.patchUser(userPatchDTO));
    }

    @Test
    public void testSearchUsersByDateOfBirth_WhenNoUsersFound_ShouldReturnEmptyList() {
        LocalDate from = LocalDate.of(1990, 1, 1);
        LocalDate to = LocalDate.of(1992, 3, 3);

        when(userRepository.findAllByDateOfBirthIsBetween(from, to)).thenReturn(List.of());
        when(userMapper.toDTO(List.of())).thenReturn(List.of());

        List<UserDTO> result = userService.searchUsersByDateOfBirth(from, to);

        verify(userRepository).findAllByDateOfBirthIsBetween(from, to);
        verify(userMapper).toDTO(List.of());

        assertEquals(List.of(), result);
    }


    @Test
    public void testDeleteUserById() {
        Long id = 1L;

        when(userRepository.existsById(id)).thenReturn(true);

        userService.deleteById(id);

        verify(userRepository).existsById(id);
        verify(userRepository).deleteById(id);
    }

    @Test
    public void testDeleteUserById_WhenUserNotFound_ShouldThrowException() {
        Long id = 123L;

        when(userRepository.existsById(id)).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> userService.deleteById(id));
    }

    private List<User> createUserList() {
        return List.of(
                new User(1L,
                        "john@example.com",
                        "John",
                        "Doe",
                        LocalDate.of(1990, 1, 1),
                        "address1",
                        "123456789"),
                new User(2L,
                        "jane@example.com",
                        "Jane",
                        "Clark",
                        LocalDate.of(1991, 2, 2),
                        "address2",
                        "987654321"),
                new User(3L,
                        "alice@example.com",
                        "Alice",
                        "Smith",
                        LocalDate.of(1992, 3, 3),
                        "address3",
                        "123123123")
        );
    }

    private List<UserDTO> createUserDTOList() {
        return List.of(
                new UserDTO(1L,
                        "john@example.com",
                        "John",
                        "Doe",
                        LocalDate.of(1990, 1, 1),
                        "address1",
                        "123456789"),
                new UserDTO(2L,
                        "jane@example.com",
                        "Jane",
                        "Clark",
                        LocalDate.of(1991, 2, 2),
                        "address2",
                        "987654321"),
                new UserDTO(3L,
                        "alice@example.com",
                        "Alice",
                        "Smith",
                        LocalDate.of(1992, 3, 3),
                        "address3",
                        "123123123")
        );
    }
}