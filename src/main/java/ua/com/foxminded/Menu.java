package ua.com.foxminded;

import static java.lang.System.lineSeparator;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import ua.com.foxminded.model.Adress;
import ua.com.foxminded.model.Classroom;
import ua.com.foxminded.model.Course;
import ua.com.foxminded.model.Group;
import ua.com.foxminded.model.Lesson;
import ua.com.foxminded.model.Student;
import ua.com.foxminded.model.Teacher;
import ua.com.foxminded.model.Time;
import ua.com.foxminded.model.University;

public class Menu {

	public void getMenu(University university) throws Exception {
		Scanner scanner = new Scanner(System.in);
		createItems();
		System.out.println("choose item");
		String input = scanner.nextLine();
		while (!input.equals("quit")) {
			if (input.equals("1")) {
				addNewStudent(scanner, university);
			} else if (input.equals("2")) {
				removeStudent(scanner, university);
			} else if (input.equals("3")) {
				addNewTeacher(scanner, university);
			} else if (input.equals("4")) {
				removeTeacher(scanner, university);
			} else if (input.equals("5")) {
				addNewGroup(scanner, university);
			} else if (input.equals("6")) {
				removeGroup(scanner, university);
			} else if (input.equals("7")) {
				addNewCourse(scanner, university);
			} else if (input.equals("8")) {
				removeCourse(scanner, university);
			} else if (input.equals("9")) {
				addNewClassroom(scanner, university);
			} else if (input.equals("10")) {
				removeClassroom(scanner, university);
			} else if (input.equals("11")) {
				addNewLesson(scanner, university);
			} else if (input.equals("12")) {
				removeLesson(scanner, university);
			} else if (input.equals("13")) {
				ScheduleFormater formater = new ScheduleFormater();
				System.out.println(formater.format(university.getLessons()));

			}
			input = scanner.nextLine();
		}
		scanner.close();
	}

	private void createItems() {
		StringBuilder menu = new StringBuilder();
		menu.append("1.add new student" + lineSeparator()).append("2. remove student" + lineSeparator())
				.append("3. add new teacher" + lineSeparator()).append("4. remove teacher" + lineSeparator())
				.append("5. add new group" + lineSeparator()).append("6. remove group" + lineSeparator())
				.append("7. add new course" + lineSeparator()).append("8. remove course" + lineSeparator())
				.append("9. add new classroom" + lineSeparator()).append("10. remove classroom" + lineSeparator())
				.append("11. create a new lesson" + lineSeparator()).append("12. remove lesson")
				.append("13. show the schedule for the group" + lineSeparator());
		System.out.println(menu.toString());
	}

	private void addNewStudent(Scanner scanner, University university) {
		System.out.println("enter first name");
		String firstName = scanner.nextLine();
		System.out.println("enter last name");
		String lastName = scanner.nextLine();
		System.out.println("enter birthday yyyy-mm-dd");
		String inputDate = scanner.nextLine();
		LocalDate bithday = formatDate(inputDate);
		System.out.println("enter gender");
		String gender = scanner.nextLine();
		System.out.println("enter adress (country, city, street, house number, apartament number, postcode)");
		String inputAdress = scanner.nextLine();
		String[] array = inputAdress.split(", ");
		Adress adress = new Adress(array[0], array[1], array[2], array[3], array[4], array[5]);
		System.out.println("enter phone number");
		String phoneNumber = scanner.nextLine();
		System.out.println("enter email");
		String email = scanner.nextLine();
		Student student = new Student(firstName, lastName, bithday, gender, adress, phoneNumber, email);
		university.getStudents().add(student);
	}

	private void removeStudent(Scanner scanner, University university) {
		System.out.println("enter student's id");
		int id = scanner.nextInt();
		for (Student student : university.getStudents()) {
			if (id == student.getId()) {
				university.getStudents().remove(student);
			}
		}
	}

	private void addNewTeacher(Scanner scanner, University university) {
		System.out.println("enter first name");
		String firstName = scanner.nextLine();
		System.out.println("enter last name");
		String lastName = scanner.nextLine();
		System.out.println("enter birthday yyyy-mm-dd");
		String inputDate = scanner.nextLine();
		LocalDate bithday = formatDate(inputDate);
		System.out.println("enter gender");
		String gender = scanner.nextLine();
		System.out.println("enter adress (country, city, street, house number, apartament number, postcode)");
		String inputAdress = scanner.nextLine();
		String[] array = inputAdress.split(", ");
		Adress adress = new Adress(array[0], array[1], array[2], array[3], array[4], array[5]);
		System.out.println("enter phone number");
		String phoneNumber = scanner.nextLine();
		System.out.println("enter email");
		String email = scanner.nextLine();
		System.out.println("enter academic degree");
		String academicDegree = scanner.nextLine();
		Teacher teacher = new Teacher(firstName, lastName, bithday, gender, adress, phoneNumber, email, academicDegree);
		university.getTeachers().add(teacher);
	}

	private void removeTeacher(Scanner scanner, University university) {
		System.out.println("enter teacher's id");
		int id = scanner.nextInt();
		for (Teacher teacher : university.getTeachers()) {
			if (id == teacher.getId()) {
				university.getTeachers().remove(teacher);
			}
		}
	}

	private void addNewGroup(Scanner scanner, University university) {
		System.out.println("enter group's name");
		String name = scanner.nextLine();
		Group group = new Group(name);
		university.getGroups().add(group);
	}

	private void removeGroup(Scanner scanner, University university) {
		System.out.println("enter group's id");
		int id = scanner.nextInt();
		for (Group group : university.getGroups()) {
			if (id == group.getId()) {
				university.getGroups().remove(group);
			}
		}
	}

	private void addNewCourse(Scanner scanner, University university) {
		System.out.println("enter course's name");
		String name = scanner.nextLine();
		Course course = new Course(name);
		university.getCourses().add(course);
	}

	private void removeCourse(Scanner scanner, University university) {
		System.out.println("enter course's id");
		int id = scanner.nextInt();
		for (Course course : university.getCourses()) {
			if (id == course.getId()) {
				university.getGroups().remove(course);
			}
		}
	}

	private void addNewClassroom(Scanner scanner, University university) {
		System.out.println("enter classrooms's number");
		int number = scanner.nextInt();
		System.out.println("enter classroom's capacity");
		int capacity = scanner.nextInt();
		Classroom classroom = new Classroom(number, capacity);
		university.getClassrooms().add(classroom);
	}

	private void removeClassroom(Scanner scanner, University university) {
		System.out.println("enter clasroom's id");
		int id = scanner.nextInt();
		for (Classroom classroom : university.getClassrooms()) {
			if (id == classroom.getId()) {
				university.getClassrooms().remove(classroom);
			}
		}
	}

	private void addNewLesson(Scanner scanner, University university) {
		System.out.println("enter date yyyy-mm-dd");
		String inputDate = scanner.nextLine();
		LocalDate date = formatDate(inputDate);
		System.out.println("enter time HH-mm");
		String inputTime = scanner.nextLine();
		LocalTime startTime = formatTime(inputTime);
		LocalTime endTime = startTime.plus(Duration.ofHours(1)).plus(Duration.ofMinutes(30));
		Time timeOfLesson = new Time(startTime, endTime);
		System.out.println("enter course name");
		String courseName = scanner.nextLine();
		Course courseOfLesson = null;
		for (Course course : university.getCourses()) {
			if (courseName.equals(course.getName())) {
				courseOfLesson = course;
			} else {
				throw new IllegalArgumentException("course not found");
			}
		}
		System.out.println("choose a group");
		showGroups(university);
		String inputGroupName = scanner.nextLine();
		List<Group> groups = new ArrayList<>();
		String[] array = inputGroupName.split(", ");
		for (int i = 0; i < array.length; i++) {
			for (Group group : university.getGroups()) {
				if (array[i].equals(group.getName())) {
					groups.add(group);
				} else {
					throw new IllegalArgumentException("group not found");
				}
			}
		}

		System.out.println("enter classroom number");
		int classroomNumber = scanner.nextInt();
		Classroom classroomOfLesson = null;
		for (Classroom classroom : university.getClassrooms()) {
			if (classroomNumber == classroom.getNumber()) {
				classroomOfLesson = classroom;
			} else {
				throw new IllegalArgumentException("classroom not found");
			}
		}

		System.out.println("enter teacher id");
		int teacherId = scanner.nextInt();
		Teacher teacherOfLesson = null;
		for (Teacher teacher : university.getTeachers()) {
			if (teacherId == teacher.getId()) {
				teacherOfLesson = teacher;
			} else {
				throw new IllegalArgumentException("teahcer not found");
			}
		}

		Lesson lesson = new Lesson(courseOfLesson, groups, classroomOfLesson, teacherOfLesson, date, timeOfLesson);

		university.getLessons().add(lesson);
	}

	private void removeLesson(Scanner scanner, University university) {
		System.out.println("enter lesson id");
		int id = scanner.nextInt();
		for (Lesson lesson : university.getLessons()) {
			if (id == lesson.getId()) {
				university.getLessons().remove(lesson);
			}
		}
	}

	private LocalDate formatDate(String input) {
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDate.parse(input, formater);
	}

	private LocalTime formatTime(String input) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return LocalTime.parse(input, formatter);

	}

	private void showGroups(University university) {
		for (Group group : university.getGroups()) {
			System.out.println(group.getName());
		}
	}

}