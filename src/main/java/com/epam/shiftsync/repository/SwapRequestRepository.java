package com.epam.shiftsync.repository;

import com.epam.shiftsync.entity.SwapRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwapRequestRepository extends JpaRepository<SwapRequest, Long> {

}