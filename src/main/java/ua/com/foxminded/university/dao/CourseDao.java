package ua.com.foxminded.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.foxminded.university.model.Course;

import java.util.Optional;

public interface CourseDao extends JpaRepository<Course, Integer> {

    @Query("SELECT c FROM Course c WHERE c.name = :name")
    Optional<Course> findByName(@Param("name") String name);
}
