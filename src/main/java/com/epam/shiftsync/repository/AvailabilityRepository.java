package com.epam.shiftsync.repository;

import com.epam.shiftsync.entity.Availability;
import com.epam.shiftsync.entity.AvailabilityId;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailabilityRepository extends JpaRepository<Availability, AvailabilityId> {

    List<Availability> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}