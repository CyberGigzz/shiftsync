package com.epam.shiftsync.controller;

import com.epam.shiftsync.dto.shift.ShiftDto;
import com.epam.shiftsync.dto.shift.ShiftRequest;
import com.epam.shiftsync.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<ShiftDto> createShift(@RequestBody ShiftRequest request) {
        ShiftDto createdShift = shiftService.createShift(request);
        return new ResponseEntity<>(createdShift, HttpStatus.CREATED);
    }


    @GetMapping
    @PreAuthorize("isAuthenticated()") 
    public ResponseEntity<List<ShiftDto>> getShiftsInRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        List<ShiftDto> shifts = shiftService.getShifts(start, end);
        return ResponseEntity.ok(shifts);
    }


    @PutMapping("/{shiftId}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<ShiftDto> updateShift(
            @PathVariable Long shiftId,
            @RequestBody ShiftRequest request
    ) {
        ShiftDto updatedShift = shiftService.updateShift(shiftId, request);
        return ResponseEntity.ok(updatedShift);
    }


    @DeleteMapping("/{shiftId}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Void> deleteShift(@PathVariable Long shiftId) {
        shiftService.deleteShift(shiftId);
        return ResponseEntity.noContent().build();
    }
}