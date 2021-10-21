package ua.com.foxminded.university.service;

import java.util.List;

public interface Service <T> {

    void create(T entity);
    T getById(int id);
    void update(T entity);
    void delete(int id);
    List<T> getAll();
}
