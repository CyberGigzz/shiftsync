package com.epam.shiftsync.dto.availability;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record AvailabilityDto(
        DayOfWeek dayOfWeek,
        
        LocalTime startTime,
        LocalTime endTime
) {}