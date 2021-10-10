DROP TABLE IF EXISTS addresses CASCADE;
create table addresses
(
  id               SERIAL PRIMARY KEY NOT NULL,
  country          VARCHAR,
  city             VARCHAR,
  street           VARCHAR,
  house_number     VARCHAR,
  apartment_number VARCHAR,
  postcode         VARCHAR
);

DROP TABLE IF EXISTS groups CASCADE;
CREATE TABLE groups
(
  id   SERIAL PRIMARY KEY NOT NULL,
  name VARCHAR
);

DROP TABLE IF EXISTS courses CASCADE;
CREATE TABLE courses
(
  id   SERIAL PRIMARY KEY NOT NULL,
  name VARCHAR
);

DROP TABLE IF EXISTS students CASCADE;
CREATE TABLE students
(
  id           SERIAL PRIMARY KEY NOT NULL,
  first_name   VARCHAR,
  last_name    VARCHAR,
  birthday     DATE,
  gender       VARCHAR,
  address_id   INTEGER,
  phone_number VARCHAR,
  email        VARCHAR,
  group_id     INTEGER,
  FOREIGN KEY (address_id) REFERENCES addresses (id) ON DELETE SET NULL,
  FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE SET NULL
);

DROP TABLE IF EXISTS teachers CASCADE;
CREATE TABLE teachers
(
  id              SERIAL PRIMARY KEY NOT NULL,
  first_name      VARCHAR,
  last_name       VARCHAR,
  birthday        DATE,
  gender          VARCHAR,
  address_id      INTEGER,
  phone_number    VARCHAR,
  email           VARCHAR,
  academic_degree VARCHAR,
  FOREIGN KEY (address_id) REFERENCES addresses (id) ON DELETE SET NULL
);

DROP TABLE IF EXISTS vacations CASCADE;
CREATE TABLE vacations
(
  id         SERIAL PRIMARY KEY NOT NULL,
  start      DATE,
  ending     DATE,
  teacher_id INTEGER REFERENCES teachers (id) ON UPDATE CASCADE ON DELETE CASCADE
);

DROP TABLE IF EXISTS classrooms CASCADE;
CREATE TABLE classrooms
(
  id       SERIAL PRIMARY KEY NOT NULL,
  number   INTEGER,
  capacity INTEGER
);

DROP TABLE IF EXISTS times CASCADE;
CREATE TABLE times
(
  id    SERIAL PRIMARY KEY NOT NULL,
  start TIME,
  ending   TIME
);

DROP TABLE IF EXISTS holidays CASCADE;
CREATE TABLE holidays
(
  id   SERIAL PRIMARY KEY NOT NULL,
  name VARCHAR,
  date DATE
);

DROP TABLE IF EXISTS lessons CASCADE;
CREATE TABLE lessons
(
  id           SERIAL PRIMARY KEY NOT NULL,
  classroom_id INTEGER REFERENCES classrooms (id) ON UPDATE CASCADE ON DELETE CASCADE,
  course_id    INTEGER REFERENCES courses (id) ON UPDATE CASCADE ON DELETE CASCADE,
  teacher_id   INTEGER REFERENCES teachers (id) ON UPDATE CASCADE ON DELETE CASCADE,
  date         DATE,
  time_id      INTEGER REFERENCES times (id) ON UPDATE CASCADE ON DELETE CASCADE
);

DROP TABLE IF EXISTS teachers_courses CASCADE;
CREATE TABLE teachers_courses
(
  teacher_id INTEGER REFERENCES teachers (id) ON UPDATE CASCADE ON DELETE CASCADE,
  course_id  INTEGER REFERENCES courses (id) ON UPDATE CASCADE ON DELETE CASCADE,
  UNIQUE (teacher_id, course_id)
);

DROP TABLE IF EXISTS lessons_groups CASCADE;
CREATE TABLE lessons_groups
(
  lesson_id INTEGER REFERENCES lessons (id) ON UPDATE CASCADE ON DELETE CASCADE,
  group_id  INTEGER REFERENCES groups (id) ON UPDATE CASCADE ON DELETE CASCADE,
  UNIQUE (lesson_id, group_id)
)





