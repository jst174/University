package ua.com.foxminded.university;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.javafaker.Faker;

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

public class DataSourse {

	private static final char HYPHEN = '-';

	private Faker faker;

	public DataSourse() throws IOException {
		faker = new Faker(new Locale("US"));
	}

	public List<Student> getStudents() throws IOException {
		List<Student> students = new ArrayList<>();
		for (int i = 0; i < 200; i++) {
			students.add(generateStudent());
		}
		return students;
	}

	public List<Teacher> getTeachers() {
		List<Teacher> teachers = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Teacher teacher = generateTeacher();
			teachers.add(teacher);
		}
		return teachers;
	}

	public List<Group> getGroups() {
		List<Group> groups = Arrays.asList(new Group[10]);
		return groups.stream().map(s -> new Group(generateGroupName())).collect(Collectors.toList());
	}

	public List<Course> getCourses(String coursesFile) throws IOException {
		try (Stream<String> coursesStream = Files.lines(Paths.get(getFile(coursesFile).getAbsolutePath()))) {
			return coursesStream.map(s -> new Course(s)).collect(Collectors.toList());
		}
	}

	public List<Time> getTime() {
		List<Time> time = new ArrayList<>();
		time.add(new Time(LocalTime.of(8, 30), LocalTime.of(10, 0)));
		time.add(new Time(LocalTime.of(10, 15), LocalTime.of(11, 45)));
		time.add(new Time(LocalTime.of(12, 00), LocalTime.of(13, 30)));
		time.add(new Time(LocalTime.of(14, 15), LocalTime.of(15, 45)));
		time.add(new Time(LocalTime.of(16, 0), LocalTime.of(17, 30)));
		time.add(new Time(LocalTime.of(17, 45), LocalTime.of(19, 15)));
		time.add(new Time(LocalTime.of(19, 30), LocalTime.of(21, 0)));
		return time;
	}

	public List<Classroom> getClassrooms() {
		List<Classroom> classrooms = new ArrayList<>();
		classrooms.add(new Classroom(401, 30));
		classrooms.add(new Classroom(431, 60));
		classrooms.add(new Classroom(331, 90));
		classrooms.add(new Classroom(339, 60));
		classrooms.add(new Classroom(502, 30));
		classrooms.add(new Classroom(556, 120));
		classrooms.add(new Classroom(202, 60));
		classrooms.add(new Classroom(621, 90));
		classrooms.add(new Classroom(552, 30));
		classrooms.add(new Classroom(122, 60));
		return classrooms;
	}

	public Lesson generateLesson(LocalDate date, List<Group> groups) throws IOException {
		int random1 = faker.number().numberBetween(0, 9);
		int random2 = faker.number().numberBetween(0, 6);
		return new Lesson(getCourses("courses.txt").get(random1), groups, getClassrooms().get(random1),
				getTeachers().get(random1), date, getTime().get(random2));

	}

	public Teacher generateTeacher() {
		String firstName = faker.name().firstName();
		String lastName = faker.name().lastName();
		return new Teacher(firstName, lastName, generateBirthDate(), generateGender(), generateAddress(),
				faker.phoneNumber().phoneNumber(), generateEmail(firstName, lastName), generateAcademicDegrees());

	}

	public Student generateStudent() throws IOException {
		String firstName = faker.name().firstName();
		String lastName = faker.name().lastName();
		return new Student(firstName, lastName, generateBirthDate(), generateGender(), generateAddress(),
				faker.phoneNumber().phoneNumber(), generateEmail(firstName, lastName));

	}

	private Address generateAddress() {
		return new Address(faker.address().country(), faker.address().city(), faker.address().streetName(),
				faker.address().buildingNumber(), faker.address().buildingNumber(), faker.address().zipCode());
	}

	private LocalDate generateBirthDate() {
		long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
		long maxDay = LocalDate.of(2003, 12, 31).toEpochDay();
		long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
		return LocalDate.ofEpochDay(randomDay);
	}

	private Gender generateGender() {
		int random = faker.number().numberBetween(0, 1);
		if (random == 0) {
			return Gender.MALE;
		} else {
			return Gender.FAMALE;
		}
	}

	private String generateEmail(String firstName, String lastName) {
		return firstName + "." + lastName + "@gmail.com";

	}

	private AcademicDegree generateAcademicDegrees() {
		int random = faker.number().numberBetween(0, 3);
		if (random == 0) {
			return AcademicDegree.ASSOCIATE;
		} else if (random == 1) {
			return AcademicDegree.BACHELOR;
		} else if (random == 2) {
			return AcademicDegree.MASTER;
		} else {
			return AcademicDegree.DOCTORAL;
		}
	}

	private String generateGroupName() {
		char nextChar;
		int nextInt;
		Random random = new Random();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 4; i++) {
			if ((i == 0) || (i == 1)) {
				nextChar = (char) (random.nextInt(26) + 65);
				sb.append(nextChar);
			} else if ((i == 3)) {
				nextInt = random.nextInt(90) + 10;
				sb.append(nextInt);
			} else {
				sb.append(HYPHEN);
			}
		}
		return sb.toString();
	}

	private File getFile(String fileName) {
		ClassLoader classLoader = getClass().getClassLoader();

		try {
			return new File(classLoader.getResource(fileName).getFile());

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
