package com.roster.backned.Dto;

import com.roster.backned.Enumeration.Duty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

import java.time.Duration;

@Getter
@Setter
public class AttendanceDetails {
    private long id;
    private String duty;
    private LocalTime startTime;
    private LocalTime endTime;
    private String workedTime;
    private long restaurantId;
    private String restaurantName;

    public AttendanceDetails(long id, Duty duty, LocalTime startTime, LocalTime endTime, long restaurantId, String restaurantName) {
        this.id = id;
        this.duty = String.valueOf(duty);
        this.startTime = startTime;
        this.endTime = endTime;
        this.workedTime = getWorkedTimeFormatted(Duration.between(startTime, endTime));
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
    }

    public String getWorkedTimeFormatted(Duration time) {
        long hours = time.toHours();
        long minutes = time.toMinutes() % 60;
        long seconds = time.toSeconds() % 3600;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
