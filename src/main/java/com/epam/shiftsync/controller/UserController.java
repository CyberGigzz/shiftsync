package com.epam.shiftsync.controller;

import com.epam.shiftsync.dto.availability.AvailabilityDto;
import com.epam.shiftsync.dto.availability.AvailabilityUpdateRequest;
import com.epam.shiftsync.dto.user.UserDto;
import com.epam.shiftsync.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {
        UserDto userDto = userService.getUserById(
                ((com.epam.shiftsync.security.UserDetailsImpl) userDetails).getId() 
        );
        return ResponseEntity.ok(userDto);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}/availability")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AvailabilityDto>> getUserAvailability(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserAvailability(userId));
    }

    @PutMapping("/me/availability")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AvailabilityDto>> updateUserAvailability(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
            @RequestBody AvailabilityUpdateRequest request
    ) {
        Long currentUserId = ((com.epam.shiftsync.security.UserDetailsImpl) userDetails).getId();
        List<AvailabilityDto> updatedAvailability = userService.updateUserAvailability(currentUserId, request.availability());
        return ResponseEntity.ok(updatedAvailability);
    }
}
