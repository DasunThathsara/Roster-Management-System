package com.roster.backned.Validation.Impl;

import com.roster.backned.Validation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d).+$");

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isEmpty()) {
            return true; // Allow null or empty values
        }

        // Validate password length and pattern
        return password.length() >= 6 && PASSWORD_PATTERN.matcher(password).matches();
    }
}