package com.epam.shiftsync.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data 
@NoArgsConstructor
@Embeddable 
public class AvailabilityId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "day_of_week")
    private String dayOfWeek;

    public AvailabilityId(Long userId, String dayOfWeek) {
        this.userId = userId;
        this.dayOfWeek = dayOfWeek;
    }
}