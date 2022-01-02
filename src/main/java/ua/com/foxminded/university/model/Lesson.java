package ua.com.foxminded.university.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

public class Lesson {

    private int id;
    private List<Group> groups;
    private Course course;
    private Classroom classroom;
    private Teacher teacher;
    @DateTimeFormat(iso = DATE)
    private LocalDate date;
    private Time time;

    public Lesson() {

    }

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

    public static class Builder {

        private Lesson lesson;

        public Builder() {
            lesson = new Lesson();
        }

        public Builder setId(int id) {
            lesson.setId(id);
            return this;
        }

        public Builder setCourse(Course course) {
            lesson.setCourse(course);
            return this;
        }

        public Builder setClassroom(Classroom classroom) {
            lesson.setClassroom(classroom);
            return this;
        }

        public Builder setTeacher(Teacher teacher) {
            lesson.setTeacher(teacher);
            return this;
        }

        public Builder setDate(LocalDate date) {
            lesson.setDate(date);
            return this;
        }

        public Builder setTime(Time time) {
            lesson.setTime(time);
            return this;
        }

        public Builder setGroups(List<Group> groups) {
            lesson.setGroups(groups);
            return this;
        }

        public Builder clone(Lesson lesson) {
            Builder builder = new Builder();
            builder.setId(lesson.getId());
            builder.setClassroom(lesson.getClassroom());
            builder.setDate(lesson.getDate());
            builder.setTime(lesson.getTime());
            builder.setCourse(lesson.getCourse());
            builder.setTeacher(lesson.getTeacher());
            builder.setGroups(lesson.getGroups());
            return builder;
        }

        public Lesson build() {
            return lesson;
        }
    }
}
