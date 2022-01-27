package ua.com.foxminded.university.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@NamedQueries({
    @NamedQuery(name = "Group_delete", query = "DELETE FROM Group AS g WHERE g.id = :id"),
    @NamedQuery(name = "Group_getAll", query = "SELECT g FROM Group AS g"),
    @NamedQuery(name = "Group_countAllRows", query = "SELECT COUNT (g) FROM Group AS g"),
    @NamedQuery(name = "Group_getByName", query = "SELECT g FROM Group AS g WHERE g.name = :name")
})
@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @OneToMany(
        mappedBy = "group",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List<Student> students;

    public Group() {

    }

    public Group(String name) {
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

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(name, group.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }

    public static class Builder {

        private Group group;

        public Builder() {
            group = new Group();
        }

        public Builder setId(int id) {
            group.setId(id);
            return this;
        }

        public Builder setName(String name) {
            group.setName(name);
            return this;
        }

        public Builder setStudents(List<Student> students) {
            group.setStudents(students);
            return this;
        }

        public Group build() {
            return group;
        }
    }

}
