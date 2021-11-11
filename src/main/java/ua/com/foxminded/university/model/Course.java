package ua.com.foxminded.university.model;

import java.util.Objects;

public class Course {

    private int id;
    private String name;

    public Course() {

    }

    public Course(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(name, course.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static class Builder {

        private Course course;

        public Builder() {
            course = new Course();
        }

        public Builder setId(int id) {
            course.setId(id);
            return this;
        }

        public Builder setName(String name) {
            course.setName(name);
            return this;
        }

        public Course build() {
            return course;
        }
    }
}
