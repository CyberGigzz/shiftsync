package com.epam.shiftsync.repository;

import com.epam.shiftsync.entity.SwapRequest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SwapRequestRepository extends JpaRepository<SwapRequest, Long> {

    @Query("SELECT sr FROM SwapRequest sr WHERE sr.requester.id = :userId OR sr.requestee.id = :userId ORDER BY sr.createdAt DESC")
    List<SwapRequest> findSwapsInvolvingUser(@Param("userId") Long userId);
}