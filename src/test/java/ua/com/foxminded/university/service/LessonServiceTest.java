package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.DataSource;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.VacationDao;
import ua.com.foxminded.university.model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LessonServiceTest {

    @Mock
    private LessonDao lessonDao;
    @Mock
    private TeacherDao teacherDao;
    @Mock
    private VacationDao vacationDao;
    @Mock
    private CourseDao courseDao;
    @InjectMocks
    private LessonService lessonService;
    private List<Vacation> vacations;
    private List<Course> courses;
    private List<Lesson> lessons;
    private List<Classroom> classrooms;
    private List<Teacher> teachers;
    private List<Time> times;
    private DataSource dataSource;

    @BeforeEach
    public void setUp() throws IOException {
        dataSource = new DataSource();
        vacations = new ArrayList<>();
        teachers = new ArrayList<>();
        Teacher teacher = dataSource.generateTeacher();
        teacher.setId(1);
        teachers.add(teacher);
        Vacation vacation1 = new Vacation(
            LocalDate.of(2021, 11, 5),
            LocalDate.of(2021, 11, 30),
            teacher);
        vacation1.setId(1);
        Vacation vacation2 = new Vacation(
            LocalDate.of(2021, 5, 5),
            LocalDate.of(2021, 5, 30),
            teacher
        );
        vacation2.setId(1);
        vacations.add(vacation1);
        vacations.add(vacation2);
        courses = new ArrayList<>();
        Course course1 = new Course("Math");
        course1.setId(1);
        Course course2 = new Course("Physics");
        course2.setId(2);
        courses.add(course1);
        courses.add(course2);

        classrooms = new ArrayList<>();
        Classroom classroom1 = new Classroom(101, 30);
        classroom1.setId(1);
        Classroom classroom2 = new Classroom(120, 60);
        classroom2.setId(2);
        classrooms.add(classroom1);
        classrooms.add(classroom2);

        times = new ArrayList<>();
        Time time1 = new Time(LocalTime.of(8, 0), LocalTime.of(9, 30));
        time1.setId(1);
        Time time2 = new Time(LocalTime.of(12, 0), LocalTime.of(13, 30));
        time2.setId(2);
        times.add(time1);
        times.add(time2);

        lessons = new ArrayList<>();
        Lesson lesson1 = new Lesson(
            course1,
            classroom2,
            teacher,
            LocalDate.of(2021, 10, 26),
            time1);
        lesson1.setId(1);

        Lesson lesson2 = new Lesson(
            course2,
            classroom2,
            teacher,
            LocalDate.of(2021, 10, 26),
            time2
        );
        lesson2.setId(2);
        lessons.add(lesson1);
        lessons.add(lesson2);
    }

    @Test
    public void test() {

    }
}
