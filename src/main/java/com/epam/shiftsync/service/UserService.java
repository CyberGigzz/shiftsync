package com.epam.shiftsync.service;

import com.epam.shiftsync.dto.availability.AvailabilityDto;
import com.epam.shiftsync.dto.user.UserDto;
import com.epam.shiftsync.entity.Availability;
import com.epam.shiftsync.entity.AvailabilityId;
import com.epam.shiftsync.entity.User;
import com.epam.shiftsync.exception.NotFoundException;
import com.epam.shiftsync.repository.AvailabilityRepository;
import com.epam.shiftsync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AvailabilityRepository availabilityRepository;

    public UserDto getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(this::convertToUserDto)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
    }

    public List<AvailabilityDto> getUserAvailability(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with id: " + userId);
        }
        
        return availabilityRepository.findByUserId(userId)
                .stream()
                .map(this::convertToAvailabilityDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<AvailabilityDto> updateUserAvailability(Long userId, List<AvailabilityDto> availabilityDtos) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        availabilityRepository.deleteByUserId(userId);

        List<Availability> newAvailabilities = availabilityDtos.stream()
                .map(dto -> {
                    Availability availability = new Availability();
                    availability.setId(new AvailabilityId(user.getId(), dto.dayOfWeek().toString()));
                    availability.setUser(user);
                    availability.setStartTime(dto.startTime());
                    availability.setEndTime(dto.endTime());
                    return availability;
                })
                .collect(Collectors.toList());

        List<Availability> savedAvailabilities = availabilityRepository.saveAll(newAvailabilities);

        return savedAvailabilities.stream()
                .map(this::convertToAvailabilityDto)
                .collect(Collectors.toList());
    }
    
    private UserDto convertToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole()
        );
    }

    private AvailabilityDto convertToAvailabilityDto(Availability availability) {
        return new AvailabilityDto(
                java.time.DayOfWeek.valueOf(availability.getId().getDayOfWeek()),
                availability.getStartTime(),
                availability.getEndTime()
        );
    }
}