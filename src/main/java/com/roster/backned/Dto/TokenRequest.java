package com.roster.backned.Dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequest {
    @NotEmpty(message = "Token is empty")
    private String token;
}
