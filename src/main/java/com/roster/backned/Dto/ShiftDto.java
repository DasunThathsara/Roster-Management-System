package com.roster.backned.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class ShiftDto {
    private long id;
    private String duty;
    private LocalTime startTime;
    private LocalTime endTime;
    private long userId;
    private long rosterId;
}
