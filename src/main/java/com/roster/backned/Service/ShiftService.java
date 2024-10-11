package com.roster.backned.Service;

import com.roster.backned.Dto.AttendanceDetails;
import com.roster.backned.Dto.ShiftDto;
import com.roster.backned.Dto.ShiftRequestDto;
import com.roster.backned.Dto.WeekShiftDto;
import com.roster.backned.Entity.Roster;
import com.roster.backned.Entity.Shift;
import com.roster.backned.Entity.User;
import com.roster.backned.Enumeration.Duty;
import com.roster.backned.Exception.ApiException;
import com.roster.backned.Exception.NotFoundException;
import com.roster.backned.Exception.SQLException;
import com.roster.backned.Repository.RosterRepository;
import com.roster.backned.Repository.ShiftRepository;
import com.roster.backned.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;
    private final RosterRepository rosterRepository;

    public ShiftDto createShift(ShiftRequestDto shiftDto) {
        try {
            Shift shift = new Shift();
            shiftDtoToShift(shiftDto, shift);

            shift.setCreatedBy(shiftDto.getCurrentUserId());
            shift.setModifiedBy(shiftDto.getCurrentUserId());

            shift.setCreatedDate(LocalDateTime.now());
            shift.setModifiedDate(LocalDateTime.now());

            shift = shiftRepository.save(shift);

            log.info("Shift {} added successfully", shift.getId());
            return shiftToShiftDto(shift);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    public ShiftDto getShift(long id) {
        Shift shift = shiftRepository.findById(id).orElseThrow(() -> new NotFoundException("Shift not found"));
        return shiftToShiftDto(shift);
    }

    public List<ShiftDto> getAllShifts() {
        List<Shift> shifts = shiftRepository.findAll();
        List<ShiftDto> shiftDtos = new ArrayList<>();

        if (shifts.isEmpty()) throw new NotFoundException("Shifts not found");

        for (Shift shift : shifts) {
            ShiftDto shiftDto = shiftToShiftDto(shift);
            shiftDtos.add(shiftDto);
        }
        return shiftDtos;
    }

    public ShiftDto updateShift(ShiftRequestDto shiftDto) {
        try {
            Shift shift = shiftRepository.findById(shiftDto.getId()).orElseThrow(() -> new NotFoundException("Shift not found"));
            shiftDtoToShift(shiftDto, shift);

            shift.setModifiedBy(shiftDto.getCurrentUserId());
            shift.setModifiedDate(LocalDateTime.now());

            shift = shiftRepository.save(shift);

            log.info("Shift {} updated successfully", shift.getId());
            return shiftToShiftDto(shift);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    public ShiftDto deleteShift(long id) {
        try {
            Shift shift = shiftRepository.findById(id).orElseThrow(() -> new NotFoundException("Shift not found"));
            shiftRepository.delete(shift);

            log.info("Shift {} deleted successfully", shift.getId());
            return shiftToShiftDto(shift);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    private void shiftDtoToShift(ShiftRequestDto shiftDto, Shift shift) {
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
        shiftDto.setCreatedBy(shift.getCreatedBy());
        shiftDto.setModifiedBy(shift.getModifiedBy());
        shiftDto.setCreatedDate(shift.getCreatedDate());
        shiftDto.setModifiedDate(shift.getModifiedDate());

        return shiftDto;
    }

    public List<WeekShiftDto> getShiftsForWeek(LocalDate date, long restaurantId) {
        LocalDate endDate = date.plusDays(6);
        return shiftRepository.findShiftsByDateRangeAndRestaurant(date, endDate, restaurantId);
    }

    public List<AttendanceDetails> getAttendance(long id) {
        return shiftRepository.findShiftsByUserId(id);
    }
}
