package ua.com.foxminded.university.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

public class Vacation {

    private int id;
    @DateTimeFormat(iso = DATE)
    private LocalDate start;
    @DateTimeFormat(iso = DATE)
    private LocalDate end;
    private Teacher teacher;

    public Vacation() {

    }

    public Vacation(LocalDate start, LocalDate end, Teacher teacher) {
        this.start = start;
        this.end = end;
        this.teacher = teacher;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "Vacation{" +
            "start=" + start +
            ", end=" + end +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vacation vacation = (Vacation) o;
        return Objects.equals(start, vacation.start) && Objects.equals(end, vacation.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    public static class Builder {

        private Vacation vacation;

        public Builder() {
            vacation = new Vacation();
        }

        public Builder setId(int id) {
            vacation.setId(id);
            return this;
        }

        public Builder setStart(LocalDate start) {
            vacation.setStart(start);
            return this;
        }

        public Builder setEnd(LocalDate end) {
            vacation.setEnd(end);
            return this;
        }

        public Builder setTeacher(Teacher teacher) {
            vacation.setTeacher(teacher);
            return this;
        }

        public Builder clone(Vacation vacation) {
            Builder builder = new Builder();
            builder.setId(vacation.getId());
            builder.setStart(vacation.getStart());
            builder.setEnd(vacation.getEnd());
            builder.setTeacher(vacation.getTeacher());
            return builder;
        }

        public Vacation build() {
            return vacation;
        }
    }
}
