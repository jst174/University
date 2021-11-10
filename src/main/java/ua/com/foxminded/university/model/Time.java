package ua.com.foxminded.university.model;

import java.time.LocalTime;
import java.util.Objects;

public class Time {

    private int id;
    private LocalTime startTime;
    private LocalTime endTime;

    public Time() {

    }

    public Time(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Time time = (Time) o;
        return Objects.equals(startTime, time.startTime) && Objects.equals(endTime, time.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }

    @Override
    public String toString() {
        return "Time{" +
            "startTime=" + startTime +
            ", endTime=" + endTime +
            '}';
    }

    public static class Builder {

        private Time time;

        public Builder() {
            time = new Time();
        }

        public Builder setId(int id) {
            time.setId(id);
            return this;
        }

        public Builder setStartTime(LocalTime startTime) {
            time.setStartTime(startTime);
            return this;
        }

        public Builder setEndTime(LocalTime endTime) {
            time.setEndTime(endTime);
            return this;
        }

        public Time build() {
            return time;
        }
    }
}
