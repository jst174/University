package ua.com.foxminded.model;

import java.time.LocalTime;

public class Time {

	private LocalTime startTime;
	private LocalTime endTime;

	public Time(LocalTime startTime, LocalTime endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

}
