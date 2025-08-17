INSERT INTO users (first_name, last_name, email, password_hash, role) VALUES
('Admin', 'Manager', 'manager@example.com', '$2a$10$A8aMzbchkp.YaLeDTe7GwOtezVbl6CAtQB6YjAPoBBYQZ8AW2e2Pu', 'MANAGER'),
('Jane', 'Doe', 'jane.doe@example.com', '$2a$10$A8aMzbchkp.YaLeDTe7GwOtezVbl6CAtQB6YjAPoBBYQZ8AW2e2Pu', 'EMPLOYEE'),
('John', 'Smith', 'john.smith@example.com', '$2a$10$A8aMzbchkp.YaLeDTe7GwOtezVbl6CAtQB6YjAPoBBYQZ8AW2e2Pu', 'EMPLOYEE'),
('Emily', 'Jones', 'emily.jones@example.com', '$2a$10$A8aMzbchkp.YaLeDTe7GwOtezVbl6CAtQB6YjAPoBBYQZ8AW2e2Pu', 'EMPLOYEE');

INSERT INTO positions (position_name, description) VALUES
('Barista', 'Prepares and serves coffee and other beverages.'),
('Cashier', 'Handles customer payments and front-of-house interactions.'),
('Shift Lead', 'Supervises staff during a shift, handles escalations.'),
('Cook', 'Prepares food items according to the menu.');


INSERT INTO user_positions (user_id, position_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), 
(2, 1), (2, 2),                  
(3, 4),                          
(4, 2);                          

INSERT INTO availability (user_id, day_of_week, start_time, end_time) VALUES
(2, 'MONDAY', '08:00:00', '18:00:00'),   
(2, 'TUESDAY', '08:00:00', '14:00:00'),  
(3, 'WEDNESDAY', NULL, NULL),           
(4, 'MONDAY', '09:00:00', '17:00:00'),  
(4, 'FRIDAY', '12:00:00', '20:00:00');  

DO $$
DECLARE
    next_monday date := date_trunc('week', now()) + interval '7 days';
BEGIN
    INSERT INTO shifts (user_id, position_id, start_time, end_time) VALUES
    (2, 1, next_monday + time '09:00:00', next_monday + time '17:00:00');

    INSERT INTO shifts (user_id, position_id, start_time, end_time) VALUES
    (3, 4, next_monday + time '12:00:00', next_monday + time '20:00:00');

    INSERT INTO shifts (user_id, position_id, start_time, end_time) VALUES
    (4, 2, (next_monday + interval '1 day') + time '09:00:00', (next_monday + interval '1 day') + time '17:00:00');

    INSERT INTO shifts (user_id, position_id, start_time, end_time) VALUES
    (2, 2, (next_monday + interval '2 days') + time '08:00:00', (next_monday + interval '2 days') + time '16:00:00');

    INSERT INTO shifts (user_id, position_id, start_time, end_time) VALUES
    (NULL, 3, (next_monday + interval '4 days') + time '10:00:00', (next_monday + interval '4 days') + time '18:00:00');

END $$;