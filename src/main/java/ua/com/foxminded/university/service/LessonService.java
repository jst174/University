package ua.com.foxminded.university.service;

import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.*;
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Holiday;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Vacation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class LessonService {

    private LessonDao lessonDao;
    private TeacherDao teacherDao;
    private VacationDao vacationDao;
    private CourseDao courseDao;
    private HolidayDao holidayDao;

    public LessonService(
        LessonDao lessonDao,
        TeacherDao teacherDao,
        VacationDao vacationDao,
        CourseDao courseDao,
        HolidayDao holidayDao) {
        this.lessonDao = lessonDao;
        this.teacherDao = teacherDao;
        this.vacationDao = vacationDao;
        this.courseDao = courseDao;
        this.holidayDao = holidayDao;
    }

    public void create(Lesson lesson) {
        if (checkConditions(lesson)) {
            lessonDao.create(lesson);
        } else {
            throw new IllegalArgumentException("lesson is not created");
        }

    }

    public Lesson getById(int id) {
        if (idIsExist(id)) {
            return lessonDao.getById(id);
        } else {
            throw new IllegalArgumentException("lesson is not found");
        }

    }

    public void update(Lesson lesson) {
        if (checkConditions(lesson)) {
            lessonDao.update(lesson);
        } else {
            throw new IllegalArgumentException("lesson is not updated");
        }

    }

    public void delete(int id) {
        if (idIsExist(id)) {
            lessonDao.delete(id);
        } else {
            throw new IllegalArgumentException("lesson is not found");
        }

    }

    public List<Lesson> getAll() {
        return lessonDao.getAll();
    }

    private boolean checkConditions(Lesson lesson) {
        if (!lessonIsExist(lesson) && !isHoliday(lesson) && !isWeekend(lesson) && !isClassroomsBusy(lesson)
            && teacherIsMatchedCourse(lesson) && !teacherOnVacation(lesson) && !isTeacherBusy(lesson)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean lessonIsExist(Lesson lesson) {
        List<Lesson> lessons = lessonDao.getAll();
        return lessons.stream().anyMatch(lesson::equals);
    }

    private boolean idIsExist(int id) {
        List<Lesson> lessons = lessonDao.getAll();
        return lessons.stream().anyMatch(lesson -> lesson.getId() == id);
    }

    private boolean teacherOnVacation(Lesson lesson) {
        LocalDate lessonDate = lesson.getDate();
        List<Vacation> vacations = vacationDao.getByTeacherId(lesson.getTeacher().getId());
        return vacations.stream()
            .anyMatch(vacation -> (lessonDate.isAfter(vacation.getStart()) && lessonDate.isBefore(vacation.getEnd()))
                || (lessonDate.isEqual(vacation.getStart()) || (lessonDate.isEqual(vacation.getEnd()))));
    }

    private boolean teacherIsMatchedCourse(Lesson lesson) {
        List<Course> courses = courseDao.getByTeacherId(lesson.getTeacher().getId());
        return courses.stream().anyMatch(lesson.getCourse()::equals);
    }

    private boolean isTeacherBusy(Lesson newLesson) {
        List<Lesson> lessons = lessonDao.getByTeacherId(newLesson.getTeacher().getId());
        return compareLessonDateAndTime(lessons, newLesson);
    }

    private boolean isClassroomsBusy(Lesson newLesson) {
        List<Lesson> lessons = lessonDao.getByClassroomId(newLesson.getClassroom().getId());
        return compareLessonDateAndTime(lessons, newLesson);
    }

    private boolean isHoliday(Lesson lesson) {
        List<Holiday> holidays = holidayDao.getAll();
        return holidays.stream().anyMatch(holiday -> holiday.getDate().isEqual(lesson.getDate()));
    }

    private boolean isWeekend(Lesson lesson) {
        LocalDate lessonDate = lesson.getDate();
        if (lessonDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) || lessonDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean compareLessonDateAndTime(List<Lesson> lessons, Lesson newLesson) {
        return lessons.stream().
            anyMatch(lesson -> (lesson.getDate().isEqual(newLesson.getDate())
                && lesson.getTime().equals(newLesson.getTime())));
    }
}
