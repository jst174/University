package ua.com.foxminded.university.dao.jdbc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.university.config.SpringConfigTest;
import ua.com.foxminded.university.dao.jdbc.JdbcAddressDao;
import ua.com.foxminded.university.model.Address;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfigTest.class})
@Sql({"/create_address_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcAddressDaoTest {

    @Autowired
    private JdbcAddressDao addressDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void givenNewAddress_whenCreate_thenCreated() {
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");

        addressDao.create(address);

        assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "addresses"));
    }

    @Test
    public void givenId_whenGetById_thenReturn() {
        Address expected = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");

        Address actual = addressDao.getById(1);

        assertEquals(expected, actual);
    }

    @Test
    public void givenUpdatedAddressAndId_whenUpdate_thenUpdated() {
        String SQL = "SELECT COUNT(0) FROM addresses WHERE country = 'Russia' and city = 'Moscow'" +
            "and street = 'Kutuzov Avenue' and house_number = '43' " +
            "and apartment_number = '192' and postcode = '432436'";
        Address updatedAddress = new Address("Russia", "Moscow", "Kutuzov Avenue",
            "43", "192", "432436");

        addressDao.update(1, updatedAddress);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "addresses", SQL));
    }

    @Test
    public void givenId_whenDelete_thenDeleted() {

        addressDao.delete(1);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "addresses"));
    }

    @Test
    public void whenGetAll_thenReturnAllAddress(){
        Address address1 = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        Address address2 = new Address("Russia", "Yekaterinburg", "Lenin Avenue",
            "34", "432", "34254");
        List<Address> expected = new ArrayList<>();
        expected.add(address1);
        expected.add(address2);

        List<Address> actual = addressDao.getAll();

        assertEquals(expected, actual);
    }
}