package ua.com.foxminded.university.dao.hibernate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.com.foxminded.university.config.DatabaseConfigTest;
import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.model.Address;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfigTest.class})
@Sql({"/create_address_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class HibernateAddressDaoTest {

    @Autowired
    private AddressDao addressDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void givenNewAddress_whenCreate_thenCreated() {
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        int expectedRows = countRowsInTable(jdbcTemplate, "addresses") + 1;

        addressDao.create(address);

        assertEquals(expectedRows, countRowsInTable(jdbcTemplate, "addresses"));
    }

    @Test
    public void givenId_whenGetById_thenReturn() {
        Address expected = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");

        Optional<Address> actual = addressDao.getById(1);

        assertEquals(expected, actual.get());
    }

    @Test
    public void givenUpdatedAddressAndId_whenUpdate_thenUpdated() {
        String sql = "SELECT COUNT(0) FROM addresses WHERE country = 'Russia' and city = 'Moscow'" +
            "and street = 'Kutuzov Avenue' and house_number = '43' " +
            "and apartment_number = '192' and postcode = '432436'";
        Address updatedAddress = new Address("Russia", "Moscow", "Kutuzov Avenue",
            "43", "192", "432436");
        updatedAddress.setId(1);
        int expectedRows = countRowsInTableWhere(jdbcTemplate, "addresses", sql) + 1;

        addressDao.update(updatedAddress);

        assertEquals(expectedRows, countRowsInTableWhere(jdbcTemplate, "addresses", sql));
    }

    @Test
    public void givenId_whenDelete_thenDeleted() {
        int expectedRows = countRowsInTable(jdbcTemplate, "addresses") - 1;

        addressDao.delete(1);

        assertEquals(expectedRows, countRowsInTable(jdbcTemplate, "addresses"));
    }

    @Test
    public void whenGetAll_thenReturnAllAddress() {
        List<Address> expected = Arrays.asList(TestData.address1, TestData.address2);

        List<Address> actual = addressDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    public void givenPageable_whenGetAll_thenReturn() {
        List<Address> addresses = Arrays.asList(TestData.address1, TestData.address2);
        Pageable pageable = PageRequest.of(0, 2);

        Page<Address> addressPage = new PageImpl<Address>(addresses, pageable, addresses.size());

        assertEquals(addressPage, addressDao.getAll(pageable));
    }

    interface TestData {
        Address address1 = new Address.Builder()
            .setCountry("Russia")
            .setCity("Saint Petersburg")
            .setStreet("Nevsky Prospect")
            .setHouseNumber("15")
            .setApartmentNumber("45")
            .setPostcode("342423")
            .build();

        Address address2 = new Address.Builder()
            .setCountry("Russia")
            .setCity("Yekaterinburg")
            .setStreet("Lenin Avenue")
            .setHouseNumber("34")
            .setApartmentNumber("432")
            .setPostcode("34254")
            .build();
    }
}
