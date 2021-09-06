package ua.com.foxminded.university.model;

import java.time.LocalDate;

public class Student extends Person {

	private int id;
	private Group group;

	public Student(String firstName, String lastName, LocalDate birthDate, Gender gender, Adress adress,
			String phoneNumber, String email) {
		super(firstName, lastName, birthDate, gender, adress, phoneNumber, email);
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Group getGroup() {
		return group;
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
				+ "; " + getPhoneNumber() + "; " + getEmail() + "; " + group;
	}

}
