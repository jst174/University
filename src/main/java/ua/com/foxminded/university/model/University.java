package ua.com.foxminded.university.model;

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

	public void setClassrooms(List<Classroom> classrooms) {
		this.classrooms = classrooms;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}

	public List<Lesson> getLessons() {
		return lessons;
	}

	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public List<Holiday> getHolidays() {
		return holidays;
	}

	public void setHolidays(List<Holiday> holidays) {
		this.holidays = holidays;
	}

	public List<Time> getTimes() {
		return times;
	}

	public void setTimes(List<Time> times) {
		this.times = times;
	}

}
