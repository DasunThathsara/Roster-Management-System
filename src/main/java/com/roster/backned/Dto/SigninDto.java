package com.roster.backned.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.ToString;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class SigninDto {
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
