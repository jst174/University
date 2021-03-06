package ua.com.foxminded.university.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    void create(T entity);

    Optional<T> getById(int id);

    void update(T entity);

    void delete(int id);

    List<T> getAll();

    Page<T> getAll(Pageable pageable);

    Long count();

}
