package ua.com.foxminded.university.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ua.com.foxminded.university.model.Classroom;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ClassroomRepositoryTest {

    @Autowired
    private ClassroomRepository classroomRepository;



    @Test
    public void givenClassroomNumber_whereGetByNumber_thenReturn() {
        Classroom expected = new Classroom(102, 30);

        classroomRepository.save(expected);

        Optional<Classroom> actual = classroomRepository.findByNumber(102);

        assertThat(actual.get()).isEqualTo(expected);
    }

}
