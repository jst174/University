package ua.com.foxminded;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ua.com.foxminded.model.Classroom;
import ua.com.foxminded.model.Course;
import ua.com.foxminded.model.Group;
import ua.com.foxminded.model.Lesson;
import ua.com.foxminded.model.Student;
import ua.com.foxminded.model.Teacher;
import ua.com.foxminded.model.Time;
import ua.com.foxminded.model.University;

public class Application {

	public static void main(String[] args) throws Exception {
		University university = new University();
		DataSourse dataSourse = new DataSourse();

		ScheduleFormater scheduleFormater = new ScheduleFormater();

		university.getTeachers().addAll(dataSourse.getTeachers());
		university.getClassrooms().addAll(dataSourse.getClassrooms());
		university.getGroups().addAll(dataSourse.getGroups());
		university.getStudents().addAll(dataSourse.getStudents());

		Menu menu = new Menu();
		menu.getMenu(university);
	}

}
