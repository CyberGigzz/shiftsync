package com.epam.shiftsync.dto.auth;

public record AuthResponse(
        String token,
        Long userId,
        String role
) {}