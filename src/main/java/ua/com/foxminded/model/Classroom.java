package ua.com.foxminded.model;

public class Classroom {
	private static int counter;
	private int id;
	private int number;
	private int capacity;

	public Classroom(int number, int capacity) {
		counter++;
		this.id = counter;
		this.number = number;
		this.capacity = capacity;
	}

	public int getId() {
		return id;
	}

	public int getNumber() {
		return number;
	}

	public int getCapacity() {
		return capacity;
	}

}
