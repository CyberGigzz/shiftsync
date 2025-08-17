package com.epam.shiftsync.service;

import com.epam.shiftsync.dto.swap.SwapCreateRequest;
import com.epam.shiftsync.dto.swap.SwapRequestDto;
import com.epam.shiftsync.entity.Shift;
import com.epam.shiftsync.entity.SwapRequest;
import com.epam.shiftsync.entity.User;
import com.epam.shiftsync.exception.ForbiddenException;
import com.epam.shiftsync.exception.InvalidStateException;
import com.epam.shiftsync.exception.NotFoundException;
import com.epam.shiftsync.repository.ShiftRepository;
import com.epam.shiftsync.repository.SwapRequestRepository;
import com.epam.shiftsync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SwapService {

    private final SwapRequestRepository swapRequestRepository;
    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;
    
    private final ShiftService shiftService;
    private final UserService userService;

    @Transactional
    public SwapRequestDto createSwapRequest(SwapCreateRequest request, Long requesterId) {
        Shift offeredShift = shiftRepository.findById(request.offeredShiftId())
                .orElseThrow(() -> new NotFoundException("Offered shift not found"));
        Shift requestedShift = shiftRepository.findById(request.requestedShiftId())
                .orElseThrow(() -> new NotFoundException("Requested shift not found"));

        if (!offeredShift.getUser().getId().equals(requesterId)) {
            throw new ForbiddenException("You can only offer your own shifts for swapping.");
        }
        
        if (offeredShift.getUser().getId().equals(requestedShift.getUser().getId())) {
             throw new InvalidStateException("You cannot swap shifts with yourself.");
        }

        User requester = userRepository.findById(requesterId).orElseThrow();
        User requestee = requestedShift.getUser();

        SwapRequest swapRequest = new SwapRequest();
        swapRequest.setRequester(requester);
        swapRequest.setRequestee(requestee);
        swapRequest.setOfferedShift(offeredShift);
        swapRequest.setRequestedShift(requestedShift);
        swapRequest.setStatus("PENDING");
        swapRequest.setCreatedAt(LocalDateTime.now());

        SwapRequest savedRequest = swapRequestRepository.save(swapRequest);
        return convertToDto(savedRequest);
    }

    @Transactional
    public SwapRequestDto respondToSwap(Long requestId, Long currentUserId, String action) {
        SwapRequest request = swapRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Swap request not found"));

        if (!request.getRequestee().getId().equals(currentUserId)) {
            throw new ForbiddenException("You are not authorized to respond to this swap request.");
        }

        if (!"PENDING".equals(request.getStatus())) {
            throw new InvalidStateException("This swap request is no longer pending.");
        }

        if ("ACCEPT".equalsIgnoreCase(action)) {
            request.setStatus("ACCEPTED");
        } else if ("REJECT".equalsIgnoreCase(action)) {
            request.setStatus("REJECTED");
            request.setResolvedAt(LocalDateTime.now());
        } else {
            throw new IllegalArgumentException("Invalid action. Must be 'ACCEPT' or 'REJECT'.");
        }
        
        SwapRequest updatedRequest = swapRequestRepository.save(request);
        return convertToDto(updatedRequest);
    }

    @Transactional
    public SwapRequestDto approveSwap(Long requestId, String action) {
        SwapRequest request = swapRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Swap request not found"));

        if (!"ACCEPTED".equals(request.getStatus())) {
            throw new InvalidStateException("This swap request has not been accepted by the employee yet.");
        }

        if ("APPROVE".equalsIgnoreCase(action)) {
            request.setStatus("APPROVED");

            Shift offeredShift = request.getOfferedShift();
            Shift requestedShift = request.getRequestedShift();
            User requester = request.getRequester();
            User requestee = request.getRequestee();

            offeredShift.setUser(requestee);
            requestedShift.setUser(requester);

        } else if ("DENY".equalsIgnoreCase(action)) {
            request.setStatus("DENIED");
        } else {
            throw new IllegalArgumentException("Invalid action. Must be 'APPROVE' or 'DENY'.");
        }
        
        request.setResolvedAt(LocalDateTime.now());
        SwapRequest updatedRequest = swapRequestRepository.save(request);
        return convertToDto(updatedRequest);
    }

    public List<SwapRequestDto> getSwapsForUser(Long userId) {
        List<SwapRequest> swaps = swapRequestRepository.findSwapsInvolvingUser(userId);

        return swaps.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    private SwapRequestDto convertToDto(SwapRequest swapRequest) {
        return new SwapRequestDto(
                swapRequest.getId(),
                userService.convertToUserDto(swapRequest.getRequester()),
                userService.convertToUserDto(swapRequest.getRequestee()),
                shiftService.convertToDto(swapRequest.getOfferedShift()),
                shiftService.convertToDto(swapRequest.getRequestedShift()),
                swapRequest.getStatus(),
                swapRequest.getCreatedAt(),
                swapRequest.getResolvedAt()
        );
    }
}