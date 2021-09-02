package ua.com.foxminded.model;

public class Course {

	private static int counter;
	private int id;
	private String name;

	public Course(String name) {
		counter++;
		this.name = name;
		this.id = counter;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Course [name=" + name + "]";
	}

}
