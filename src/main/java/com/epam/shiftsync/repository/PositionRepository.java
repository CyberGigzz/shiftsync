package com.epam.shiftsync.repository;

import com.epam.shiftsync.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {

}