package ua.com.foxminded.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.DataSource;
import ua.com.foxminded.university.dao.*;
import ua.com.foxminded.university.model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
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
    @Mock
    private GroupDao groupDao;
    @Mock
    private HolidayDao holidayDao;
    @InjectMocks
    private LessonService lessonService;
    private List<Lesson> lessons;
    private List<Teacher> teachers;
    private List<Vacation> vacations;
    private List<Course> courses;
    private List<Group> groups;
    private List<Holiday> holidays;
    private DataSource dataSource;

    @BeforeEach
    public void setUp() throws IOException {
        dataSource = new DataSource();
        teachers = getTeachers();
        vacations = getVacations(teachers);
        courses = getCourses();
        groups = getGroups();
        holidays = getHolidays();


        lessons = new ArrayList<>();
        Lesson lesson1 = new Lesson(
            getCourses().get(0),
            getClassrooms().get(0),
            getTeachers().get(0),
            LocalDate.of(2021, 10, 26),
            getTimes().get(0));
        lesson1.setId(1);
        lesson1.setGroups(getGroups());
        Lesson lesson2 = new Lesson(
            getCourses().get(1),
            getClassrooms().get(1),
            getTeachers().get(0),
            LocalDate.of(2021, 10, 26),
            getTimes().get(1)
        );
        lesson2.setGroups(getGroups());
        lesson2.setId(2);
        lessons.add(lesson1);
        lessons.add(lesson2);
    }

    @Test
    public void givenNewLesson_whenCreate_thenCreated() throws IOException {
        Lesson lesson = new Lesson(courses.get(0), getClassrooms().get(0), teachers.get(0),
            LocalDate.of(2021, 12, 15), getTimes().get(0));
        lesson.setGroups(groups);
        addStudentToGroup(groups.get(0), 10);
        addStudentToGroup(groups.get(1), 10);
        addStudentToGroup(groups.get(2), 10);

        when(vacationDao.getByTeacherId(lesson.getTeacher().getId())).thenReturn(vacations);
        when(courseDao.getByTeacherId(lesson.getTeacher().getId())).thenReturn(courses);
        when(lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(), lesson.getTeacher())).thenReturn(new ArrayList<>());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom())).thenReturn(new ArrayList<>());
        when(lessonDao.getByDateAndTime(lesson.getDate(), lesson.getTime())).thenReturn(new ArrayList<>());
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());

        lessonService.create(lesson);

        verify(lessonDao).create(lesson);
    }

    @Test
    public void givenLessonId_whenGetById_thenReturn() {
        Lesson lesson = lessons.get(0);

        when(lessonDao.getById(lesson.getId())).thenReturn(Optional.of(lesson));

        assertEquals(lesson, lessonService.getById(1));
    }

    @Test
    public void givenUpdatedLesson_whenUpdate_thenUpdated() throws IOException {
        Lesson lesson = lessons.get(0);
        lesson.setGroups(groups);
        addStudentToGroup(groups.get(0), 10);
        addStudentToGroup(groups.get(1), 10);
        addStudentToGroup(groups.get(2), 10);

        when(vacationDao.getByTeacherId(lesson.getTeacher().getId())).thenReturn(vacations);
        when(courseDao.getByTeacherId(lesson.getTeacher().getId())).thenReturn(courses);
        when(lessonDao.getByDateAndTimeAndTeacher(lesson.getDate(), lesson.getTime(), lesson.getTeacher())).thenReturn(new ArrayList<>());
        when(lessonDao.getByDateAndTimeAndClassroom(lesson.getDate(), lesson.getTime(), lesson.getClassroom())).thenReturn(new ArrayList<>());
        when(lessonDao.getByDateAndTime(lesson.getDate(), lesson.getTime())).thenReturn(new ArrayList<>());
        when(holidayDao.getByDate(lesson.getDate())).thenReturn(Optional.empty());

        lessonService.update(lesson);

        verify(lessonDao).update(lesson);
    }

    @Test
    public void givenLessonId_whenDelete_thenDeleted() {
        lessonService.delete(1);

        verify(lessonDao).delete(1);
    }

    private List<Teacher> getTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        Teacher teacher = dataSource.generateTeacher();
        teacher.setCourses(getCourses());
        teacher.setId(1);
        teachers.add(teacher);
        return teachers;
    }

    private List<Vacation> getVacations(List<Teacher> teachers) {
        Teacher teacher = teachers.get(0);
        List<Vacation> vacations = new ArrayList<>();
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
        return vacations;
    }

    private List<Course> getCourses() {
        List<Course> courses = new ArrayList<>();
        Course course1 = new Course("Math");
        course1.setId(1);
        Course course2 = new Course("Physics");
        course2.setId(2);
        courses.add(course1);
        courses.add(course2);
        return courses;
    }

    private List<Classroom> getClassrooms() {
        List<Classroom> classrooms = new ArrayList<>();
        Classroom classroom1 = new Classroom(101, 30);
        classroom1.setId(1);
        Classroom classroom2 = new Classroom(120, 60);
        classroom2.setId(2);
        classrooms.add(classroom1);
        classrooms.add(classroom2);
        return classrooms;
    }

    private List<Time> getTimes() {
        List<Time> times = new ArrayList<>();
        Time time1 = new Time(LocalTime.of(8, 0), LocalTime.of(9, 30));
        time1.setId(1);
        Time time2 = new Time(LocalTime.of(12, 0), LocalTime.of(13, 30));
        time2.setId(2);
        times.add(time1);
        times.add(time2);
        return times;
    }

    private List<Group> getGroups() {
        List<Group> groups = new ArrayList<>();
        Group group1 = new Group("MH-12");
        Group group2 = new Group("LF-43");
        Group group3 = new Group("DF-32");
        groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);
        groups.add(group3);
        return groups;
    }

    private List<Holiday> getHolidays() {
        List<Holiday> holidays = new ArrayList<>();
        Holiday holiday1 = new Holiday("New Year", LocalDate.of(2022, 1, 1));
        Holiday holiday2 = new Holiday("Christmas", LocalDate.of(2022, 1, 7));
        holidays.add(holiday1);
        holidays.add(holiday2);
        return holidays;
    }

    private void addStudentToGroup(Group group, int numberOfStudent) throws IOException {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < numberOfStudent; i++) {
            students.add(dataSource.generateStudent());
        }
        group.setStudents(students);
    }
}
