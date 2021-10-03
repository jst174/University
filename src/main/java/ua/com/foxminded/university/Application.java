package ua.com.foxminded.university;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.config.SpringConfig;
import ua.com.foxminded.university.dao.jdbc.*;
import ua.com.foxminded.university.model.*;

import java.time.LocalDate;

public class Application {

    public static void main(String[] args) throws Exception {
        University university = new University();
        DataSource dataSource = new DataSource();




        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        SqlScript sqlScript = new SqlScript(context.getBean(javax.sql.DataSource.class));
        sqlScript.executeScript("schema.sql");

        university.getTeachers().addAll(dataSource.getTeachers());
        university.getClassrooms().addAll(dataSource.getClassrooms());
        university.getGroups().addAll(dataSource.getGroups());
        university.getStudents().addAll(dataSource.getStudents());
        university.getCourses().addAll(dataSource.getCourses("courses.txt"));
        university.getTimes().addAll(dataSource.getTime());

//		Menu menu = new Menu();
//		menu.getMenu(university);
        JdbcStudentDao studentDao = context.getBean(JdbcStudentDao.class);
        JdbcAddressDao addressDao = context.getBean(JdbcAddressDao.class);
        JdbcTeacherDao teacherDao = context.getBean(JdbcTeacherDao.class);
        JdbcVacationDao vacationDao = context.getBean(JdbcVacationDao.class);
        JdbcGroupDao groupDao = context.getBean(JdbcGroupDao.class);

        Student student1 = dataSource.generateStudent();
        Teacher teacher1 = dataSource.generateTeacher();
        Vacation vacation = new Vacation(
            LocalDate.of(2021, 12, 5),
            LocalDate.of(2021, 12, 10));


        groupDao.create(new Group("fdsfds"));

        addressDao.create(student1.getAdress());
        studentDao.create(student1, 1);



    }

}
