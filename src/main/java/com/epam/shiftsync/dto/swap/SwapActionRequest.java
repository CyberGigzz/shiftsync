package com.epam.shiftsync.dto.swap;

import jakarta.validation.constraints.NotBlank;

public record SwapActionRequest(
        // The action can be "ACCEPT", "REJECT", "APPROVE", "DENY"
        @NotBlank(message = "Action is required")
        String action
) {}