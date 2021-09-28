package ua.com.foxminded.university;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.Dao.AddressDao;
import ua.com.foxminded.university.Dao.StudentDao;
import ua.com.foxminded.university.Dao.TeacherDao;
import ua.com.foxminded.university.Dao.VacationDao;
import ua.com.foxminded.university.SpringConfig;
import ua.com.foxminded.university.model.*;

public class Application {

	public static void main(String[] args) throws Exception {
		University university = new University();
		DataSourse dataSourse = new DataSourse();

		university.getTeachers().addAll(dataSourse.getTeachers());
		university.getClassrooms().addAll(dataSourse.getClassrooms());
		university.getGroups().addAll(dataSourse.getGroups());
		university.getStudents().addAll(dataSourse.getStudents());
		university.getCourses().addAll(dataSourse.getCourses("courses.txt"));
		university.getTimes().addAll(dataSourse.getTime());

		Menu menu = new Menu();
		menu.getMenu(university);
	}

}
