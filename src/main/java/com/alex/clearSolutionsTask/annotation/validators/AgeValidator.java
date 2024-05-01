package com.alex.clearSolutionsTask.annotation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AgeValidator implements ConstraintValidator<AgeOverEighteen, LocalDate> {
    private boolean isNullable;

    @Override
    public void initialize(AgeOverEighteen constraintAnnotation) {
        isNullable = constraintAnnotation.nullable();
    }
    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext context) {
        if (dateOfBirth == null) {
            System.out.println(isNullable);
            return isNullable;
        }
            LocalDate today = LocalDate.now();
            Period period = Period.between(dateOfBirth, today);
            return period.getYears() >= 18;

    }
}