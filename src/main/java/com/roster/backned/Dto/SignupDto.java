package com.roster.backned.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.roster.backned.Validation.ValidGender;
import com.roster.backned.Validation.ValidPassword;
import com.roster.backned.Validation.ValidRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class SignupDto {
    @NotEmpty(message = "First name cannot be empty")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email address")
    private String email;

    @NotNull(message = "Dob cannot be empty")
    private LocalDate dob;

    @ValidGender
    private String gender;

    @ValidPassword
    private String password;

    @ValidRole
    private String role;
}
