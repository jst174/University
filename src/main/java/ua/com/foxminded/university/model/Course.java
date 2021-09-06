package ua.com.foxminded.university.model;

public class Course {

	private int id;
	private String name;

	public Course(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

}
