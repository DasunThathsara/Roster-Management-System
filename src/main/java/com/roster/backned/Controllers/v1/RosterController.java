package com.roster.backned.Controllers.v1;

import com.roster.backned.Dto.RosterDto;
import com.roster.backned.Service.RosterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roster")
@Tag(name = "Roster Controller")
@RequiredArgsConstructor
@Validated
@Slf4j
public class RosterController {
    private final RosterService rosterService;

    @PostMapping
    public ResponseEntity<RosterDto> addRoster(@Valid @RequestBody RosterDto rosterDto) {
        return ResponseEntity.status(201).body(rosterService.createRoster(rosterDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RosterDto> getRoster(@PathVariable long id) {
        return ResponseEntity.ok().body(rosterService.getRoster(id));
    }

    @GetMapping
    public ResponseEntity<List<RosterDto>> getAllRosters() {
        return ResponseEntity.ok().body(rosterService.getAllRosters());
    }

    @PutMapping
    public ResponseEntity<RosterDto> updateRoster(@Valid @RequestBody RosterDto rosterDto) {
        return ResponseEntity.ok().body(rosterService.updateRoster(rosterDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RosterDto> deleteRoster(@PathVariable long id) {
        return ResponseEntity.ok().body(rosterService.deleteRoster(id));
    }
}
