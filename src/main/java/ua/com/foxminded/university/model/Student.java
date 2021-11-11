package ua.com.foxminded.university.model;

import java.time.LocalDate;
import java.util.Objects;

public class Student extends Person {

    private int id;
    private Group group;

    public Student() {

    }

    public Student(String firstName, String lastName, LocalDate birthDate, Gender gender, Address address,
                   String phoneNumber, String email) {
        super(firstName, lastName, birthDate, gender, address, phoneNumber, email);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + "; " + getBirthDate() + "; " + getGender() + "; " + getAddress()
            + "; " + getPhoneNumber() + "; " + getEmail() + "; " + group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(group, student.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), group);
    }
}
