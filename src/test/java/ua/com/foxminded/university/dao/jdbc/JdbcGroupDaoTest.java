package ua.com.foxminded.university.dao.jdbc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.university.config.SpringConfigTest;
import ua.com.foxminded.university.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.university.model.Group;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfigTest.class})
@Sql({"/create_groups_test.sql", "/create_students_for_group_test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JdbcGroupDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcGroupDao groupDao;

    @Test
    public void givenNewGroup_whenCreate_thenCreated() {
        Group group = new Group("MJ-12");

        groupDao.create(group);

        assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups"));

    }

    @Test
    public void givenId_whenGetById_thenReturn() {
        Group group = new Group("MJ-12");

        assertEquals(group, groupDao.getById(1));
    }

    @Test
    public void givenUpdatedCroupAndId_thenUpdated() {
        String SQL = "SELECT COUNT(0) FROM groups WHERE name = 'JD-32'";
        Group updatedGroup = new Group("JD-32");
        int beforeUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups", SQL);

        groupDao.update(1, updatedGroup);

        int afterUpdate = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "groups", SQL);
        assertEquals(1, afterUpdate);
    }

    @Test
    public void givenId_whenDelete_thenDeleted() {
        groupDao.delete(1);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups"));
    }

    @Test
    public void givenGroupIdAndStudentId_whenAddStudent_thenAddStudentToGroup() {
        groupDao.addStudent(1, 1);
        groupDao.addStudent(1, 2);

        assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "group_students"));

    }

    @Test
    public void whenGetAll_thenReturnAllGroups(){
        Group group1 = new Group("MJ-12");
        Group group2 = new Group("FM-22");
        List<Group> expected = new ArrayList<>();
        expected.add(group1);
        expected.add(group2);

        List<Group> actual = groupDao.getAll();

        assertEquals(expected, actual);
    }


}
