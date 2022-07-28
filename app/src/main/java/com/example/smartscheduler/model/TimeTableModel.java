package com.example.smartscheduler.model;

public class TimeTableModel {
    String timeslot;
    String course;
    String faculty;

    public TimeTableModel(String timeslot,String course,String faculty) {
        this.timeslot = timeslot;
        this.course = course;
        this.faculty = faculty;
    }

    public String getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(String timeslot) {
        this.timeslot = timeslot;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }
}
