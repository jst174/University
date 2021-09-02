package ua.com.foxminded.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Teacher extends Person {

	private static int counter;

	private int id;
	private String academicDegree;
	private List<Course> courses;
	private List<Vacation> vacations;

	public Teacher(String firstName, String lastName, LocalDate birthDate, String gender, Adress adress,
			String phoneNumber, String email, String academicDegree) {
		super(firstName, lastName, birthDate, gender, adress, phoneNumber, email);
		this.academicDegree = academicDegree;
		courses = new ArrayList<>();
		vacations = new ArrayList<>();
		counter++;
		this.id = counter;
	}

	public void setAcademicDegree(String academicDegree) {
		this.academicDegree = academicDegree;
	}

	public void setCourses(Course course) {
		this.courses.add(course);
	}

	public void setVacations(Vacation vacation) {
		this.vacations.add(vacation);
	}

	public List<Course> getCourses() {
		return courses;
	}

	public List<Vacation> getVacations() {
		return vacations;
	}

	public String getAcademicDegree() {
		return academicDegree;
	}

	public int getId() {
		return id;
	}

}
