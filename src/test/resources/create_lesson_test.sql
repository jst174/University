insert into addresses (country, city, street, house_number, apartment_number, postcode) values
  ('Russia', 'Saint Petersburg', 'Nevsky Prospect', '15', '45', '342423');
INSERT INTO teachers(first_name,last_name,birthday,gender,address_id,phone_number,email, academic_degree)
values ('Mike','Miller','1977-05-13','MALE',1,'5435345334','miller97@gmail.com', 'MASTER');
insert into courses (course_name) values ('History');
insert  into  classrooms (number, capacity) values (102, 30);
insert into time (start_time, end_time) values ('8:00','9:30');
insert into lessons (classroom_id, course_id, teacher_id, lesson_date, time_id)
values (1,1,1,'2021-09-28',1);
