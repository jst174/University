package ua.com.foxminded.university;

import static java.lang.System.lineSeparator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import ua.com.foxminded.university.model.AcademicDegree;
import ua.com.foxminded.university.model.Address;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Gender;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.Time;
import ua.com.foxminded.university.model.University;

public class Menu {

	public void getMenu(University university) throws Exception {
		Scanner scanner = new Scanner(System.in);
		createMenu();
		System.out.println("choose item");
		String inputMenuItem = scanner.nextLine();
		while (!inputMenuItem.equals("quit")) {
			if (inputMenuItem.equals("1")) {
				createSubmenu();
				String inputSubmenuItem = scanner.nextLine();
				while (!inputSubmenuItem.equals("back")) {
					if (inputSubmenuItem.equals("1")) {
						addNewTeacher(scanner, university);
					} else if (inputSubmenuItem.equals("2")) {
						showAllTeachers(university);
					} else if (inputSubmenuItem.equals("3")) {
						removeTeacher(scanner, university);
					} else if (inputSubmenuItem.equals("4")) {
						updateTeacher(scanner, university);
					}

					createSubmenu();
					inputSubmenuItem = scanner.nextLine();
				}

			} else if (inputMenuItem.equals("2")) {
				createSubmenu();
				String inputSubmenuItem = scanner.nextLine();
				while (!inputSubmenuItem.equals("back")) {
					if (inputSubmenuItem.equals("1")) {
						addNewGroup(scanner, university);
					} else if (inputSubmenuItem.equals("2")) {
						showAllGroups(university);
					} else if (inputSubmenuItem.equals("3")) {
						removeGroup(scanner, university);
					} else if (inputSubmenuItem.equals("4")) {
						updateTeacher(scanner, university);
					}

					createSubmenu();
					inputSubmenuItem = scanner.nextLine();
				}

			} else if (inputMenuItem.equals("3")) {
				createSubmenu();
				String inputSubmenuItem = scanner.nextLine();
				while (!inputSubmenuItem.equals("back")) {
					if (inputSubmenuItem.equals("1")) {
						addNewCourse(scanner, university);
					} else if (inputSubmenuItem.equals("2")) {
						showAllCourses(scanner, university);
					} else if (inputSubmenuItem.equals("3")) {
						removeCourse(scanner, university);
					} else if (inputSubmenuItem.equals("4")) {

					}
					createSubmenu();
					inputSubmenuItem = scanner.nextLine();
				}
			} else if (inputMenuItem.equals("4")) {
				createSubmenu();
				String inputSubmenuItem = scanner.nextLine();
				while (!inputSubmenuItem.equals("back")) {
					if (inputSubmenuItem.equals("1")) {
						addNewStudent(scanner, university);
					} else if (inputSubmenuItem.equals("2")) {
						showAllStudents(scanner, university);
					} else if (inputSubmenuItem.equals("3")) {
						removeStudent(scanner, university);
					} else if (inputSubmenuItem.equals("4")) {
						updateStudent(scanner, university);
					}

					createSubmenu();
					inputSubmenuItem = scanner.nextLine();
				}
			} else if (inputMenuItem.equals("5")) {
				createSubmenu();
				String inputSubmenuItem = scanner.nextLine();
				while (!inputSubmenuItem.equals("back")) {
					if (inputSubmenuItem.equals("1")) {
						addNewClassroom(scanner, university);
					} else if (inputSubmenuItem.equals("2")) {
						showAllClassrooms(university);
					} else if (inputSubmenuItem.equals("3")) {
						removeClassroom(scanner, university);
					} else if (inputSubmenuItem.equals("4")) {
						updateClassroom(scanner, university);
					}

					createSubmenu();
					inputSubmenuItem = scanner.nextLine();
				}
			} else if (inputMenuItem.equals("6")) {
				createSubmenu();
				String inputSubmenuItem = scanner.nextLine();
				while (!inputSubmenuItem.equals("back")) {
					if (inputSubmenuItem.equals("1")) {
						addNewLesson(scanner, university);
					} else if (inputSubmenuItem.equals("2")) {
						ScheduleFormater formater = new ScheduleFormater();
						System.out.println(formater.format(university.getLessons()));
					} else if (inputSubmenuItem.equals("3")) {
						removeLesson(scanner, university);
					} else if (inputSubmenuItem.equals("4")) {
						updateLesson(scanner, university);
					}

					createSubmenu();
					inputSubmenuItem = scanner.nextLine();
				}

			}
			createMenu();
			System.out.println("choose item");
			inputMenuItem = scanner.nextLine();

		}
		scanner.close();
	}

	private void createMenu() {
		StringBuilder menu = new StringBuilder();
		menu.append("1. Teachers" + lineSeparator()).append("2. Groups" + lineSeparator())
				.append("3. Course" + lineSeparator()).append("4. Students" + lineSeparator())
				.append("5. Classroom" + lineSeparator()).append("6. Lessons");
		System.out.println(menu.toString());
	}

	private void createSubmenu() {
		StringBuilder menu = new StringBuilder();
		menu.append("1. Create" + lineSeparator()).append("2. Show all" + lineSeparator())
				.append("3. Remove" + lineSeparator()).append("4. Update" + lineSeparator()).append("choose item");
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
		String inputGender = scanner.nextLine();
		Gender gender = getGender(inputGender);
		System.out.println("enter adress (country, city, street, house number, apartament number, postcode)");
		String inputAdress = scanner.nextLine();
		String[] array = inputAdress.split(", ");
		Address adress = new Address(array[0], array[1], array[2], array[3], array[4], array[5]);
		System.out.println("enter phone number");
		String phoneNumber = scanner.nextLine();
		System.out.println("enter email");
		String email = scanner.nextLine();
		Student student = new Student(firstName, lastName, bithday, gender, adress, phoneNumber, email);
		university.getStudents().add(student);
	}

	private void showAllStudents(Scanner scanner, University university) {
		for (Student student : university.getStudents()) {
			System.out.println(student);
		}
	}

	private void removeStudent(Scanner scanner, University university) {
		System.out.println("enter student's last name");
		String lastName = scanner.nextLine();
		System.out.println("enter student's first name");
		String firstName = scanner.nextLine();
		for (Iterator<Student> iterator = university.getStudents().iterator(); iterator.hasNext();) {
			Student student = iterator.next();
			if (lastName.equals(student.getLastName()) && firstName.equals(student.getFirstName())) {
				iterator.remove();
			}
		}
	}

	private void updateStudent(Scanner scanner, University university) {
		System.out.println("enter student's last name");
		String lastName = scanner.nextLine();
		System.out.println("enter student's first name");
		String firstName = scanner.nextLine();
		Student updateStudent = null;
		for (Student student : university.getStudents()) {
			if (lastName.equals(student.getLastName()) && firstName.equals(student.getFirstName())) {
				updateStudent = student;
				break;
			}
		}
		Scanner scanner2 = new Scanner(System.in);
		showItemsForUpdateStudent();
		String input = scanner2.nextLine();
		if (input.equals("a")) {
			System.out.println("enter adress (country, city, street, house number, apartament number, postcode)");
			String inputAdress = scanner2.nextLine();
			String[] array = inputAdress.split(", ");
			Address adress = new Address(array[0], array[1], array[2], array[3], array[4], array[5]);
			updateStudent.setAdress(adress);
		} else if (input.equals("b")) {
			System.out.println("enter phone number");
			String phoneNumber = scanner2.nextLine();
			updateStudent.setPhoneNumber(phoneNumber);
		} else if (input.equals("c")) {
			System.out.println("enter email");
			String email = scanner2.nextLine();
			updateStudent.setEmail(email);
		} else if (input.equals("d")) {
			System.out.println("enter group name");
			String groupName = scanner.nextLine();
			for (Group group : university.getGroups()) {
				if (groupName.equals(group.getName())) {
					updateStudent.setGroup(group);
					break;
				}
			}
		}
	}

	private void showItemsForUpdateStudent() {
		StringBuilder items = new StringBuilder();
		items.append("a. Adress" + lineSeparator()).append("b. number of phone" + lineSeparator())
				.append("c. email" + lineSeparator()).append("d. group");

		System.out.println(items.toString());
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
		String inputGender = scanner.nextLine();
		Gender gender = getGender(inputGender);
		System.out.println("enter adress (country, city, street, house number, apartament number, postcode)");
		String inputAdress = scanner.nextLine();
		String[] array = inputAdress.split(", ");
		Address adress = new Address(array[0], array[1], array[2], array[3], array[4], array[5]);
		System.out.println("enter phone number");
		String phoneNumber = scanner.nextLine();
		System.out.println("enter email");
		String email = scanner.nextLine();
		System.out.println("enter academic degree");
		String inputAcademicDegree = scanner.nextLine();
		AcademicDegree academicDegree = getAcademicDegree(inputAcademicDegree);
		Teacher teacher = new Teacher(firstName, lastName, bithday, gender, adress, phoneNumber, email, academicDegree);
		university.getTeachers().add(teacher);
	}

	private void showAllTeachers(University university) {
		for (Teacher teacher : university.getTeachers()) {
			System.out.println(teacher);
		}
	}

	private void removeTeacher(Scanner scanner, University university) {
		System.out.println("enter teacher's last name");
		String lastName = scanner.nextLine();
		System.out.println("enter teacher's first name");
		String firstName = scanner.nextLine();
		for (Iterator<Teacher> iterator = university.getTeachers().iterator(); iterator.hasNext();) {
			Teacher teacher = iterator.next();
			if (lastName.equals(teacher.getLastName()) && firstName.equals(teacher.getFirstName())) {
				iterator.remove();
				break;
			}
		}
	}

	private void updateTeacher(Scanner scanner, University university) {
		System.out.println("enter teacher's last name");
		String lastName = scanner.nextLine();
		System.out.println("enter teacher's first name");
		String firstName = scanner.nextLine();
		Teacher updateTeacher = null;
		for (Teacher teacher : university.getTeachers()) {
			if (lastName.equals(teacher.getLastName()) && firstName.equals(teacher.getFirstName())) {
				updateTeacher = teacher;
				break;
			}
		}
		Scanner scanner2 = new Scanner(System.in);
		showItemsForUpdateTeacher();
		String input = scanner2.nextLine();
		if (input.equals("a")) {
			System.out.println("enter adress (country, city, street, house number, apartament number, postcode)");
			String inputAdress = scanner2.nextLine();
			String[] array = inputAdress.split(", ");
			Address adress = new Address(array[0], array[1], array[2], array[3], array[4], array[5]);
			updateTeacher.setAdress(adress);
		} else if (input.equals("b")) {
			System.out.println("enter phone number");
			String phoneNumber = scanner2.nextLine();
			updateTeacher.setPhoneNumber(phoneNumber);
		} else if (input.equals("c")) {
			System.out.println("enter email");
			String email = scanner2.nextLine();
			updateTeacher.setEmail(email);
		} else if (input.equals("d")) {
			System.out.println("enter academic degree");
			String inputAcademicDegree = scanner2.nextLine();
			AcademicDegree academicDegree = getAcademicDegree(inputAcademicDegree);
			updateTeacher.setAcademicDegree(academicDegree);
		}

	}

	private void showItemsForUpdateTeacher() {
		StringBuilder items = new StringBuilder();
		items.append("a. Adress" + lineSeparator()).append("b. number of phone" + lineSeparator())
				.append("c. email" + lineSeparator()).append("d. academic degree");

		System.out.println(items.toString());
	}

	private void addNewGroup(Scanner scanner, University university) {
		System.out.println("enter group's name");
		String name = scanner.nextLine();
		Group group = new Group(name);
		university.getGroups().add(group);
	}

	private void showAllGroups(University university) {
		for (Group group : university.getGroups()) {
			System.out.println(group);
		}
	}

	private void removeGroup(Scanner scanner, University university) {
		System.out.println("enter group's name");
		String name = scanner.nextLine();
		for (Iterator<Group> iterator = university.getGroups().iterator(); iterator.hasNext();) {
			Group group = iterator.next();
			if (name.equals(group.getName())) {
				iterator.remove();
			}
		}
	}

	private void updateGroup(Scanner scanner, University university) {
		System.out.println("enter group name");
		String groupName = scanner.nextLine();
		Group updateGroup = null;
		for (Group group : university.getGroups()) {
			if (groupName.equals(group.getName())) {
				updateGroup = group;
				break;
			}
		}
		System.out.println("enter new group name");
		String name = scanner.nextLine();
		updateGroup.setName(name);
	}

	private void addNewCourse(Scanner scanner, University university) {
		System.out.println("enter course's name");
		String name = scanner.nextLine();
		Course course = new Course(name);
		university.getCourses().add(course);
	}

	private void showAllCourses(Scanner scanner, University university) {
		for (Course course : university.getCourses()) {
			System.out.println(course);
		}
	}

	private void removeCourse(Scanner scanner, University university) {
		System.out.println("enter course's name");
		String name = scanner.nextLine();
		for (Iterator<Course> iterator = university.getCourses().iterator(); iterator.hasNext();) {
			Course course = iterator.next();
			if (name.equals(course.getName())) {
				iterator.remove();
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

	private void showAllClassrooms(University university) {
		for (Classroom classroom : university.getClassrooms()) {
			System.out.println(classroom);
		}
	}

	private void removeClassroom(Scanner scanner, University university) {
		System.out.println("enter clasroom's number");
		int number = scanner.nextInt();
		for (Iterator<Classroom> iterator = university.getClassrooms().iterator(); iterator.hasNext();) {
			Classroom classroom = iterator.next();
			if (number == classroom.getId()) {
				iterator.remove();
			}
		}
	}

	private void updateClassroom(Scanner scanner, University university) {
		System.out.println("enter classroom number");
		int number = scanner.nextInt();
		Classroom updateClassroom = null;
		for (Classroom classroom : university.getClassrooms()) {
			if (number == classroom.getNumber()) {
				updateClassroom = classroom;
				break;
			}
		}
		System.out.println("enter capacity");
		int capacity = scanner.nextInt();
		updateClassroom.setCapacity(capacity);
	}

	private void addNewLesson(Scanner scanner, University university) {
		System.out.println("enter date yyyy-MM-dd");
		String inputDate = scanner.nextLine();
		LocalDate date = formatDate(inputDate);
		System.out.println("enter time HH:mm");
		String inputTime = scanner.nextLine();
		LocalTime startTime = formatTime(inputTime);
		Time timeOfLesson = null;
		for (Time time : university.getTimes()) {
			if (startTime.equals(time.getStartTime())) {
				timeOfLesson = time;
			}
		}
		System.out.println("enter course name");
		String courseName = scanner.nextLine();
		Course courseOfLesson = null;
		for (Course course : university.getCourses()) {
			if (courseName.equals(course.getName())) {
				courseOfLesson = course;
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
				}
			}
		}

		System.out.println("enter classroom number");
		int classroomNumber = scanner.nextInt();
		Classroom classroomOfLesson = null;
		for (Classroom classroom : university.getClassrooms()) {
			if (classroomNumber == classroom.getNumber()) {
				classroomOfLesson = classroom;
			}
		}

		System.out.println("enter teacher last name");
		String lastName = scanner.nextLine();
		System.out.println("enter teacher first name");
		String firstName = scanner.nextLine();
		Teacher teacherOfLesson = null;
		for (Teacher teacher : university.getTeachers()) {
			if (lastName.equals(teacher.getLastName()) && firstName.equals(teacher.getFirstName())) {
				teacherOfLesson = teacher;
			}
		}

		Lesson lesson = new Lesson(courseOfLesson, classroomOfLesson, teacherOfLesson, date, timeOfLesson);

		university.getLessons().add(lesson);
	}

	private void removeLesson(Scanner scanner, University university) {
		System.out.println("enter lesson date yyyy-MM-dd");
		String inputDate = scanner.nextLine();
		LocalDate date = formatDate(inputDate);
		System.out.println("enter lesson time HH:mm");
		String inputTime = scanner.nextLine();
		LocalTime startTime = formatTime(inputTime);
		for (Iterator<Lesson> iterator = university.getLessons().iterator(); iterator.hasNext();) {
			Lesson lesson = iterator.next();
			if (date.equals(lesson.getDate()) && startTime.equals(lesson.getTime().getStartTime())) {
				iterator.remove();
			}
		}
	}

	private void updateLesson(Scanner scanner, University university) {
		System.out.println("enter lesson date yyyy-MM-dd");
		String inputDate = scanner.nextLine();
		LocalDate date = formatDate(inputDate);
		System.out.println("enter lesson time HH:mm");
		String inputTime = scanner.nextLine();
		LocalTime startTime = formatTime(inputTime);
		Lesson updateLesson = null;
		for (Lesson lesson : university.getLessons()) {
			if (date.equals(lesson.getDate()) && startTime.equals(lesson.getTime())) {
				updateLesson = lesson;
				break;
			}
		}
		Scanner scanner2 = new Scanner(System.in);
		showItemsForUpdateLesson();
		String input = scanner2.nextLine();
		scanner.nextLine();
		if (input.equals("a")) {
			System.out.println("enter date yyyy-mm-dd");
			String inputNewDate = scanner.nextLine();
			LocalDate newDate = formatDate(inputNewDate);
			updateLesson.setDate(newDate);
		} else if (input.equals("b")) {
			System.out.println("enter time HH:mm");
			String inputNewTime = scanner.nextLine();
			LocalTime newTime = formatTime(inputNewTime);
			Time timeOfLesson = null;
			for (Time time : university.getTimes()) {
				if (newTime.equals(time.getStartTime())) {
					updateLesson.setTime(time);
				}
			}
		} else if (input.equals("c")) {
			System.out.println("enter course name");
			String courseName = scanner.nextLine();
			for (Course course : university.getCourses()) {
				if (courseName.equals(course.getName())) {
					updateLesson.setCourse(course);
					break;
				}
			}
		} else if (input.equals("d")) {
			System.out.println("enter group name");
			String groupName = scanner.nextLine();
			for (Group group : university.getGroups()) {
				if (groupName.equals(group.getName())) {
					updateLesson.getGroups().add(group);
				}
			}
		} else if (input.equals("e")) {
			System.out.println("enter group name");
			String groupName = scanner.nextLine();
			for (Iterator<Group> iterator = updateLesson.getGroups().iterator(); iterator.hasNext();) {
				Group group = iterator.next();
				if (groupName.equals(group.getName())) {
					iterator.remove();
				}
			}
		} else if (input.equals("f")) {
			System.out.println("enter classroom number");
			int number = scanner.nextInt();
			for (Classroom classroom : university.getClassrooms()) {
				if (number == classroom.getNumber()) {
					updateLesson.setClassroom(classroom);
				}
			}
		} else if (input.equals("g")) {
			System.out.println("enter teacher last name");
			String lastName = scanner.nextLine();
			System.out.println("enter teacher first name");
			String firstName = scanner.nextLine();
			for (Teacher teacher : university.getTeachers()) {
				if (lastName.equals(teacher.getLastName()) && firstName.equals(teacher.getFirstName())) {
					updateLesson.setTeacher(teacher);
				}
			}
		}

	}

	private void showItemsForUpdateLesson() {
		StringBuilder items = new StringBuilder();
		items.append("a. Date" + lineSeparator()).append("b. Time" + lineSeparator())
				.append("c. Course" + lineSeparator()).append("d. Add group" + lineSeparator())
				.append("e. Remove group" + lineSeparator()).append("f. Classroom" + lineSeparator())
				.append("g. Teacher");

		System.out.println(items.toString());
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

	private Gender getGender(String inputGender) {
		if (inputGender.equals(Gender.MALE.toString())) {
			return Gender.MALE;
		} else if (inputGender.equals(Gender.FAMALE.toString())) {
			return Gender.FAMALE;
		} else {
			throw new IllegalArgumentException("gender is incorrect");
		}
	}

	private AcademicDegree getAcademicDegree(String input) {
		if (input.equals(AcademicDegree.ASSOCIATE.toString())) {
			return AcademicDegree.ASSOCIATE;
		} else if (input.equals(AcademicDegree.BACHELOR.toString())) {
			return AcademicDegree.BACHELOR;
		} else if (input.equals(AcademicDegree.MASTER.toString())) {
			return AcademicDegree.MASTER;
		} else if (input.equals(AcademicDegree.DOCTORAL.toString())) {
			return AcademicDegree.DOCTORAL;
		} else {
			throw new IllegalArgumentException("gender is incorrect");
		}
	}

}
