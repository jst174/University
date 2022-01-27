package ua.com.foxminded.university.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@NamedQueries({
    @NamedQuery(name = "Student_delete", query = "DELETE FROM Student AS s WHERE s.id = :id"),
    @NamedQuery(name = "Student_getAll", query = "SELECT s FROM Student  AS s"),
    @NamedQuery(name = "Student_countAllRows", query = "SELECT COUNT (s) FROM Student AS s"),
    @NamedQuery(name = "Student_getByName", query = "SELECT s FROM Student AS s " +
        "WHERE s.firstName =: firstName and s.lastName = :lastName")
})
@Entity
@Table(name = "students")
public class Student extends Person {

    @ManyToOne(cascade = {
        CascadeType.PERSIST, CascadeType.MERGE,
        CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "group_id")
    private Group group;

    public Student() {

    }

    public Student(String firstName, String lastName, LocalDate birthDate, Gender gender, Address address,
                   String phoneNumber, String email) {
        super(firstName, lastName, birthDate, gender, address, phoneNumber, email);
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

    public static class Builder {

        private Student student;

        public Builder() {
            student = new Student();
        }

        public Builder setId(int id) {
            student.setId(id);
            return this;
        }

        public Builder setFirstName(String firstName) {
            student.setFirstName(firstName);
            return this;
        }

        public Builder setLastName(String lastName) {
            student.setLastName(lastName);
            return this;
        }

        public Builder setBirtDate(LocalDate birtDate) {
            student.setBirthDate(birtDate);
            return this;
        }

        public Builder setGender(Gender gender) {
            student.setGender(gender);
            return this;
        }

        public Builder setAddress(Address address) {
            student.setAddress(address);
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            student.setPhoneNumber(phoneNumber);
            return this;
        }

        public Builder setEmail(String email) {
            student.setEmail(email);
            return this;
        }

        public Builder setGroup(Group group) {
            student.setGroup(group);
            return this;
        }

        public Builder clone(Student student) {
            Builder builder = new Builder();
            builder.setId(student.getId());
            builder.setFirstName(student.getFirstName());
            builder.setLastName(student.getLastName());
            builder.setAddress(student.getAddress());
            builder.setBirtDate(student.getBirthDate());
            builder.setGender(student.getGender());
            builder.setPhoneNumber(student.getPhoneNumber());
            builder.setEmail(student.getEmail());
            builder.setGroup(student.getGroup());
            return builder;
        }

        public Student build() {
            return student;
        }

    }
}
