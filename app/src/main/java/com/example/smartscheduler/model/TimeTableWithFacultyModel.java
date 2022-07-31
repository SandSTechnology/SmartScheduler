package com.example.smartscheduler.model;

public class TimeTableWithFacultyModel {
    String timeslot;
    String course;
    String faculty;
    String Day;
    String Room;

    public TimeTableWithFacultyModel(String timeslot, String course, String faculty, String day, String room) {
        this.timeslot = timeslot;
        this.course = course;
        this.faculty = faculty;
        Day = day;
        Room = room;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
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
