package ua.com.foxminded.university.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.Dao.mapper.AddressMapper;
import ua.com.foxminded.university.model.Address;

@Component
public class AddressDao {

    private static final String SQL_INSERT_ADDRESS =
        "INSERT INTO addresses(country, city, street, house_number, apartment_number, postcode) values (?,?,?,?,?,?)";
    private static final String SQL_UPDATE_ADDRESS = "UPDATE addresses SET country = ?, city = ?, street = ?," +
        "house_number = ?, apartment_number = ?, postcode = ? WHERE address_id = ?";
    private static final String SQL_FIND_ADDRESS = "SELECT * FROM addresses WHERE address_id = ?";
    private static final String SQL_DELETE_ADDRESS = "DELETE FROM addresses WHERE address_id = ?";


    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AddressDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Address address) {
        jdbcTemplate.update(SQL_INSERT_ADDRESS,
            address.getCountry(),
            address.getCity(),
            address.getStreet(),
            address.getHouseNumber(),
            address.getApartmentNumber(),
            address.getPostcode());
    }

    public Address getById(int id) {
        return jdbcTemplate.queryForObject(SQL_FIND_ADDRESS, new AddressMapper(), id);
    }

    public void update(int id, Address address) {
        jdbcTemplate.update(SQL_UPDATE_ADDRESS, address.getCountry(),
            address.getCity(),
            address.getStreet(),
            address.getHouseNumber(),
            address.getApartmentNumber(),
            address.getPostcode(),
            id);
    }

    public void delete(int id) {
        jdbcTemplate.update(SQL_DELETE_ADDRESS, id);
    }
}
