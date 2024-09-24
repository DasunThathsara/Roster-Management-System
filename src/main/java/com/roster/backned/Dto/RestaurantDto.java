package com.roster.backned.Dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantDto {
    private long id;
    @NotEmpty(message = "Restaurant name cannot be empty")
    private String name;
    @NotEmpty(message = "Restaurant address cannot be empty")
    private String address;
    @NotEmpty(message = "Restaurant location cannot be empty")
    private String location;
}
