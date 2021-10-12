package ua.com.foxminded.university.dao;

import java.util.List;

public interface Dao <T> {

    void create(T entity);
    T getById(int id);
    void update(T entity);
    void delete(int id);
    List<T> getAll();

}
