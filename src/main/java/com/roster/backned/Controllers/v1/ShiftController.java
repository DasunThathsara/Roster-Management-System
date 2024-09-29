package com.roster.backned.Controllers.v1;

import com.roster.backned.Dto.AttendanceDetails;
import com.roster.backned.Dto.ShiftDto;
import com.roster.backned.Dto.WeekShiftDto;
import com.roster.backned.Service.ShiftService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shift")
@Tag(name = "Shift Controller")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@Slf4j
public class ShiftController {
    private final ShiftService shiftService;

    @PostMapping
    public ResponseEntity<ShiftDto> addShift(@Valid @RequestBody ShiftDto shiftDto) {
        return ResponseEntity.status(201).body(shiftService.createShift(shiftDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShiftDto> getShift(@PathVariable long id) {
        return ResponseEntity.ok().body(shiftService.getShift(id));
    }

    @GetMapping("/attendance/{id}")
    public ResponseEntity<List<AttendanceDetails>> getAttendance(@PathVariable long id) {
        return ResponseEntity.ok().body(shiftService.getAttendance(id));
    }

    @GetMapping
    public ResponseEntity<List<ShiftDto>> getAllShifts() {
        return ResponseEntity.ok().body(shiftService.getAllShifts());
    }

    @GetMapping("/{date}/{restaurant}")
    public ResponseEntity<List<WeekShiftDto>> getShiftsForWeek(@PathVariable LocalDate date, @PathVariable long restaurant) {
        return ResponseEntity.ok().body(shiftService.getShiftsForWeek(date, restaurant));
    }

    @PutMapping
    public ResponseEntity<ShiftDto> updateShift(@Valid @RequestBody ShiftDto shiftDto) {
        return ResponseEntity.ok().body(shiftService.updateShift(shiftDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ShiftDto> deleteShift(@PathVariable long id) {
        return ResponseEntity.ok().body(shiftService.deleteShift(id));
    }
}
