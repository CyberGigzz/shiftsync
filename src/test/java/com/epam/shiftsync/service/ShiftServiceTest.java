package com.epam.shiftsync.service;

import com.epam.shiftsync.dto.shift.ShiftRequest;
import com.epam.shiftsync.entity.Position;
import com.epam.shiftsync.entity.Shift;
import com.epam.shiftsync.entity.User;
import com.epam.shiftsync.exception.NotFoundException;
import com.epam.shiftsync.repository.PositionRepository;
import com.epam.shiftsync.repository.ShiftRepository;
import com.epam.shiftsync.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShiftServiceTest {

    @Mock
    private ShiftRepository shiftRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PositionRepository positionRepository;

    @InjectMocks
    private ShiftService shiftService;

    private User testUser;
    private Position testPosition;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName("Test");
        testUser.setLastName("User");

        testPosition = new Position();
        testPosition.setId(1L);
        testPosition.setPositionName("Barista");
    }

    @Test
    void createShift_shouldSucceed_whenUserAndPositionExist() {
        
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(8);
        ShiftRequest request = new ShiftRequest(testUser.getId(), testPosition.getId(), startTime, endTime);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        when(positionRepository.findById(1L)).thenReturn(Optional.of(testPosition));
        
        when(shiftRepository.save(any(Shift.class))).thenAnswer(invocation -> invocation.getArgument(0));

        shiftService.createShift(request);

        verify(shiftRepository).save(argThat(shift -> {
            assertNotNull(shift.getUser());
            assertEquals(testUser.getId(), shift.getUser().getId());
            assertNotNull(shift.getPosition());
            assertEquals(testPosition.getId(), shift.getPosition().getId());
            assertEquals(startTime, shift.getStartTime());
            return true;
        }));
    }


    @Test
    void createShift_shouldThrowNotFoundException_whenPositionDoesNotExist() {

        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = startTime.plusHours(8);

        long nonExistentPositionId = 99L;

        ShiftRequest request = new ShiftRequest(testUser.getId(), nonExistentPositionId, startTime, endTime);


        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            shiftService.createShift(request);
        });

        assertEquals("Position not found with id: " + nonExistentPositionId, exception.getMessage());

        verify(shiftRepository, never()).save(any(Shift.class));

        verify(userRepository, never()).findById(anyLong());
    }
}