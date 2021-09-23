package ua.com.foxminded.university.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Teacher extends Person {

	private int id;
	private AcademicDegree academicDegree;
	private List<Course> courses;
	private List<Vacation> vacations;

	public Teacher(String firstName, String lastName, LocalDate birthDate, Gender gender, Address address,
			String phoneNumber, String email, AcademicDegree academicDegree) {
		super(firstName, lastName, birthDate, gender, address, phoneNumber, email);
		this.academicDegree = academicDegree;
		courses = new ArrayList<>();
		vacations = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public AcademicDegree getAcademicDegree() {
		return academicDegree;
	}

	public void setAcademicDegree(AcademicDegree academicDegree) {
		this.academicDegree = academicDegree;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public List<Vacation> getVacations() {
		return vacations;
	}

	public void setVacations(Vacation vacation) {
		this.vacations.add(vacation);
	}

	@Override
	public String toString() {
		return getFirstName() + " " + getLastName() + "; " + getBirthDate() + "; " + getGender() + "; " + getAdress()
				+ "; " + getPhoneNumber() + "; " + getEmail() + "; " + academicDegree;
	}

}