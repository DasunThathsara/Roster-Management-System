package com.roster.backned.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RosterDto {
    private long id;
    private LocalDate date;
    private long restaurantId;
}
