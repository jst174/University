package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Course;

import java.util.List;

public interface AddressDao {

    void create(Address address);

    Address getById(int id);

    void update(int id, Address address);

    void delete(int id);

    List<Address> getAll();
}
