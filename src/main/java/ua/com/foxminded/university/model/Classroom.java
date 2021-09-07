package ua.com.foxminded.university.model;

public class Classroom {

	private int id;
	private int number;
	private int capacity;

	public Classroom(int number, int capacity) {
		this.number = number;
		this.capacity = capacity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	@Override
	public String toString() {
		return "number: " + number + "; capacity: " + capacity;
	}

}
