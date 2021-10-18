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

        university.getTeachers().addAll(dataSource.getTeachers());
        university.getClassrooms().addAll(dataSource.getClassrooms());
        university.getGroups().addAll(dataSource.getGroups());
        university.getStudents().addAll(dataSource.getStudents());
        university.getCourses().addAll(dataSource.getCourses("courses.txt"));
        university.getTimes().addAll(dataSource.getTime());

		Menu menu = new Menu();
		menu.getMenu(university);




    }

}
