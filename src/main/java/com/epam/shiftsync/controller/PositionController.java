// src/main/java/com/epam/shiftsync/controller/PositionController.java
package com.epam.shiftsync.controller;

import com.epam.shiftsync.dto.position.PositionDto;
import com.epam.shiftsync.dto.position.PositionRequest;
import com.epam.shiftsync.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/positions")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<PositionDto> createPosition(@RequestBody PositionRequest request) {
        PositionDto createdPosition = positionService.createPosition(request);
        return new ResponseEntity<>(createdPosition, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PositionDto>> getAllPositions() {
        List<PositionDto> positions = positionService.getAllPositions();
        return ResponseEntity.ok(positions);
    }
}