package ua.com.foxminded.model;

import java.time.LocalDate;

public class Vacation {

	private LocalDate start;
	private LocalDate end;

	public Vacation(LocalDate start, LocalDate end) {
		this.start = start;
		this.end = end;
	}

}
