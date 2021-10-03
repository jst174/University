package ua.com.foxminded.university.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.dao.mapper.AddressMapper;
import ua.com.foxminded.university.model.Address;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
public class JdbcAddressDao implements AddressDao {

    private static final String SQL_INSERT_ADDRESS =
        "INSERT INTO addresses(country, city, street, house_number, apartment_number, postcode) values (?,?,?,?,?,?)";
    private static final String SQL_UPDATE_ADDRESS = "UPDATE addresses SET country = ?, city = ?, street = ?," +
        "house_number = ?, apartment_number = ?, postcode = ? WHERE id = ?";
    private static final String SQL_FIND_ADDRESS = "SELECT * FROM addresses WHERE id = ?";
    private static final String SQL_DELETE_ADDRESS = "DELETE FROM addresses WHERE id = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM addresses";


    private JdbcTemplate jdbcTemplate;
    private AddressMapper addressMapper;

    @Autowired
    public JdbcAddressDao(JdbcTemplate jdbcTemplate, AddressMapper addressMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.addressMapper  = addressMapper;
    }

    public void create(Address address) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_ADDRESS, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, address.getCountry());
            statement.setString(2, address.getCity());
            statement.setString(3, address.getStreet());
            statement.setString(4, address.getHouseNumber());
            statement.setString(5, address.getApartmentNumber());
            statement.setString(6, address.getPostcode());
            return statement;
        }, keyHolder);
        address.setId((int)keyHolder.getKeys().get("id"));

    }

    public Address getById(int id) {
        return jdbcTemplate.queryForObject(SQL_FIND_ADDRESS, addressMapper, id);
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

    @Override
    public List<Address> getAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, addressMapper);
    }
}
