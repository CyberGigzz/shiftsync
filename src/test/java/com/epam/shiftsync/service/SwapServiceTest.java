package com.epam.shiftsync.service;

import com.epam.shiftsync.dto.swap.SwapCreateRequest;
import com.epam.shiftsync.entity.Shift;
import com.epam.shiftsync.entity.SwapRequest;
import com.epam.shiftsync.entity.User;
import com.epam.shiftsync.exception.ForbiddenException;
import com.epam.shiftsync.exception.InvalidStateException;
import com.epam.shiftsync.repository.ShiftRepository;
import com.epam.shiftsync.repository.SwapRequestRepository;
import com.epam.shiftsync.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SwapServiceTest {

    @Mock
    private SwapRequestRepository swapRequestRepository;
    @Mock
    private ShiftRepository shiftRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ShiftService shiftService; 
    @Mock
    private UserService userService; 

    @InjectMocks
    private SwapService swapService;

    private User requester;
    private User requestee;
    private Shift offeredShift;
    private Shift requestedShift;

    @BeforeEach
    void setUp() {
        requester = new User();
        requester.setId(1L);
        requester.setFirstName("Requester");

        requestee = new User();
        requestee.setId(2L);
        requestee.setFirstName("Requestee");

        offeredShift = new Shift();
        offeredShift.setId(101L);
        offeredShift.setUser(requester); 

        requestedShift = new Shift();
        requestedShift.setId(102L);
        requestedShift.setUser(requestee); 
    }

    @Test
    void createSwapRequest_shouldThrowForbiddenException_whenUserOffersAnothersShift() {

        User maliciousUser = new User();
        maliciousUser.setId(3L);
        long maliciousUserId = maliciousUser.getId();

        SwapCreateRequest request = new SwapCreateRequest(offeredShift.getId(), requestedShift.getId());

        when(shiftRepository.findById(offeredShift.getId())).thenReturn(Optional.of(offeredShift));
        when(shiftRepository.findById(requestedShift.getId())).thenReturn(Optional.of(requestedShift));

        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> {
            swapService.createSwapRequest(request, maliciousUserId);
        });

        assertEquals("You can only offer your own shifts for swapping.", exception.getMessage());
        verify(swapRequestRepository, never()).save(any(SwapRequest.class));
    }

    @Test
    void approveSwap_shouldSwapUsersOnShifts_whenRequestIsValid() {
        
        SwapRequest acceptedRequest = new SwapRequest();
        acceptedRequest.setId(1L);
        acceptedRequest.setRequester(requester);
        acceptedRequest.setRequestee(requestee);
        acceptedRequest.setOfferedShift(offeredShift);
        acceptedRequest.setRequestedShift(requestedShift);
        acceptedRequest.setStatus("ACCEPTED");

        when(swapRequestRepository.findById(1L)).thenReturn(Optional.of(acceptedRequest));
        when(swapRequestRepository.save(any(SwapRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(userService.convertToUserDto(any(User.class))).thenReturn(null);
        when(shiftService.convertToDto(any(Shift.class))).thenReturn(null);
        
        swapService.approveSwap(1L, "APPROVE");

        assertEquals(requestee.getId(), offeredShift.getUser().getId(), "Requestee should now own the offered shift");
        assertEquals(requester.getId(), requestedShift.getUser().getId(), "Requester should now own the requested shift");

        verify(swapRequestRepository).save(argThat(savedRequest ->
            "APPROVED".equals(savedRequest.getStatus()) &&
            savedRequest.getResolvedAt() != null
        ));
    }


    @Test
    void createSwapRequest_shouldThrowInvalidStateException_whenUserSwapsWithThemselves() {

        Shift anotherShiftOwnedByRequester = new Shift();
        anotherShiftOwnedByRequester.setId(103L);
        anotherShiftOwnedByRequester.setUser(requester); 

        SwapCreateRequest selfSwapRequest = new SwapCreateRequest(offeredShift.getId(), anotherShiftOwnedByRequester.getId());

        when(shiftRepository.findById(offeredShift.getId())).thenReturn(Optional.of(offeredShift));
        when(shiftRepository.findById(anotherShiftOwnedByRequester.getId())).thenReturn(Optional.of(anotherShiftOwnedByRequester));

        InvalidStateException exception = assertThrows(InvalidStateException.class, () -> {
            swapService.createSwapRequest(selfSwapRequest, requester.getId());
        });

        assertEquals("You cannot swap shifts with yourself.", exception.getMessage());
        verify(swapRequestRepository, never()).save(any(SwapRequest.class));
    }

    @Test
    void approveSwap_shouldThrowInvalidStateException_whenRequestIsNotAccepted() {

        SwapRequest pendingRequest = new SwapRequest();
        pendingRequest.setId(2L);
        pendingRequest.setRequester(requester);
        pendingRequest.setRequestee(requestee);
        pendingRequest.setOfferedShift(offeredShift);
        pendingRequest.setRequestedShift(requestedShift);
        pendingRequest.setStatus("PENDING"); 

        when(swapRequestRepository.findById(2L)).thenReturn(Optional.of(pendingRequest));

        InvalidStateException exception = assertThrows(InvalidStateException.class, () -> {
            swapService.approveSwap(2L, "APPROVE");
        });

        assertEquals("This swap request has not been accepted by the employee yet.", exception.getMessage());
        
        
        assertEquals(requester, offeredShift.getUser());
        assertEquals(requestee, requestedShift.getUser());
        
        verify(swapRequestRepository, never()).save(any(SwapRequest.class));
    }
}