package com.roster.backned.Service;

import com.roster.backned.Dto.RosterDto;
import com.roster.backned.Entity.Restaurant;
import com.roster.backned.Entity.Roster;
import com.roster.backned.Exception.ApiException;
import com.roster.backned.Exception.NotFoundException;
import com.roster.backned.Exception.SQLException;
import com.roster.backned.Repository.RestaurantRepository;
import com.roster.backned.Repository.RosterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RosterService {
    private final RosterRepository rosterRepository;
    private final RestaurantRepository restaurantRepository;

    public RosterDto createRoster(RosterDto rosterDto) {
        try {
            Roster roster = new Roster();
            rosterDtoToRoster(rosterDto, roster);
            roster = rosterRepository.save(roster);

            log.info("Roster {} added successfully", roster.getId());
            return rosterToRosterDto(roster);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    public RosterDto getRoster(long id) {
        Roster roster = rosterRepository.findById(id).orElseThrow(() -> new NotFoundException("Roster not found"));
        return rosterToRosterDto(roster);
    }

    public List<RosterDto> getAllRosters() {
        List<Roster> rosters = rosterRepository.findAll();
        List<RosterDto> rosterDtos = new ArrayList<>();

        if (rosters.isEmpty()) throw new NotFoundException("Rosters not found");

        for (Roster roster : rosters) {
            RosterDto rosterDto = rosterToRosterDto(roster);
            rosterDtos.add(rosterDto);
        }
        return rosterDtos;
    }

    public RosterDto updateRoster(RosterDto rosterDto) {
        try {
            Roster roster = rosterRepository.findById(rosterDto.getId()).orElseThrow(() -> new NotFoundException("Roster not found"));
            rosterDtoToRoster(rosterDto, roster);
            roster = rosterRepository.save(roster);

            log.info("Roster {} updated successfully", roster.getId());
            return rosterToRosterDto(roster);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    public RosterDto deleteRoster(long id) {
        try {
            Roster roster = rosterRepository.findById(id).orElseThrow(() -> new NotFoundException("Roster not found"));
            rosterRepository.delete(roster);

            log.info("Roster {} deleted successfully", roster.getId());
            return rosterToRosterDto(roster);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    private void rosterDtoToRoster(RosterDto rosterDto, Roster roster) {
        roster.setDate(rosterDto.getDate());

        Restaurant restaurant = restaurantRepository.findById(rosterDto.getRestaurantId()).orElseThrow(() -> new NotFoundException("Restaurant not found"));
        roster.setRestaurant(restaurant);
    }

    private RosterDto rosterToRosterDto(Roster roster) {
        RosterDto rosterDto = new RosterDto();
        rosterDto.setId(roster.getId());
        rosterDto.setDate(roster.getDate());
        rosterDto.setRestaurantId(roster.getRestaurant().getId());

        return rosterDto;
    }
}
