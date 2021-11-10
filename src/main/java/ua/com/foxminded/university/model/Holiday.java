package ua.com.foxminded.university.model;

import java.time.LocalDate;
import java.util.Objects;

public class Holiday {

    private int id;
    private String name;
    private LocalDate date;

    public Holiday() {

    }

    public Holiday(String name, LocalDate date) {
        this.name = name;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Holiday holiday = (Holiday) o;
        return Objects.equals(name, holiday.name) && Objects.equals(date, holiday.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, date);
    }
}
