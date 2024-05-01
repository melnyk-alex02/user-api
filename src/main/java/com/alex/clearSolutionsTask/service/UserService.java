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
import com.alex.clearSolutionsTask.utils.Patcher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDTO> getAllUsers() {
        return userMapper.toDTO(userRepository.findAll());
    }

    public UserDTO getUserById(Long id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User with id " + id + " not found")));
    }

    public List<UserDTO> searchUsersByDateOfBirth(LocalDate from, LocalDate to) {
        return userMapper.toDTO(userRepository.findAllByDateOfBirthIsBetween(from, to));
    }

    public UserDTO createUser(UserCreateDTO userCreateDTO) {
        if (userRepository.existsByEmail(userCreateDTO.email())) {
            throw new InvalidDataException("User with email " + userCreateDTO.email() + " already exists");
        }
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userCreateDTO)));
    }


    public UserDTO updateUser(UserUpdateDTO userUpdateDTO) {
        if (!userRepository.existsById(userUpdateDTO.id())) {
            throw new DataNotFoundException("User with id " + userUpdateDTO.id() + " not found");
        }

        return userMapper.toDto(userRepository.save(userMapper.toEntity(userUpdateDTO)));
    }

    public UserDTO patchUser(UserPatchDTO userPatchDTO) {
        if (!userRepository.existsById(userPatchDTO.id())) {
            throw new DataNotFoundException("User with id " + userPatchDTO.id() + " not found");
        }

        User existingUser = userRepository.findById(userPatchDTO.id())
                .orElseThrow(() -> new DataNotFoundException("User with id " +
                        userPatchDTO.id() + " not found"));

        User incopmpleteUser = userMapper.toEntity(userPatchDTO);
        try {
            Patcher.userPatcher(existingUser, incopmpleteUser);
            return userMapper.toDto(userRepository.save(existingUser));
        } catch (Exception e) {
            throw new InvalidDataException("Invalid data");
        }
    }

    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new DataNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }
}