package ua.com.foxminded.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.university.model.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {

}
