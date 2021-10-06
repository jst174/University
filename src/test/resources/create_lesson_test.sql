INSERT INTO addresses (country, city, street, house_number, apartment_number, postcode)
VALUES ('Russia', 'Saint Petersburg', 'Nevsky Prospect', '15', '45', '342423');
INSERT INTO teachers(first_name, last_name, birthday, gender, address_id, phone_number, email, academic_degree)
VALUES ('Mike', 'Miller', '1977-05-13', 'MALE', 1, '5435345334', 'miller77@gmail.com', 'MASTER');
INSERT INTO teachers(first_name, last_name, birthday, gender, address_id, phone_number, email, academic_degree)
VALUES ('Bob', 'King', '1965-11-21', 'MALE', 1, '5345345', 'king65@gmail.com', 'DOCTORAL');
INSERT INTO courses (name)
VALUES ('History');
INSERT INTO courses (name)
VALUES ('Physics');
INSERT INTO classrooms (number, capacity)
VALUES (102, 30);
INSERT INTO classrooms (number, capacity)
VALUES (201, 60);
INSERT INTO times (start, ending)
VALUES ('8:00', '9:30');
INSERT INTO times (start, ending)
VALUES ('12:00', '13:30');
INSERT INTO lessons (classroom_id, course_id, teacher_id, date, time_id)
VALUES (1, 1, 1, '2021-09-28', 1);

INSERT INTO groups (name) VALUES ('MH-12');
INSERT INTO groups (name) VALUES ('JW-23');
INSERT INTO groups (name) VALUES ('MG-54');

