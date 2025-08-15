package com.epam.shiftsync.repository;

import com.epam.shiftsync.entity.Availability;
import com.epam.shiftsync.entity.AvailabilityId; 
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailabilityRepository extends JpaRepository<Availability, AvailabilityId> {

}