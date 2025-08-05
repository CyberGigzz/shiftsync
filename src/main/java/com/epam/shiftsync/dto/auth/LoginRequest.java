package com.epam.shiftsync.dto.auth;

public record LoginRequest(
        String email,
        String password
) {}