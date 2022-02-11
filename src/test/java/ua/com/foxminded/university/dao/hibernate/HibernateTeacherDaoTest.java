package ua.com.foxminded.university.dao.hibernate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.config.DatabaseConfigTest;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@Sql({"/create_address_test.sql", "/create_teacher_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class HibernateTeacherDaoTest {

    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Test
    @Transactional
    public void givenNewTeacher_whenCreate_thenCreated() {
        Teacher teacher = new Teacher(
            "Alex",
            "King",
            LocalDate.of(1977, 12, 16),
            Gender.MALE,
            TestData.address,
            "36d22366",
            "king97@yandex.ru",
            AcademicDegree.MASTER
        );

        teacherDao.create(teacher);

        assertEquals(teacher, hibernateTemplate.get(Teacher.class, teacher.getId()));
    }

    @Test
    @Transactional
    public void givenId_whenGetById_thenReturn() {
        assertEquals(TestData.teacher1, teacherDao.getById(1).get());
    }

    @Test
    @Transactional
    public void givenUpdatedTeacherAndId_whenUpdate_thenUpdated() {
        Teacher updatedTeacher = new Teacher(
            "Alan",
            "King",
            LocalDate.of(1945, 12, 16),
            Gender.MALE,
            TestData.address,
            "3622366",
            "king97@yandex.ru",
            AcademicDegree.DOCTORAL
        );
        updatedTeacher.setId(1);

        teacherDao.update(updatedTeacher);

        assertEquals(updatedTeacher, hibernateTemplate.get(Teacher.class, updatedTeacher.getId()));
    }

    @Test
    @Transactional
    public void givenId_whenDelete_thenDeleted() {
        assertNotNull(hibernateTemplate.get(Teacher.class, 1));

        teacherDao.delete(1);

        hibernateTemplate.clear();
        assertNull(hibernateTemplate.get(Teacher.class, 1));
    }

    @Test
    @Transactional
    public void whenGetAll_thenReturnAllTeachers() {
        assertEquals(Arrays.asList(TestData.teacher1, TestData.teacher2), teacherDao.getAll());
    }

    @Test
    @Transactional
    public void givenPageable_whenGetAll_thenReturnAllTeachers() {
        List<Teacher> teachers = Arrays.asList(TestData.teacher1, TestData.teacher2);
        Pageable pageable = PageRequest.of(0, teachers.size());
        Page<Teacher> teacherPage = new PageImpl<Teacher>(teachers, pageable, teachers.size());

        assertEquals(teacherPage, teacherDao.getAll(pageable));
    }

    @Test
    @Transactional
    public void givenFirstNameAndLastName_whenGetByFirstNameAndLastName_thenReturn() {
        Teacher expected = TestData.teacher1;

        Optional<Teacher> actual = teacherDao.getByFirstNameAndLastName(expected.getFirstName(), expected.getLastName());

        assertEquals(expected, actual.get());
    }

    @Test
    @Transactional
    public void whenCount_thenReturn() {
        assertEquals(2, teacherDao.count());
    }

    interface TestData {
        Address address = new Address.Builder()
            .setCountry("Russia")
            .setCity("Saint Petersburg")
            .setStreet("Nevsky Prospect")
            .setHouseNumber("15")
            .setApartmentNumber("45")
            .setPostcode("342423")
            .setId(1)
            .build();
        Teacher teacher1 = new Teacher.Builder()
            .setFirstName("Mike")
            .setLastName("Miller")
            .setBirtDate(LocalDate.of(1977, 5, 13))
            .setGender(Gender.MALE)
            .setAddress(address)
            .setPhoneNumber("5435345334")
            .setEmail("miller77@gmail.com")
            .setAcademicDegree(AcademicDegree.MASTER)
            .setCourses(new ArrayList<>())
            .setVacations(new ArrayList<>())
            .setId(1)
            .build();
        Teacher teacher2 = new Teacher.Builder()
            .setFirstName("Bob")
            .setLastName("King")
            .setBirtDate(LocalDate.of(1965, 11, 21))
            .setGender(Gender.MALE)
            .setAddress(address)
            .setPhoneNumber("5345345")
            .setEmail("king65@gmail.com")
            .setAcademicDegree(AcademicDegree.DOCTORAL)
            .setCourses(new ArrayList<>())
            .setVacations(new ArrayList<>())
            .setId(2)
            .build();
    }

}
