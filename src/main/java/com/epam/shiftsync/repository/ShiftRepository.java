package com.epam.shiftsync.repository;

import com.epam.shiftsync.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

}