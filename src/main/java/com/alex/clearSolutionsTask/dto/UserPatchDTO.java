package com.alex.clearSolutionsTask.dto;

import com.alex.clearSolutionsTask.annotation.validators.AgeOverEighteen;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record UserPatchDTO(
        Long id,
        String email,
        String firstName,
        String lastName,
        @Nullable
        @Past
        @AgeOverEighteen(nullable = true)
        LocalDate dateOfBirth,
        String address,
        @Pattern(regexp = "^[0-9]{3}-[0-9]{3}-[0-9]{4}$", message = "Phone number should contain 10 digits")
        String phoneNumber
) {
}
