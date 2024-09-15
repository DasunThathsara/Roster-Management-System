package com.roster.backned.Dto;

import com.roster.backned.Enumeration.Duty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class WeekShiftDto {
    private long id;
    private String duty;
    private LocalTime startTime;
    private LocalTime endTime;
    private String user;

    public WeekShiftDto(long id, Duty duty, LocalTime startTime, LocalTime endTime, String user) {
        this.id = id;
        this.duty = String.valueOf(duty);
        this.startTime = startTime;
        this.endTime = endTime;
        this.user = user;
    }
}
