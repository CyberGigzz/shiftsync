package com.epam.shiftsync.dto.shift;

import com.epam.shiftsync.dto.position.PositionDto;
import com.epam.shiftsync.dto.user.UserDto; 

import java.time.LocalDateTime;

public record ShiftDto(
        Long id,
        LocalDateTime startTime,
        LocalDateTime endTime,
        UserDto user, 
        PositionDto position 
) {}