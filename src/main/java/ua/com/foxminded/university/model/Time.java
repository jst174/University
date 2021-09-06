package ua.com.foxminded.university.model;

import java.time.LocalTime;

public class Time {

	private int id;
	private LocalTime startTime;
	private LocalTime endTime;

	public Time(LocalTime startTime, LocalTime endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

}
