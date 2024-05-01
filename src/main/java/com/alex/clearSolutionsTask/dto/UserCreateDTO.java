package com.alex.clearSolutionsTask.dto;

import com.alex.clearSolutionsTask.annotation.validators.AgeOverEighteen;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record UserCreateDTO(
        @NotNull(message = "Email is mandatory")
        @NotBlank(message = "Email is mandatory")
        String email,
        @NotNull(message = "First name is mandatory")
        @NotBlank(message = "First name is mandatory")
        @Pattern(regexp = "^[a-zA-Z]+$", message = "First name should contain only letters")
        String firstName,
        @NotNull(message = "Last name is mandatory")
        @NotBlank(message = "Last name is mandatory")
        @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name should contain only letters")
        String lastName,
        @NotNull(message = "Date of birth is mandatory")
        @AgeOverEighteen
        @Past
        LocalDate dateOfBirth,
        String address,
        @Pattern(regexp = "^[0-9]{3}-[0-9]{3}-[0-9]{4}$", message = "Phone number should contain 10 digits")
        String phoneNumber
) {
}