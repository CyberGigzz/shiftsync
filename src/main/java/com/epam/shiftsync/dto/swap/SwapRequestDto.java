package com.epam.shiftsync.dto.swap;

import com.epam.shiftsync.dto.shift.ShiftDto;
import com.epam.shiftsync.dto.user.UserDto;
import java.time.LocalDateTime;

public record SwapRequestDto(
        Long id,
        UserDto requester,
        UserDto requestee,
        ShiftDto offeredShift,
        ShiftDto requestedShift,
        String status,
        LocalDateTime createdAt,
        LocalDateTime resolvedAt
) {}