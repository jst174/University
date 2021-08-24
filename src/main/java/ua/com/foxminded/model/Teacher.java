package ua.com.foxminded.model;

import java.time.LocalDate;
import java.util.List;

public class Teacher extends Person {

	private String academicDegree;
	private List<Course> courese;
	private List<Vacation> vacations;

	public Teacher(String firstName, String lastName, LocalDate birthDate, String gender, Adress adress,
			String phoneNumber, String email, String academicDegree) {
		super(firstName, lastName, birthDate, gender, adress, phoneNumber, email);
		this.academicDegree = academicDegree;
	}

	public List<Course> getCourese() {
		return courese;
	}

	public List<Vacation> getVacations() {
		return vacations;
	}

}
