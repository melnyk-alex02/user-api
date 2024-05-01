package com.alex.clearSolutionsTask.mapper;

import com.alex.clearSolutionsTask.dto.UserCreateDTO;
import com.alex.clearSolutionsTask.dto.UserDTO;
import com.alex.clearSolutionsTask.dto.UserPatchDTO;
import com.alex.clearSolutionsTask.dto.UserUpdateDTO;
import com.alex.clearSolutionsTask.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    UserDTO toDto(User user);

    List<UserDTO> toDTO(List<User> users);

    User toEntity(UserUpdateDTO userUpdateDTO);
    User toEntity(UserCreateDTO userCreateDTO);

    User toEntity(UserPatchDTO userPatchDTO);
}