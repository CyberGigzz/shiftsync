CREATE TABLE users (
    user_id         BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    email           VARCHAR(255) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    role            VARCHAR(50) NOT NULL CHECK (role IN ('MANAGER', 'EMPLOYEE')),
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE positions (
    position_id     BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    position_name   VARCHAR(100) NOT NULL UNIQUE,
    description     TEXT
);

CREATE TABLE user_positions (
    user_id         BIGINT NOT NULL,
    position_id     BIGINT NOT NULL,
    
    PRIMARY KEY (user_id, position_id),
    
    CONSTRAINT fk_user_positions_user
        FOREIGN KEY(user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_user_positions_position
        FOREIGN KEY(position_id) REFERENCES Positions(position_id) ON DELETE CASCADE
);

CREATE TABLE shifts (
    shift_id        BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id         BIGINT,
    position_id     BIGINT NOT NULL,
    start_time      TIMESTAMP NOT NULL,
    end_time        TIMESTAMP NOT NULL,
    
    CONSTRAINT fk_shift_user
        FOREIGN KEY(user_id) REFERENCES Users(user_id) ON DELETE SET NULL, 
    CONSTRAINT fk_shift_position
        FOREIGN KEY(position_id) REFERENCES Positions(position_id) ON DELETE CASCADE
);

CREATE TABLE availability (
    user_id         BIGINT NOT NULL,
    day_of_week     VARCHAR(10) NOT NULL CHECK (day_of_week IN ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY')),
    start_time      TIME,
    end_time        TIME,

    PRIMARY KEY (user_id, day_of_week),

    CONSTRAINT fk_availability_user
        FOREIGN KEY(user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

CREATE TABLE swapRequests (
    request_id          BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    requester_id        BIGINT NOT NULL,
    requestee_id        BIGINT NOT NULL,
    offered_shift_id    BIGINT NOT NULL,
    requested_shift_id  BIGINT NOT NULL,
    status              VARCHAR(50) NOT NULL CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED', 'APPROVED', 'DENIED')),
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved_at         TIMESTAMP, 

    CONSTRAINT fk_swap_requester
        FOREIGN KEY(requester_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_swap_requestee
        FOREIGN KEY(requestee_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_swap_offered_shift
        FOREIGN KEY(offered_shift_id) REFERENCES Shifts(shift_id) ON DELETE CASCADE,
    CONSTRAINT fk_swap_requested_shift
        FOREIGN KEY(requested_shift_id) REFERENCES Shifts(shift_id) ON DELETE CASCADE
);