package com.roster.backned.Validation.Impl;

import com.roster.backned.Enumeration.Gender;
import com.roster.backned.Validation.ValidGender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class GenderValidator implements ConstraintValidator<ValidGender, String> {
    @Override
    public boolean isValid(String gender, ConstraintValidatorContext context) {
        if (gender == null || gender.isEmpty()) {
            return true; // Allow null or empty values
        }
        return Arrays.stream(Gender.values())
                .anyMatch(enumGender -> enumGender.name().equals(gender));
    }
}