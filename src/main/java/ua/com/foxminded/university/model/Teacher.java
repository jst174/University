package ua.com.foxminded.university.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NamedQueries({
    @NamedQuery(name = "Teacher_delete", query = "DELETE FROM Teacher AS t WHERE t.id = :id"),
    @NamedQuery(name = "Teacher_getAll", query = "SELECT t FROM Teacher AS t"),
    @NamedQuery(name = "Teacher_countAllRows", query = "SELECT COUNT (t) FROM Teacher AS t"),
    @NamedQuery(name = "Teacher_getByName", query = "SELECT t FROM Teacher AS t " +
        "WHERE t.firstName =: firstName and t.lastName = :lastName")
})
@Entity
@Table(name = "teachers")
public class Teacher extends Person {

    @Column(name = "academic_degree")
    @Enumerated(EnumType.STRING)
    private AcademicDegree academicDegree;
    @ManyToMany(cascade = {
        CascadeType.PERSIST, CascadeType.DETACH,
        CascadeType.MERGE, CascadeType.MERGE
    })
    @JoinTable(
        name = "teachers_courses",
        joinColumns = {@JoinColumn(name = "teacher_id")},
        inverseJoinColumns = {@JoinColumn(name = "course_id")}
    )
    private List<Course> courses;
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Vacation> vacations;

    public Teacher() {

    }

    public Teacher(String firstName, String lastName, LocalDate birthDate, Gender gender, Address address,
                   String phoneNumber, String email, AcademicDegree academicDegree) {
        super(firstName, lastName, birthDate, gender, address, phoneNumber, email);
        this.academicDegree = academicDegree;
        courses = new ArrayList<>();
        vacations = new ArrayList<>();
    }

    public AcademicDegree getAcademicDegree() {
        return academicDegree;
    }

    public void setAcademicDegree(AcademicDegree academicDegree) {
        this.academicDegree = academicDegree;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Vacation> getVacations() {
        return vacations;
    }

    public void setVacations(List<Vacation> vacations) {
        this.vacations = vacations;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + "; " + getBirthDate() + "; " + getGender() + "; " + getAddress()
            + "; " + getPhoneNumber() + "; " + getEmail() + "; " + academicDegree;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher)) return false;
        if (!super.equals(o)) return false;
        Teacher teacher = (Teacher) o;
        return academicDegree == teacher.academicDegree && Objects.equals(vacations, teacher.vacations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), academicDegree, vacations);
    }

    public static class Builder {

        private Teacher teacher;

        public Builder() {
            teacher = new Teacher();
        }

        public Builder setId(int id) {
            teacher.setId(id);
            return this;
        }

        public Builder setFirstName(String firstName) {
            teacher.setFirstName(firstName);
            return this;
        }

        public Builder setLastName(String lastName) {
            teacher.setLastName(lastName);
            return this;
        }

        public Builder setBirtDate(LocalDate birtDate) {
            teacher.setBirthDate(birtDate);
            return this;
        }

        public Builder setGender(Gender gender) {
            teacher.setGender(gender);
            return this;
        }

        public Builder setAddress(Address address) {
            teacher.setAddress(address);
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            teacher.setPhoneNumber(phoneNumber);
            return this;
        }

        public Builder setEmail(String email) {
            teacher.setEmail(email);
            return this;
        }

        public Builder setAcademicDegree(AcademicDegree academicDegree) {
            teacher.setAcademicDegree(academicDegree);
            return this;
        }

        public Builder setCourses(List<Course> courses) {
            teacher.setCourses(courses);
            return this;
        }

        public Builder setVacations(List<Vacation> vacations) {
            teacher.setVacations(vacations);
            return this;
        }

        public Builder clone(Teacher teacher) {
            Builder builder = new Builder();
            builder.setId(teacher.getId());
            builder.setFirstName(teacher.getFirstName());
            builder.setLastName(teacher.getLastName());
            builder.setAddress(teacher.getAddress());
            builder.setBirtDate(teacher.getBirthDate());
            builder.setGender(teacher.getGender());
            builder.setPhoneNumber(teacher.getPhoneNumber());
            builder.setEmail(teacher.getEmail());
            builder.setAcademicDegree(teacher.getAcademicDegree());
            builder.setCourses(teacher.getCourses());
            builder.setVacations(teacher.getVacations());
            return builder;
        }

        public Teacher build() {
            return teacher;
        }

    }
}
