package ua.com.foxminded.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.foxminded.university.model.Student;

import java.util.Optional;

public interface StudentDao extends JpaRepository<Student, Integer> {

    @Query("SELECT s FROM Student s WHERE s.firstName = :firstName and s.lastName = :lastName")
    Optional<Student> findByFirstNameAndLastName(
        @Param("firstName") String firstName,
        @Param("lastName") String lastName);
}
