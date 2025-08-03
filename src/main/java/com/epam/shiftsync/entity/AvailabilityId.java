package com.epam.shiftsync.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data // Lombok for getters, setters, hashCode, and equals
@NoArgsConstructor
@Embeddable // Mark this as a class that can be embedded as an ID
public class AvailabilityId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "day_of_week")
    private String dayOfWeek;

    // You can add a constructor for convenience if needed
    public AvailabilityId(Long userId, String dayOfWeek) {
        this.userId = userId;
        this.dayOfWeek = dayOfWeek;
    }
}