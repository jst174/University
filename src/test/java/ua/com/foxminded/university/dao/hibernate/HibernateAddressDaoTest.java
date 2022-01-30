package ua.com.foxminded.university.dao.hibernate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfigTest.class})
@Sql({"/create_address_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class HibernateAddressDaoTest {

    @Autowired
    private AddressDao addressDao;

    @Test
    public void givenNewAddress_whenCreate_thenCreated() {
        Address address = new Address("Russia", "Saint Petersburg", "Nevsky Prospect",
            "15", "45", "342423");

        addressDao.create(address);

        Address actual = addressDao.getById(3).get();
        assertEquals(address, actual);
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

        addressDao.update(updatedAddress);

        Address actual = addressDao.getById(1).get();
        assertEquals(updatedAddress, actual);
    }

    @Test
    public void givenId_whenDelete_thenDeleted() {
        addressDao.delete(1);

        assertEquals(Optional.empty(), addressDao.getById(1));
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

    @Test
    public void whenCountTotalRows_thenReturn(){
        assertEquals(2, addressDao.countTotalRows());
    }

    interface TestData {
        Address address1 = new Address.Builder()
            .setId(1)
            .setCountry("Russia")
            .setCity("Saint Petersburg")
            .setStreet("Nevsky Prospect")
            .setHouseNumber("15")
            .setApartmentNumber("45")
            .setPostcode("342423")
            .build();

        Address address2 = new Address.Builder()
            .setId(1)
            .setCountry("Russia")
            .setCity("Yekaterinburg")
            .setStreet("Lenin Avenue")
            .setHouseNumber("34")
            .setApartmentNumber("432")
            .setPostcode("34254")
            .build();
    }
}
