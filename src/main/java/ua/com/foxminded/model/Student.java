package ua.com.foxminded.model;

import java.time.LocalDate;

public class Student extends Person {

	private Group group;

	public Student(String firstName, String lastName, LocalDate birthDate, String gender, Adress adress,
			String phoneNumber, String email) {
		super(firstName, lastName, birthDate, gender, adress, phoneNumber, email);
	}
	
	

	public void setGroup(Group group) {
		this.group = group;
	}

	public Group getGroup() {
		return group;
	}

}
