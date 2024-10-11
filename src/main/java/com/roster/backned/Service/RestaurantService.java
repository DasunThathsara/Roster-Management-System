package com.roster.backned.Service;

import com.roster.backned.Dto.RestaurantDto;
import com.roster.backned.Entity.Restaurant;
import com.roster.backned.Entity.User;
import com.roster.backned.Exception.ApiException;
import com.roster.backned.Exception.NotFoundException;
import com.roster.backned.Exception.SQLException;
import com.roster.backned.Repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final JwtService jwtService;

    public RestaurantDto createRestaurant(RestaurantDto restaurantDto) {
        try {
            User currentUser = jwtService.getCurrentUser();

            Restaurant restaurant = new Restaurant();
            restaurantDtoToRestaurant(restaurantDto, restaurant);
            restaurant.setCreatedBy(currentUser);
            restaurant.setUpdatedBy(currentUser);

            restaurant = restaurantRepository.save(restaurant);

            log.info("Restaurant {} added successfully", restaurant.getId());
            return restaurantToRestaurantDto(restaurant);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    public RestaurantDto getRestaurant(long id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new NotFoundException("Restaurant not found"));
        return restaurantToRestaurantDto(restaurant);
    }

    public List<RestaurantDto> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        List<RestaurantDto> restaurantDtos = new ArrayList<>();

        if (restaurants.isEmpty()) throw new NotFoundException("Restaurants not found");

        for (Restaurant restaurant : restaurants) {
            RestaurantDto restaurantDto = restaurantToRestaurantDto(restaurant);
            restaurantDtos.add(restaurantDto);
        }
        return restaurantDtos;
    }

    public RestaurantDto updateRestaurant(RestaurantDto restaurantDto) {
        try {
            User currentUser = jwtService.getCurrentUser();

            Restaurant restaurant = restaurantRepository.findById(restaurantDto.getId()).orElseThrow(() -> new NotFoundException("Restaurant not found"));
            restaurantDtoToRestaurant(restaurantDto, restaurant);
            restaurant.setUpdatedBy(currentUser);

            restaurant = restaurantRepository.save(restaurant);

            log.info("Restaurant {} updated successfully", restaurant.getId());
            return restaurantToRestaurantDto(restaurant);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    public RestaurantDto deleteRestaurant(long id) {
        try {
            Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new NotFoundException("Restaurant not found"));
            restaurantRepository.delete(restaurant);

            log.info("Restaurant {} deleted successfully", restaurant.getId());
            return restaurantToRestaurantDto(restaurant);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    private static void restaurantDtoToRestaurant(RestaurantDto restaurantDto, Restaurant restaurant) {
        restaurant.setName(restaurantDto.getName());
        restaurant.setAddress(restaurantDto.getAddress());
        restaurant.setLocation(restaurantDto.getLocation());
    }

    private static RestaurantDto restaurantToRestaurantDto(Restaurant restaurant) {
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setId(restaurant.getId());
        restaurantDto.setName(restaurant.getName());
        restaurantDto.setAddress(restaurant.getAddress());
        restaurantDto.setLocation(restaurant.getLocation());
        return restaurantDto;
    }
}
