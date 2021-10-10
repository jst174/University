package ua.com.foxminded.university;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.config.DatabaseConfig;
import ua.com.foxminded.university.dao.TimeDao;
import ua.com.foxminded.university.dao.jdbc.*;
import ua.com.foxminded.university.model.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class Application {

    public static void main(String[] args) throws Exception {
        University university = new University();
        DataSource dataSource = new DataSource();

        ApplicationContext context = new AnnotationConfigApplicationContext(DatabaseConfig.class);

        JdbcTimeDao time = context.getBean(JdbcTimeDao.class);
        time.create(new Time(LocalTime.of(8,0), LocalTime.of(9, 30)));
        time.create(new Time(LocalTime.of(12,0), LocalTime.of(13, 30)));

        Time time1 = time.getById(1);

        time1.setStartTime(LocalTime.of(8,30));
        time1.setEndTime(LocalTime.of(10,0));

        time.update(time1);


        university.getTeachers().addAll(dataSource.getTeachers());
        university.getClassrooms().addAll(dataSource.getClassrooms());
        university.getGroups().addAll(dataSource.getGroups());
        university.getStudents().addAll(dataSource.getStudents());
        university.getCourses().addAll(dataSource.getCourses("courses.txt"));
        university.getTimes().addAll(dataSource.getTime());

//		Menu menu = new Menu();
//		menu.getMenu(university);




    }

}
