package ua.com.foxminded.university.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    Optional<Teacher> findByFirstNameAndLastName(
        @Param("firstName") String firstName,
        @Param("lastName") String lastName);
}
