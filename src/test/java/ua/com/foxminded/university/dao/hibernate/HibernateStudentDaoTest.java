package ua.com.foxminded.university.dao.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfigTest.class})
@Sql({"/create_address_test.sql", "/create_groups_test.sql", "/create_student_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class HibernateStudentDaoTest {

    @Autowired
    private StudentDao studentDao;
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Test
    @Transactional
    public void givenNewStudent_whenCreate_thenCreated() throws IOException {
        Student student = new Student.Builder().clone(TestData.student1)
            .setLastName("King")
            .setEmail("king97@yandex.ru")
            .setPhoneNumber("3622366")
            .build();

        studentDao.create(student);

        assertEquals(student, hibernateTemplate.get(Student.class, student.getId()));
    }

    @Test
    @Transactional
    public void givenId_whenGetById_thenReturn() {
        assertEquals(TestData.student1, studentDao.getById(1).get());
    }

    @Test
    @Transactional
    public void givenUpdatedStudent_whenUpdate_thenUpdated() {
        Student updatedStudent = new Student(
            "Mike",
            "King",
            LocalDate.of(1997, 5, 13),
            Gender.MALE,
            TestData.address,
            "3622366",
            "king97@yandex.ru"
        );
        updatedStudent.setGroup(TestData.group);
        updatedStudent.setId(1);

        studentDao.update(updatedStudent);

        assertEquals(updatedStudent, hibernateTemplate.get(Student.class, updatedStudent.getId()));

    }

    @Test
    @Transactional
    public void givenId_whenDelete_thenDeleted() {
        if (hibernateTemplate.get(Student.class, 1) != null) {
            studentDao.delete(1);
            hibernateTemplate.clear();
        }

        assertNull(hibernateTemplate.get(Student.class, 1));
    }

    @Test
    @Transactional
    public void whenGetAll_thenReturnAllStudents() {
        assertEquals(Arrays.asList(TestData.student1, TestData.student2), studentDao.getAll());
    }

    @Test
    @Transactional
    public void givenPageable_whenGetAll_thenReturnAllStudents() {
        List<Student> students = Arrays.asList(TestData.student1, TestData.student2);
        Pageable pageable = PageRequest.of(0, students.size());
        Page<Student> studentPage = new PageImpl<Student>(students, pageable, students.size());

        assertEquals(studentPage, studentDao.getAll(pageable));
    }

    @Test
    @Transactional
    public void givenFirstNameAndLastName_whenGetByFirstNameAndLastName_thenReturn() {
        Student student = TestData.student1;

        Optional<Student> actual = studentDao.getByFirstNameAndLastName(student.getFirstName(), student.getLastName());

        assertEquals(student, actual.get());
    }

    @Test
    @Transactional
    public void whenCount_thenReturn() {
        assertEquals(2, studentDao.count());
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
        Group group = new Group.Builder()
            .setName("MJ-12")
            .build();
        Student student1 = new Student.Builder()
            .setFirstName("Mike")
            .setLastName("Miller")
            .setBirtDate(LocalDate.of(1997, 5, 13))
            .setGender(Gender.MALE)
            .setAddress(address)
            .setPhoneNumber("5435345334")
            .setEmail("miller97@gmail.com")
            .setGroup(group)
            .build();
        Student student2 = new Student.Builder()
            .setFirstName("Steve")
            .setLastName("King")
            .setBirtDate(LocalDate.of(1995, 5, 2))
            .setGender(Gender.MALE)
            .setAddress(address)
            .setPhoneNumber("432423432")
            .setEmail("king95@gmail.com")
            .setGroup(group)
            .build();

    }
}
