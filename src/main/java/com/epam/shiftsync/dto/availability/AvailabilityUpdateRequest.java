package com.epam.shiftsync.dto.availability;

import java.util.List;

public record AvailabilityUpdateRequest(
    List<AvailabilityDto> availability
) {}