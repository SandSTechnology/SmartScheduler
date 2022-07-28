package com.example.smartscheduler.model;

public class TimeTableWithFacultyModel {
    String timeslot;
    String course;
    String faculty;
    String Day;

    public TimeTableWithFacultyModel(String timeslot, String course,String faculty,String day) {
        this.timeslot = timeslot;
        this.course = course;
        this.faculty = faculty;
        this.Day = day;
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

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }
}
