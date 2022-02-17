package ua.com.foxminded.university.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ua.com.foxminded.university.model.AcademicDegree;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "university")
public class UniversityProperties {

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
