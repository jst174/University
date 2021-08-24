package ua.com.foxminded.model;

import java.time.LocalDate;

public class Holiday {

	private String name;
	private LocalDate date;

	public Holiday(String name, LocalDate date) {
		this.name = name;
		this.date = date;
	}

}
