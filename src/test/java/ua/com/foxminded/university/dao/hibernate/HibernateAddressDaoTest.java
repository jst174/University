package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.SessionFactory;
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
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
public class HibernateAddressDaoTest {

    @Autowired
    private AddressDao addressDao;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    public void givenNewAddress_whenCreate_thenCreated() {
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");
        Long expectedRows = countRows() + 1;

        addressDao.create(address);

        Long actualRows = countRows();
        assertEquals(expectedRows, actualRows);
    }

    @Test
    public void givenId_whenGetById_thenReturn() {
        Optional<Address> actual = addressDao.getById(1);

        assertEquals(TestData.address1, actual.get());
    }

    @Test
    public void givenUpdatedAddressAndId_whenUpdate_thenUpdated() {
        Address updatedAddress = new Address.Builder().clone(TestData.address1)
            .setCity("Moscow")
            .setStreet("Kutuzov Avenue")
            .setHouseNumber("43")
            .setApartmentNumber("192")
            .setPostcode("432436")
            .build();
        Long expectedRows = countUpdatedRows(updatedAddress) + 1;

        addressDao.update(updatedAddress);

        Long actualRows = countUpdatedRows(updatedAddress);
        assertEquals(expectedRows, actualRows);
    }

    @Test
    public void givenId_whenDelete_thenDeleted() {
        Long expectedRows = countRows() - 1;

        addressDao.delete(1);

        Long actual = countRows();
        assertEquals(expectedRows, actual);
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


    private Long countRows() {
        return sessionFactory.getCurrentSession()
            .createQuery("SELECT COUNT (a) FROM Address AS a", Long.class)
            .uniqueResult();
    }

    private Long countUpdatedRows(Address address) {
        return sessionFactory.getCurrentSession()
            .createQuery("SELECT COUNT(a.id) FROM Address AS a WHERE a.country = :country AND " +
                "a.city = :city AND a.street = :street AND a.houseNumber = :houseNumber " +
                "AND a.apartmentNumber = :apartmentNumber AND a.postcode = :postcode", Long.class)
            .setParameter("country", address.getCountry())
            .setParameter("city", address.getCity())
            .setParameter("street", address.getStreet())
            .setParameter("houseNumber", address.getHouseNumber())
            .setParameter("apartmentNumber", address.getHouseNumber())
            .setParameter("postcode", address.getPostcode())
            .getSingleResult();
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
