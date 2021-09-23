package ua.com.foxminded.university.Dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Address;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AddressMapper implements RowMapper<Address> {

    @Override
    public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
        Address address = new Address(rs.getString("country"),
            rs.getString("city"),
            rs.getString("street"),
            rs.getString("house_number"),
            rs.getString("apartment_number"),
            rs.getString("postcode"));
        address.setId(rs.getInt("address_id"));
        return address;
    }
}
