package com.roster.backned.Validation.Impl;

import com.roster.backned.Enumeration.Role;
import com.roster.backned.Validation.ValidRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class RoleValidator implements ConstraintValidator<ValidRole, String> {

    @Override
    public boolean isValid(String role, ConstraintValidatorContext context) {
        if (role == null || role.isEmpty()) {
            return true; // Allow null or empty values
        }
        return Arrays.stream(Role.values())
                .anyMatch(enumRole -> enumRole.name().equals(role));
    }
}