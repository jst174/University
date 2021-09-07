package ua.com.foxminded.university.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Lesson {

	private int id;
	private List<Group> groups;
	private Course course;
	private Classroom classroom;
	private Teacher teacher;
	private LocalDate date;
	private Time time;

	public Lesson(Course course, List<Group> groups, Classroom classroom, Teacher teacher, LocalDate localDate,
			Time time) {
		this.course = course;
		this.classroom = classroom;
		this.teacher = teacher;
		this.date = localDate;
		this.time = time;
		this.groups = groups;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Classroom getClassroom() {
		return classroom;
	}

	public void setClassroom(Classroom classroom) {
		this.classroom = classroom;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Lesson [groups=" + groups + ", course=" + course + ", classroom=" + classroom + ", teacher=" + teacher
				+ ", localDate=" + date + ", time=" + time + "]";
	}

}
