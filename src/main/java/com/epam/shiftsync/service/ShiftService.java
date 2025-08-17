package com.epam.shiftsync.service;

import com.epam.shiftsync.dto.shift.ShiftDto;
import com.epam.shiftsync.dto.shift.ShiftRequest;
import com.epam.shiftsync.dto.user.UserDto;
import com.epam.shiftsync.dto.position.PositionDto;
import com.epam.shiftsync.entity.Position;
import com.epam.shiftsync.entity.Shift;
import com.epam.shiftsync.entity.User;
import com.epam.shiftsync.exception.NotFoundException; 
import com.epam.shiftsync.repository.PositionRepository;
import com.epam.shiftsync.repository.ShiftRepository;
import com.epam.shiftsync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;
    private final PositionRepository positionRepository;

    @Transactional
    public ShiftDto createShift(ShiftRequest request) {
        Position position = positionRepository.findById(request.positionId())
                .orElseThrow(() -> new NotFoundException("Position not found with id: " + request.positionId()));

        User user = null;
        if (request.userId() != null) {
            user = userRepository.findById(request.userId())
                    .orElseThrow(() -> new NotFoundException("User not found with id: " + request.userId()));
        }

        Shift newShift = new Shift();
        newShift.setUser(user);
        newShift.setPosition(position);
        newShift.setStartTime(request.startTime());
        newShift.setEndTime(request.endTime());

        Shift savedShift = shiftRepository.save(newShift);
        return convertToDto(savedShift);
    }

    public List<ShiftDto> getShifts(LocalDateTime start, LocalDateTime end) {
        return shiftRepository.findAll() 
                .stream()
                .filter(shift -> !shift.getStartTime().isBefore(start) && !shift.getEndTime().isAfter(end))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ShiftDto convertToDto(Shift shift) {
        User user = shift.getUser();
        UserDto userDto = null;
        if (user != null) {
            userDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole());
        }

        Position position = shift.getPosition();
        PositionDto positionDto = new PositionDto(position.getId(), position.getPositionName(), position.getDescription());

        return new ShiftDto(shift.getId(), shift.getStartTime(), shift.getEndTime(), userDto, positionDto);
    }


    @Transactional
    public ShiftDto updateShift(Long shiftId, ShiftRequest request) {
        Shift existingShift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new NotFoundException("Shift not found with id: " + shiftId));

        Position position = positionRepository.findById(request.positionId())
                .orElseThrow(() -> new NotFoundException("Position not found with id: " + request.positionId()));

        User user = null;
        if (request.userId() != null) {
            user = userRepository.findById(request.userId())
                    .orElseThrow(() -> new NotFoundException("User not found with id: " + request.userId()));
        }

        existingShift.setUser(user); 
        existingShift.setPosition(position);
        existingShift.setStartTime(request.startTime());
        existingShift.setEndTime(request.endTime());

        Shift updatedShift = shiftRepository.save(existingShift);
        
        return convertToDto(updatedShift);
    }


    public void deleteShift(Long shiftId) {
        if (!shiftRepository.existsById(shiftId)) {
            throw new NotFoundException("Shift not found with id: " + shiftId);
        }
        shiftRepository.deleteById(shiftId);
    }
}