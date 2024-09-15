package com.roster.backned.Service;

import com.roster.backned.Dto.RosterDto;
import com.roster.backned.Entity.Restaurant;
import com.roster.backned.Entity.Roster;
import com.roster.backned.Exception.NotFoundException;
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
        Roster roster = new Roster();
        rosterDtoToRoster(rosterDto, roster);
        roster = rosterRepository.save(roster);
        return rosterToRosterDto(roster);
    }

    public RosterDto getRoster(long id) {
        Roster roster = rosterRepository.findById(id).orElseThrow(() -> new NotFoundException("Roster not found"));
        return rosterToRosterDto(roster);
    }

    public List<RosterDto> getAllRosters() {
        List<Roster> rosters = rosterRepository.findAll();
        List<RosterDto> rosterDtos = new ArrayList<>();

        for (Roster roster : rosters) {
            RosterDto rosterDto = rosterToRosterDto(roster);
            rosterDtos.add(rosterDto);
        }
        return rosterDtos;
    }

    public RosterDto updateRoster(RosterDto rosterDto) {
        Roster roster = rosterRepository.findById(rosterDto.getId()).orElseThrow(() -> new NotFoundException("Roster not found"));
        rosterDtoToRoster(rosterDto, roster);
        roster = rosterRepository.save(roster);
        return rosterToRosterDto(roster);
    }

    public RosterDto deleteRoster(long id) {
        Roster roster = rosterRepository.findById(id).orElseThrow(() -> new NotFoundException("Roster not found"));
        rosterRepository.delete(roster);
        return rosterToRosterDto(roster);
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
