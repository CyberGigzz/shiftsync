package com.epam.shiftsync.dto.position;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PositionRequest(
        @NotBlank(message = "Position name is required")
        @Size(min = 2, max = 100, message = "Position name must be between 2 and 100 characters")
        String positionName,

        String description
) {}