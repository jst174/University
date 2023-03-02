package ua.com.foxminded.university.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ua.com.foxminded.university.model.Group;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Sql({"/create_lesson_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;

    @Test
    public void givenGroupName_whenGetByName_thenReturn() {
        Group group = new Group("MH-12");

        assertEquals(group, groupRepository.findByName("MH-12").get());
    }
}
