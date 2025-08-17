package com.epam.shiftsync.dto.user;

public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String role
) {}