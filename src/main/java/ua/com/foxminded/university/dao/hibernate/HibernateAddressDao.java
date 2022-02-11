package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.model.Address;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Component
public class HibernateAddressDao implements AddressDao {

    private final EntityManager entityManager;

    public HibernateAddressDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Address address) {
        entityManager.unwrap(Session.class).save(address);
    }

    public Optional<Address> getById(int id) {
        return entityManager.unwrap(Session.class)
            .byId(Address.class)
            .loadOptional(id);
    }

    public void update(Address address) {
        entityManager.unwrap(Session.class).update(address);
    }

    public void delete(int id) {
        entityManager.unwrap(Session.class)
            .getNamedQuery("Address_delete")
            .setParameter("id", id)
            .executeUpdate();

    }

    @Override
    public List<Address> getAll() {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Address_getAll", Address.class)
            .list();
    }

    @Override
    public Page<Address> getAll(Pageable pageable) {
        List<Address> addresses = entityManager.unwrap(Session.class)
            .createNamedQuery("Address_getAll", Address.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Address>(addresses, pageable, count());
    }

    @Override
    public Long count() {
        return entityManager.unwrap(Session.class)
            .createNamedQuery("Address_countAllRows", Long.class)
            .getSingleResult();
    }

}
