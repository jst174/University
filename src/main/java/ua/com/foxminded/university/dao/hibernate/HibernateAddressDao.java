package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.model.Address;

import java.util.List;
import java.util.Optional;

@Component
public class HibernateAddressDao implements AddressDao {

    private final SessionFactory sessionFactory;

    public HibernateAddressDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(Address address) {
        sessionFactory.getCurrentSession().save(address);
    }

    public Optional<Address> getById(int id) {
        return sessionFactory.getCurrentSession()
            .byId(Address.class)
            .loadOptional(id);
    }

    public void update(Address address) {
        sessionFactory.getCurrentSession().update(address);
    }

    public void delete(int id) {
        sessionFactory.getCurrentSession()
            .getNamedQuery("Address_delete")
            .setParameter("id", id)
            .executeUpdate();

    }

    @Override
    public List<Address> getAll() {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Address_getAll", Address.class)
            .list();
    }

    @Override
    public Page<Address> getAll(Pageable pageable) {
        List<Address> addresses = sessionFactory.getCurrentSession()
            .createNamedQuery("Address_getAll", Address.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Address>(addresses, pageable, count());
    }

    @Override
    public Long count() {
        return sessionFactory.getCurrentSession()
            .createNamedQuery("Address_countAllRows", Long.class)
            .getSingleResult();
    }

}
