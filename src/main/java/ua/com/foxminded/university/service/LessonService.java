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
import java.util.stream.Collectors;

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
        if ((!isUnique(lesson)) && (checkConditions(lesson))) {
            lessonDao.create(lesson);
        }

    }

    public Lesson getById(int id) {
        return lessonDao.getById(id);
    }

    public void update(Lesson lesson) {
        if (checkConditions(lesson)) {
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
            && teacherIsMatchedCourse(lesson) && !teacherOnVacation(lesson) && !isTeacherBusy(lesson)
            && isPlacesEnough(lesson) && !isGroupBusy(lesson);
    }

    private boolean isUnique(Lesson lesson) {
        List<Lesson> lessons = lessonDao.getAll();
        return lessons.stream().anyMatch(lesson::equals);
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
        return courses.contains(lesson.getCourse());
    }

    private boolean isTeacherBusy(Lesson newLesson) {
        List<Lesson> lessons = lessonDao.getByDateAndTime(newLesson.getDate(), newLesson.getTime());
        return lessons.stream().anyMatch(lesson -> lesson.getTeacher().equals(newLesson.getTeacher()));
    }

    private boolean isClassroomsBusy(Lesson newLesson) {
        List<Lesson> lessons = lessonDao.getByDateAndTime(newLesson.getDate(), newLesson.getTime());
        return lessons.stream().anyMatch(lesson -> lesson.getClassroom().equals(newLesson.getClassroom()));
    }

    private boolean isHoliday(Lesson lesson) {
        return holidayDao.getByDate(lesson.getDate()).getDate().equals(lesson.getDate());
    }

    private boolean isWeekend(Lesson lesson) {
        LocalDate lessonDate = lesson.getDate();
        return lessonDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) || lessonDate.getDayOfWeek().equals(DayOfWeek.SUNDAY);
    }

    private boolean isPlacesEnough(Lesson lesson) {
        List<Group> groups = groupDao.getByLessonId(lesson.getId());
        int numberOfStudents = groups.stream().mapToInt(group -> group.getStudents().size()).reduce(Integer::sum).getAsInt();
        return numberOfStudents <= lesson.getClassroom().getCapacity();
    }

    private boolean isGroupBusy(Lesson newLesson){
        List<Lesson> lessons = lessonDao.getByDateAndTime(newLesson.getDate(), newLesson.getTime());
        List<Group> groups = new ArrayList<>();
        lessons.forEach(lesson -> groups.addAll(lesson.getGroups()));
        return groups.stream().anyMatch(group -> newLesson.getGroups().contains(group));
    }
}
