package ua.com.foxminded.university.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import ua.com.foxminded.university.model.AcademicDegree;

import java.util.Map;

@ConfigurationProperties(prefix = "university")
public class UniversityConfigProperties {

    private int maxGroupSize;
    private int minLessonDurationInMinutes;
    private Map<AcademicDegree, Integer> maxPeriodsVacation;

    public int getMaxGroupSize() {
        return maxGroupSize;
    }

    public void setMaxGroupSize(int maxGroupSize) {
        this.maxGroupSize = maxGroupSize;
    }

    public int getMinLessonDurationInMinutes() {
        return minLessonDurationInMinutes;
    }

    public void setMinLessonDurationInMinutes(int minLessonDurationInMinutes) {
        this.minLessonDurationInMinutes = minLessonDurationInMinutes;
    }

    public Map<AcademicDegree, Integer> getMaxPeriodsVacation() {
        return maxPeriodsVacation;
    }

    public void setMaxPeriodsVacation(Map<AcademicDegree, Integer> maxPeriodsVacation) {
        this.maxPeriodsVacation = maxPeriodsVacation;
    }
}
