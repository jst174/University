package ua.com.foxminded.university.model;

import java.time.LocalDate;

public class Vacation {

	private LocalDate start;
	private LocalDate end;

	public Vacation(LocalDate start, LocalDate end) {
		this.start = start;
		this.end = end;
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

}
