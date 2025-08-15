package com.epam.shiftsync.dto.position;

public record PositionDto(
        Long id,
        String positionName,
        String description
) {}