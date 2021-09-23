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

//		Menu menu = new Menu();
//		menu.getMenu(university);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

        StudentDao studentDao = context.getBean(StudentDao.class);
        AddressDao addressDao = context.getBean(AddressDao.class);
        TeacherDao teacherDao = context.getBean(TeacherDao.class);
        VacationDao vacationDao = context.getBean(VacationDao.class);

        Student student1 = dataSourse.generateStudent();
        Teacher teacher1 = dataSourse.generateTeacher();
        Vacation vacation = new Vacation(LocalDate.of(2021,12,5), LocalDate.of(2021,12,10));


        addressDao.create(student1.getAdress());
        studentDao.create(student1,1);

        System.out.println(studentDao.getById(1));

        addressDao.create(teacher1.getAdress());
        teacherDao.create(teacher1, 2);

        System.out.println(teacherDao.getById(1));

        vacationDao.create(vacation);

        Teacher updaterTeacher = teacherDao.getById(1);
        teacherDao.update(updaterTeacher,1);

        System.out.println(teacherDao.getById(1).getVacations());






        context.close();
	}

}
