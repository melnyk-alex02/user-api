package com.alex.clearSolutionsTask.restcontroller;

import com.alex.clearSolutionsTask.dto.UserCreateDTO;
import com.alex.clearSolutionsTask.dto.UserDTO;
import com.alex.clearSolutionsTask.dto.UserPatchDTO;
import com.alex.clearSolutionsTask.dto.UserUpdateDTO;
import com.alex.clearSolutionsTask.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/search")
    public List<UserDTO> searchUserByDateOgBirth(LocalDate from, LocalDate to) {
        return userService.searchUsersByDateOfBirth(from, to);
    }

    @PostMapping
    public UserDTO createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        return userService.createUser(userCreateDTO);
    }

    @PutMapping()
    public UserDTO updatedUser(@Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        return userService.updateUser(userUpdateDTO);
    }

    @PatchMapping
    public UserDTO patchUser(@Valid @RequestBody UserPatchDTO userPatchDTO) {
        return userService.patchUser(userPatchDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}