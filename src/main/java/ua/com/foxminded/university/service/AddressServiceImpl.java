package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.model.Address;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private AddressDao addressDao;

    public AddressServiceImpl(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    @Override
    public void create(Address address) {
        addressDao.create(address);
    }

    @Override
    public Address getById(int id) {
        return addressDao.getById(id);
    }

    @Override
    public void update(Address address) {
        addressDao.update(address);
    }

    @Override
    public void delete(int id) {
        addressDao.delete(id);
    }

    @Override
    public List<Address> getAll() {
        return addressDao.getAll();
    }
}
