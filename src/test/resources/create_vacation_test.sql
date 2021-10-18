INSERT INTO addresses (country, city, street, house_number, apartment_number, postcode) VALUES
  ('Russia', 'Saint Petersburg', 'Nevsky Prospect', '15', '45', '342423');
INSERT INTO teachers (first_name,last_name,birthday,gender,address_id,phone_number,email, academic_degree)
VALUES ('Mike', 'Miller', '1977-05-13', 'MALE', 1, '5435345334', 'miller97@gmail.com', 'MASTER');

INSERT INTO vacations (start, ending, teacher_id)
VALUES ('2021-10-15','2021-10-30',1);
INSERT INTO vacations (start, ending, teacher_id)
VALUES ('2021-05-15','2021-05-30', 1);
