package ua.com.foxminded.university.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Vacation {

    private int id;
    private LocalDate start;
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

        public Vacation build() {
            return vacation;
        }
    }
}
