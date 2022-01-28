package ua.com.foxminded.university.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.foxminded.university.model.Group;

import java.util.List;
import java.util.Optional;

public interface GroupDao extends Dao<Group> {

    Optional<Group> getByName(String name);
}
