INSERT INTO addresses (country, city, street, house_number, apartment_number, postcode)
VALUES ('Russia', 'Saint Petersburg', 'Nevsky Prospect', '15', '45', '342423');
INSERT INTO teachers(first_name, last_name, birthday, gender, address_id, phone_number, email, academic_degree)
VALUES ('Mike', 'Miller', '1977-05-13', 'MALE', 1, '5435345334', 'miller97@gmail.com', 'MASTER');
INSERT INTO courses (name)
VALUES ('History');
INSERT INTO classrooms (number, capacity)
VALUES (102, 30);
INSERT INTO times (start, ending)
VALUES ('8:00', '9:30');
INSERT INTO lessons (classroom_id, course_id, teacher_id, date, time_id)
VALUES (1, 1, 1, '2021-09-28', 1);
