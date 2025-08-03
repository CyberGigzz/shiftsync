// src/main/java/com/epam/shiftsync/entity/Availability.java
package com.epam.shiftsync.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Availability")
public class Availability {

    @EmbeddedId // Use the external composite key class
    private AvailabilityId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId") // This is crucial. It tells JPA that the 'userId' field in our 'AvailabilityId' class maps to this User entity.
    @JoinColumn(name = "user_id")
    private User user;

    // The 'dayOfWeek' part of the ID is just a basic column in the ID class,
    // so we don't need a @MapsId for it here.

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;
}