package ua.com.foxminded.university.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@NamedQueries({
        @NamedQuery(name = "Vacation_delete", query = "DELETE FROM Vacation AS v WHERE v.id = :id"),
        @NamedQuery(name = "Vacation_getAll", query = "SELECT v FROM Vacation AS v"),
        @NamedQuery(name = "Vacation_countAllRows", query = "SELECT COUNT (v) FROM Vacation AS v"),
        @NamedQuery(name = "Vacation_getByTeacherAndLessonDate", query = "SELECT v FROM Vacation AS v " +
                "WHERE v.teacher.id=:id AND :lessonDate BETWEEN v.start AND v.ending"),
        @NamedQuery(name = "Vacation_getByTeacherAndVacationDates", query = "SELECT v FROM Vacation AS v " +
                "WHERE v.teacher.id=:id AND v.start = :start AND v.ending = :ending")
})
@Entity
@Table(name = "vacations")
public class Vacation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @DateTimeFormat(iso = DATE)
    private LocalDate start;
    @DateTimeFormat(iso = DATE)
    private LocalDate ending;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    public Vacation() {

    }

    public Vacation(LocalDate start, LocalDate ending, Teacher teacher) {
        this.start = start;
        this.ending = ending;
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

    public LocalDate getEnding() {
        return ending;
    }

    public void setEnding(LocalDate end) {
        this.ending = end;
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
                ", end=" + ending +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vacation vacation = (Vacation) o;
        return Objects.equals(start, vacation.start) && Objects.equals(ending, vacation.ending);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, ending);
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
            vacation.setEnding(end);
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
            builder.setEnd(vacation.getEnding());
            builder.setTeacher(vacation.getTeacher());
            return builder;
        }

        public Vacation build() {
            return vacation;
        }
    }
}
