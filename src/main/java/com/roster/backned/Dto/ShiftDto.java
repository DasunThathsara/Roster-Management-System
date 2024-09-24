package com.roster.backned.Dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class ShiftDto {
    private long id;
    @NotEmpty(message = "Duty cannot be empty")
    private String duty;
    @NotNull(message = "Start time cannot be empty")
    private LocalTime startTime;
    @NotNull(message = "End time cannot be empty")
    private LocalTime endTime;
    @NotNull(message = "User cannot be empty")
    private long userId;
    @NotNull(message = "Roster cannot be empty")
    private long rosterId;
}
