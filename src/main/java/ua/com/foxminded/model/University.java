package ua.com.foxminded.model;

import java.util.ArrayList;
import java.util.List;

public class University {

	private List<Classroom> classrooms;
	private List<Group> groups;
	private List<Teacher> teachers;
	private List<Lesson> lessons;
	private List<Course> courses;
	private List<Student> students;
	private List<Holiday> holidays;
	private List<Time> times;

	public University() {
		classrooms = new ArrayList<>();
		groups = new ArrayList<>();
		teachers = new ArrayList<>();
		lessons = new ArrayList<>();
		courses = new ArrayList<>();
		students = new ArrayList<>();
		holidays = new ArrayList<>();
		times = new ArrayList<>();
	}

	public List<Classroom> getClassrooms() {
		return classrooms;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public List<Lesson> getLessons() {
		return lessons;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public List<Student> getStudents() {
		return students;
	}

	public List<Holiday> getHolidays() {
		return holidays;
	}

	public List<Time> getTimes() {
		return times;
	}

}
