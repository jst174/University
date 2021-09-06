package ua.com.foxminded;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Time;
import ua.com.foxminded.university.model.University;

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
