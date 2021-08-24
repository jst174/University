package ua.com.foxminded.model;

import java.util.List;

public class Group {

	private String name;
	private List<Student> students;

	public Group(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<Student> getStudents() {
		return students;
	}

}
