package com.roster.backned.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserDto {
    private long userId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;
    private String gender;
    private String role;
}
