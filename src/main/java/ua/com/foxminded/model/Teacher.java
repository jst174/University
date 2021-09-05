package ua.com.foxminded.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Teacher extends Person {

	private int id;
	private AcademicDegree academicDegree;
	private List<Course> courses;
	private List<Vacation> vacations;

	public Teacher(String firstName, String lastName, LocalDate birthDate, Gender gender, Adress adress,
			String phoneNumber, String email, AcademicDegree academicDegree) {
		super(firstName, lastName, birthDate, gender, adress, phoneNumber, email);
		this.academicDegree = academicDegree;
		courses = new ArrayList<>();
		vacations = new ArrayList<>();
	}

	public void setAcademicDegree(AcademicDegree academicDegree) {
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

	public AcademicDegree getAcademicDegree() {
		return academicDegree;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return getFirstName() + " " + getLastName() + "; " + getBirthDate() + "; " + getGender() + "; " + getAdress()
				+ "; " + getPhoneNumber() + "; " + getEmail() + "; " + academicDegree;
	}

}
