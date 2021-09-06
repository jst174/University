package ua.com.foxminded;

import static java.lang.System.lineSeparator;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.University;

public class ScheduleFormater {

	private static final char SLAH = '|';
	private static final String Date = "Date";
	private static final String Start = "Start";
	private static final String End = "End";
	private static final String Course = "Course";
	private static final String Group = "Group";
	private static final String Classroom = "Classroom";
	private static final String Teacher = "Teacher";

	public String format(List<Lesson> lessons) {
		StringBuilder result = new StringBuilder();
		int maxGroupLength = groupFormat(getLessonWithMaxGroup(lessons).getGroups()).length();
		int maxCourseLength = getMaxCourseLength(lessons);
		String formatPattern = "%-10s" + SLAH + "%s" + "-" + "%-5s" + SLAH + "%-" + maxCourseLength + "s" + SLAH + "%-"
				+ maxGroupLength + "s" + SLAH + "%-9s" + SLAH + "%s";
		result.append(makeHeader(formatPattern) + lineSeparator());
		lessons.stream()
				.map(lesson -> String.format(formatPattern, lesson.getDate(), lesson.getTime().getStartTime(),
						lesson.getTime().getEndTime(), lesson.getCourse().getName(), groupFormat(lesson.getGroups()),
						lesson.getClassroom().getNumber(), lesson.getTeacher().getLastName()))
				.forEach(lesson -> result.append(lesson + lineSeparator()));
		return result.toString();

	}

	private String groupFormat(List<Group> group) {
		StringBuilder groups = new StringBuilder();
		for (int i = 0; i < group.size(); i++) {
			groups.append(group.get(i));
			if (i != group.size() - 1) {
				groups.append(", ");
			}
		}
		return groups.toString();

	}

	private String makeHeader(String formatPattern) {
		return String.format(formatPattern, Date, Start, End, Course, Group, Classroom, Teacher);
	}

	private Lesson getLessonWithMaxGroup(List<Lesson> lessons) {
		int max = 0;
		Lesson lessonWithMaxGroups = null;
		for (Lesson lesson : lessons) {
			if (lesson.getGroups().size() > max) {
				max = lesson.getGroups().size();
				lessonWithMaxGroups = lesson;
			}
		}

		return lessonWithMaxGroups;
	}

	private int getMaxCourseLength(List<Lesson> lessons) {
		Lesson lessonWithMaxCourseName = lessons.stream()
				.max((c1, c2) -> c1.getCourse().getName().compareTo(c2.getCourse().getName())).get();
		if (Course.length() > lessonWithMaxCourseName.getCourse().getName().length()) {
			return Course.length();
		} else {
			return lessonWithMaxCourseName.getCourse().getName().length();
		}

	}

}
