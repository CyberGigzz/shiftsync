package com.epam.shiftsync.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "SwapRequests")
public class SwapRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestee_id", nullable = false)
    private User requestee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offered_shift_id", nullable = false)
    private Shift offeredShift;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_shift_id", nullable = false)
    private Shift requestedShift;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}