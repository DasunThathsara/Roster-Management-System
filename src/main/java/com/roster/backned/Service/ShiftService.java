package com.roster.backned.Service;

import com.roster.backned.Dto.ShiftDto;
import com.roster.backned.Dto.WeekShiftDto;
import com.roster.backned.Entity.Roster;
import com.roster.backned.Entity.Shift;
import com.roster.backned.Entity.User;
import com.roster.backned.Enumeration.Duty;
import com.roster.backned.Exception.NotFoundException;
import com.roster.backned.Repository.RosterRepository;
import com.roster.backned.Repository.ShiftRepository;
import com.roster.backned.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;
    private final RosterRepository rosterRepository;

    public ShiftDto createShift(ShiftDto shiftDto) {
        Shift shift = new Shift();
        shiftDtoToShift(shiftDto, shift);
        shift = shiftRepository.save(shift);
        return shiftToShiftDto(shift);
    }

    public ShiftDto getShift(long id) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new NotFoundException("Shift not found"));
        return shiftToShiftDto(shift);
    }

    public List<ShiftDto> getAllShifts() {
        List<Shift> shifts = shiftRepository.findAll();
        List<ShiftDto> shiftDtos = new ArrayList<>();

        for (Shift shift : shifts) {
            ShiftDto shiftDto = shiftToShiftDto(shift);
            shiftDtos.add(shiftDto);
        }
        return shiftDtos;
    }

    public ShiftDto updateShift(ShiftDto shiftDto) {
        Shift shift = shiftRepository.findById(shiftDto.getId()).orElseThrow(() -> new NotFoundException("Shift not found"));
        shiftDtoToShift(shiftDto, shift);
        shift = shiftRepository.save(shift);
        return shiftToShiftDto(shift);
    }

    public ShiftDto deleteShift(long id) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new NotFoundException("Shift not found"));
        shiftRepository.delete(shift);
        return shiftToShiftDto(shift);
    }

    private void shiftDtoToShift(ShiftDto shiftDto, Shift shift) {
        shift.setDuty(Duty.valueOf(shiftDto.getDuty()));
        shift.setStartTime(shiftDto.getStartTime());
        shift.setEndTime(shiftDto.getEndTime());

        User user = userRepository.findById(shiftDto.getUserId()).orElseThrow(() -> new NotFoundException("User not found"));
        shift.setUser(user);

        Roster roster = rosterRepository.findById(shiftDto.getRosterId()).orElseThrow(() -> new NotFoundException("Roster not found"));
        shift.setRoster(roster);
    }

    private ShiftDto shiftToShiftDto(Shift shift) {
        ShiftDto shiftDto = new ShiftDto();
        shiftDto.setId(shift.getId());
        shiftDto.setDuty(String.valueOf(shift.getDuty()));
        shiftDto.setStartTime(shift.getStartTime());
        shiftDto.setEndTime(shift.getEndTime());
        shiftDto.setUserId(shift.getUser().getUserId());
        shiftDto.setRosterId(shift.getRoster().getId());

        return shiftDto;
    }

    public List<WeekShiftDto> getShiftsForWeek(LocalDate date, long restaurantId) {
        LocalDate endDate = date.plusDays(6);
        return shiftRepository.findShiftsByDateRangeAndRestaurant(date, endDate, restaurantId);
    }
}
