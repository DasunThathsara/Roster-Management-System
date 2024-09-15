package com.roster.backned.Controllers.v1;

import com.roster.backned.Dto.RestaurantDto;
import com.roster.backned.Service.RestaurantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurant")
@Tag(name = "Restaurant Controller")
@RequiredArgsConstructor
@Validated
@Slf4j
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<RestaurantDto> addRestaurant(@Valid @RequestBody RestaurantDto restaurantDto) {
        return ResponseEntity.status(201).body(restaurantService.createRestaurant(restaurantDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> getRestaurant(@PathVariable long id) {
        return ResponseEntity.ok().body(restaurantService.getRestaurant(id));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDto>> getAllRestaurants() {
        return ResponseEntity.ok().body(restaurantService.getAllRestaurants());
    }

    @PutMapping
    public ResponseEntity<RestaurantDto> updateRestaurant(@Valid @RequestBody RestaurantDto restaurantDto) {
        return ResponseEntity.ok().body(restaurantService.updateRestaurant(restaurantDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestaurantDto> deleteRestaurant(@PathVariable long id) {
        return ResponseEntity.ok().body(restaurantService.deleteRestaurant(id));
    }
}
