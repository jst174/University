package ua.com.foxminded.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.foxminded.university.model.Group;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    Optional<Group> findByName(@Param("name") String name);
}
