package ua.com.foxminded.university.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ua.com.foxminded.university.model.Classroom;

import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {

    Optional<Classroom> findByNumber(@Param("number") int number);
}
