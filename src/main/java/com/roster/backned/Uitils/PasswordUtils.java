package com.roster.backned.Uitils;

import java.security.SecureRandom;
import java.util.Random;

public class PasswordUtils {
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String ALL_CHARS = LETTERS + DIGITS;
    private static final Random RANDOM = new SecureRandom();

    public static String generatePassword(int length) {
        if (length < 6) {
            throw new IllegalArgumentException("Password length must be at least 6 characters");
        }

        StringBuilder password = new StringBuilder(length);

        // Ensure at least one letter and one digit
        password.append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));
        password.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));

        // Fill the rest of the password with random characters
        for (int i = 2; i < length; i++) {
            password.append(ALL_CHARS.charAt(RANDOM.nextInt(ALL_CHARS.length())));
        }

        // Shuffle the characters to avoid predictable patterns
        return shuffleString(password.toString());
    }

    private static String shuffleString(String string) {
        char[] characters = string.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            int randomIndex = RANDOM.nextInt(characters.length);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        return new String(characters);
    }
}

