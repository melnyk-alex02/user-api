package com.alex.clearSolutionsTask.dto;

import java.time.LocalDate;

public record UserDTO(
        Long id,
        String email,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String address,
        String phoneNumber
) {
}