package ua.com.foxminded.university.model;

import javax.persistence.*;
import java.util.Objects;

@NamedQueries({
    @NamedQuery(name = "Classroom_delete", query = "DELETE FROM Classroom AS c WHERE c.id = :id"),
    @NamedQuery(name = "Classroom_getAll", query = "SELECT c FROM Classroom AS c"),
    @NamedQuery(name = "Classroom_countAllRows", query = "SELECT COUNT (c) FROM Classroom AS c"),
    @NamedQuery(name = "Classroom_getByNumber", query = "SElECT c FROM Classroom AS c WHERE c.number = :number")
})
@Entity
@Table(name = "classrooms")
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int number;
    private int capacity;

    public Classroom() {

    }

    public Classroom(int number, int capacity) {
        this.number = number;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "number: " + number + "; capacity: " + capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Classroom)) return false;
        Classroom classroom = (Classroom) o;
        return number == classroom.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    public static class Builder {

        private Classroom classroom;

        public Builder() {
            classroom = new Classroom();
        }

        public Builder setId(int id) {
            classroom.setId(id);
            return this;
        }

        public Builder setNumber(int number) {
            classroom.setNumber(number);
            return this;
        }

        public Builder setCapacity(int capacity) {
            classroom.setCapacity(capacity);
            return this;
        }

        public Classroom build() {
            return classroom;
        }
    }
}
