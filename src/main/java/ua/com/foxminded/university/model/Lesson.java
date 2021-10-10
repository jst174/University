package ua.com.foxminded.university.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lesson {

    private int id;
    private List<Group> groups;
    private Course course;
    private Classroom classroom;
    private Teacher teacher;
    private LocalDate date;
    private Time time;

    public Lesson(Course course, Classroom classroom, Teacher teacher, LocalDate localDate,
                  Time time) {
        this.course = course;
        this.classroom = classroom;
        this.teacher = teacher;
        this.date = localDate;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Lesson [groups=" + groups + ", course=" + course + ", classroom=" + classroom + ", teacher=" + teacher
            + ", localDate=" + date + ", time=" + time + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lesson)) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(groups, lesson.groups) && Objects.equals(course, lesson.course) && Objects.equals(classroom, lesson.classroom) && Objects.equals(teacher, lesson.teacher) && Objects.equals(date, lesson.date) && Objects.equals(time, lesson.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groups, course, classroom, teacher, date, time);
    }
}
