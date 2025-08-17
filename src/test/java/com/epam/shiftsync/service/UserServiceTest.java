package com.epam.shiftsync.service;

import com.epam.shiftsync.dto.availability.AvailabilityDto;
import com.epam.shiftsync.entity.Availability; 
import com.epam.shiftsync.entity.User;
import com.epam.shiftsync.repository.AvailabilityRepository;
import com.epam.shiftsync.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collection; 
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AvailabilityRepository availabilityRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void updateUserAvailability_shouldDeleteOldAndSaveNewAvailability() {

        long userId = 1L;
        User testUser = new User();
        testUser.setId(userId);

        List<AvailabilityDto> newAvailabilityRequest = List.of(
            new AvailabilityDto(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0)),
            new AvailabilityDto(DayOfWeek.TUESDAY, LocalTime.of(12, 0), LocalTime.of(18, 0))
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        when(availabilityRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        userService.updateUserAvailability(userId, newAvailabilityRequest);

        var inOrder = inOrder(availabilityRepository);

        inOrder.verify(availabilityRepository).deleteByUserId(userId);

        inOrder.verify(availabilityRepository).saveAll(argThat(
                (Collection<Availability> list) -> list.size() == 2
        ));
    }
}