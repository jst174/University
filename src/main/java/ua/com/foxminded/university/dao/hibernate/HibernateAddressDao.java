package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.AddressDao;
import ua.com.foxminded.university.model.Address;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class HibernateAddressDao implements AddressDao {

    private SessionFactory sessionFactory;

    public HibernateAddressDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(Address address) {
        sessionFactory.getCurrentSession().save(address);
    }

    public Optional<Address> getById(int id) {
        try {
            return sessionFactory.getCurrentSession()
                .byId(Address.class)
                .loadOptional(id);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(Address address) {
        sessionFactory.getCurrentSession().update(address);
    }

    public void delete(int id) {
        sessionFactory.getCurrentSession()
            .createQuery("DELETE FROM Address WHERE id=:id")
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public List<Address> getAll() {
        return sessionFactory.getCurrentSession()
            .createQuery("FROM Address")
            .list();
    }

    @Override
    public Page<Address> getAll(Pageable pageable) {
        Session session = sessionFactory.getCurrentSession();
        Long totalRows = (Long) session
            .createQuery("SELECT COUNT (*) FROM Address")
            .uniqueResult();
        List<Address> addresses = session
            .createQuery("FROM Address")
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .list();
        return new PageImpl<Address>(addresses, pageable, totalRows);
    }
}
