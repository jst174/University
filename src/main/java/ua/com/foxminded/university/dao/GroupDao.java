package ua.com.foxminded.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.foxminded.university.model.Group;

import java.util.Optional;

public interface GroupDao extends JpaRepository<Group, Integer> {

    @Query("SELECT c FROM Group c WHERE c.name = :name")
    Optional<Group> findByName(@Param("name") String name);
}
