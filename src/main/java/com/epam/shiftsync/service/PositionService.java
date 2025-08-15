package com.epam.shiftsync.service;

import com.epam.shiftsync.dto.position.PositionDto;
import com.epam.shiftsync.dto.position.PositionRequest;
import com.epam.shiftsync.entity.Position;
import com.epam.shiftsync.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;

    public PositionDto createPosition(PositionRequest request) {
        Position newPosition = new Position();
        newPosition.setPositionName(request.positionName());
        newPosition.setDescription(request.description());

        Position savedPosition = positionRepository.save(newPosition);

        return convertToDto(savedPosition);
    }

    public List<PositionDto> getAllPositions() {
        return positionRepository.findAll()
                .stream()
                .map(this::convertToDto) 
                .collect(Collectors.toList());
    }

    private PositionDto convertToDto(Position position) {
        return new PositionDto(
                position.getId(),
                position.getPositionName(),
                position.getDescription()
        );
    }
}