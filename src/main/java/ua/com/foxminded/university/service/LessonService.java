package ua.com.foxminded.university.service;

import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.*;
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Vacation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LessonService {

    private LessonDao lessonDao;
    private TeacherDao teacherDao;
    private VacationDao vacationDao;
    private CourseDao courseDao;
    private HolidayDao holidayDao;
    private GroupDao groupDao;

    public LessonService(
        LessonDao lessonDao,
        TeacherDao teacherDao,
        VacationDao vacationDao,
        CourseDao courseDao,
        HolidayDao holidayDao,
        GroupDao groupDao) {
        this.lessonDao = lessonDao;
        this.teacherDao = teacherDao;
        this.vacationDao = vacationDao;
        this.courseDao = courseDao;
        this.holidayDao = holidayDao;
        this.groupDao = groupDao;
    }

    public void create(Lesson lesson) {
        if (checkConditions(lesson)) {
            lessonDao.create(lesson);
        }

    }

    public Lesson getById(int id) {
        return lessonDao.getById(id).get();
    }

    public void update(Lesson lesson) {
        if ((lessonDao.getById(lesson.getId()).isPresent()) && (checkConditions(lesson))) {
            lessonDao.update(lesson);
        }
    }

    public void delete(int id) {
        lessonDao.delete(id);
    }

    public List<Lesson> getAll() {
        return lessonDao.getAll();
    }

    private boolean checkConditions(Lesson lesson) {
        return !isHoliday(lesson) && !isWeekend(lesson) && !isClassroomsBusy(lesson)
            && isTeacherMatchedCourse(lesson) && !isTeacherOnVacation(lesson) && !isTeacherBusy(lesson)
            && isPlacesEnough(lesson) && !isGroupBusy(lesson);
    }

    private boolean isTeacherOnVacation(Lesson lesson) {
        return vacationDao.getByTeacherAndLessonDate(lesson.getTeacher(), lesson.getDate()).isPresent();
    }

    private boolean isTeacherMatchedCourse(Lesson lesson) {
        List<Course> courses = lesson.getTeacher().getCourses();
        return courses.contains(lesson.getCourse());
    }

    private boolean isTeacherBusy(Lesson newLesson) {
        List<Lesson> lessons = lessonDao.getByDateAndTimeAndTeacher(newLesson.getDate(), newLesson.getTime(),
            newLesson.getTeacher());
        return lessons.stream().anyMatch(lesson -> lesson.getTeacher().equals(newLesson.getTeacher()));
    }

    private boolean isClassroomsBusy(Lesson newLesson) {
        List<Lesson> lessons = lessonDao.getByDateAndTimeAndClassroom(newLesson.getDate(), newLesson.getTime(),
            newLesson.getClassroom());
        return lessons.stream().anyMatch(lesson -> lesson.getClassroom().equals(newLesson.getClassroom()));
    }

    private boolean isHoliday(Lesson lesson) {
        return holidayDao.getByDate(lesson.getDate()).isPresent();
    }

    private boolean isWeekend(Lesson lesson) {
        LocalDate lessonDate = lesson.getDate();
        return (lessonDate.getDayOfWeek() == DayOfWeek.SATURDAY) || (lessonDate.getDayOfWeek() == DayOfWeek.SUNDAY);
    }

    private boolean isPlacesEnough(Lesson lesson) {
        List<Group> groups = lesson.getGroups();
        int numberOfStudents = groups.stream().mapToInt(group -> group.getStudents().size()).reduce(Integer::sum).orElse(0);
        return numberOfStudents <= lesson.getClassroom().getCapacity();
    }

    private boolean isGroupBusy(Lesson newLesson) {
        List<Lesson> lessons = lessonDao.getByDateAndTime(newLesson.getDate(), newLesson.getTime());
        List<Group> groups = new ArrayList<>();
        lessons.forEach(lesson -> groups.addAll(lesson.getGroups()));
        return groups.stream().anyMatch(group -> newLesson.getGroups().contains(group));
    }
}
