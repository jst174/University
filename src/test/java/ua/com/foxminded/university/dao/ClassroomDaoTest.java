package ua.com.foxminded.university.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ua.com.foxminded.university.model.Classroom;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql({"/create_classroom_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ClassroomDaoTest {

    @Autowired
    private ClassroomDao classroomDao;

    @Test
    public void givenClassroomNumber_whereGetByNumber_thenReturn() {
        Classroom expected = new Classroom(102, 30);

        Optional<Classroom> actual = classroomDao.findByNumber(102);

        assertEquals(expected, actual.get());
    }

}
