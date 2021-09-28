drop table if exists addresses cascade;
create table addresses
(
  address_id       serial primary key not null,
  country          varchar,
  city             varchar,
  street           varchar,
  house_number     varchar,
  apartment_number varchar,
  postcode         varchar
);
drop table if exists groups cascade;
create table groups
(
  group_id   serial primary key not null,
  group_name varchar
);
drop table if exists students cascade;
create table students
(
  student_id   serial primary key not null,
  first_name   varchar,
  last_name    varchar,
  birthday     date,
  gender       varchar,
  address_id   integer,
  phone_number varchar,
  email        varchar,
  group_id     integer,
  foreign key (address_id) references addresses (address_id) on delete set null,
  foreign key (group_id) references groups (group_id) on delete set null
);
drop table if exists vacations cascade;
create table vacations
(
  vacation_id    serial primary key not null,
  start_vacation date,
  end_vacation   date
);

drop table if exists teachers cascade;
create table teachers
(
  teacher_id      serial primary key not null,
  first_name      varchar,
  last_name       varchar,
  birthday        date,
  gender          varchar,
  address_id      integer,
  phone_number    varchar,
  email           varchar,
  academic_degree varchar,
  vacation_id     integer,
  foreign key (address_id) references addresses (address_id) on delete set null,
  foreign key (vacation_id) references vacations (vacation_id)
);





drop table if exists courses cascade;
create table courses
(
  course_id   serial primary key not null,
  course_name varchar
);
drop table if exists classrooms cascade;
create table classrooms
(
  classroom_id serial primary key not null,
  number       integer,
  capacity     integer
);

drop table if exists time cascade;
create table time
(
  time_id    serial primary key not null,
  start_time time,
  end_time   time
);

drop table if exists holidays cascade;
create table holidays
(
  holiday_id   serial primary key not null,
  holiday_name varchar,
  holiday_date date
);

drop table if exists lessons cascade;
create table lessons
(
  lesson_id    serial primary key not null,
  classroom_id integer references classrooms (classroom_id) on update cascade on delete cascade,
  course_id    integer references courses (course_id) on update cascade on delete cascade,
  teacher_id   integer references teachers (teacher_id) on update cascade on delete cascade,
  lesson_date  date,
  time_id  integer references time(time_id) on update cascade on delete cascade
);

drop table if exists teachers_vacations cascade;
create table teachers_vacations
(
  teacher_id  integer references teachers (teacher_id) on update cascade on delete cascade,
  vacation_id integer references vacations (vacation_id) on update cascade on delete cascade,
  unique (teacher_id, vacation_id)
);
drop table if exists teachers_courses cascade;
create table teachers_courses
(
  teacher_id  integer references teachers (teacher_id) on update cascade on delete cascade,
  course_id integer references courses (course_id) on update cascade on delete cascade,
  unique (teacher_id, course_id)
);
drop table if exists group_students cascade;
create table group_students
(
  group_id   integer references groups (group_id) on update cascade on delete cascade,
  student_id integer references students (student_id) on update cascade on delete cascade,
  unique (group_id, student_id)
);





