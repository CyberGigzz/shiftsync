package com.epam.shiftsync.controller;

import com.epam.shiftsync.dto.swap.SwapActionRequest;
import com.epam.shiftsync.dto.swap.SwapCreateRequest;
import com.epam.shiftsync.dto.swap.SwapRequestDto;
import com.epam.shiftsync.security.UserDetailsImpl;
import com.epam.shiftsync.service.SwapService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/swaps")
@RequiredArgsConstructor
public class SwapController {

    private final SwapService swapService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SwapRequestDto> createSwapRequest(
            @RequestBody SwapCreateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        SwapRequestDto createdSwap = swapService.createSwapRequest(request, userDetails.getId());
        return new ResponseEntity<>(createdSwap, HttpStatus.CREATED);
    }

    @PostMapping("/{requestId}/respond")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<SwapRequestDto> respondToSwapRequest(
            @PathVariable Long requestId,
            @RequestBody SwapActionRequest actionRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        SwapRequestDto updatedSwap = swapService.respondToSwap(requestId, userDetails.getId(), actionRequest.action());
        return ResponseEntity.ok(updatedSwap);
    }


    @PostMapping("/{requestId}/approve")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<SwapRequestDto> approveSwapRequest(
            @PathVariable Long requestId,
            @RequestBody SwapActionRequest actionRequest
    ) {
        SwapRequestDto updatedSwap = swapService.approveSwap(requestId, actionRequest.action());
        return ResponseEntity.ok(updatedSwap);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SwapRequestDto>> getMySwapRequests(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<SwapRequestDto> swaps = swapService.getSwapsForUser(userDetails.getId());
        return ResponseEntity.ok(swaps);
    }

}