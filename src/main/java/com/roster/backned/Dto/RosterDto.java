package com.roster.backned.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RosterDto {
    private long id;
    @NotNull(message = "Date cannot be empty")
    private LocalDate date;
    @NotNull(message = "Restaurant cannot be empty")
    private long restaurantId;
}
