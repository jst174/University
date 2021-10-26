package ua.com.foxminded.university.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Classroom)) return false;
        Classroom classroom = (Classroom) o;
        return number == classroom.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
