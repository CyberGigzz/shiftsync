package com.epam.shiftsync.dto.swap;

import jakarta.validation.constraints.NotNull;

public record SwapCreateRequest(
        @NotNull(message = "Offered shift ID cannot be null")
        Long offeredShiftId,

        @NotNull(message = "Requested shift ID cannot be null")
        Long requestedShiftId
) {}