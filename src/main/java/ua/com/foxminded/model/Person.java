package ua.com.foxminded.model;

import java.time.LocalDate;

public abstract class Person {

	private String firstName;
	private String lastName;
	private LocalDate birthDate;
	private Gender gender;
	private Adress adress;
	private String phoneNumber;
	private String email;

	public Person(String firstName, String lastName, LocalDate birthDate, Gender gender, Adress adress,
			String phoneNumber, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.gender = gender;
		this.adress = adress;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public Gender getGender() {
		return gender;
	}

	public Adress getAdress() {
		return adress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setAdress(Adress adress) {
		this.adress = adress;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setEmail(String email) {
		this.email = email;
	}


}
