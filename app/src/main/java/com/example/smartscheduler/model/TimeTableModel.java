package com.example.smartscheduler.model;

public class TimeTableModel {
    String timeslot;
    String course;
    String faculty;
    String Block;
    String Floor;

    public TimeTableModel(String timeslot, String course, String faculty, String block, String floor) {
        this.timeslot = timeslot;
        this.course = course;
        this.faculty = faculty;
        Block = block;
        Floor = floor;
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

    public String getBlock() {
        return Block;
    }

    public void setBlock(String block) {
        Block = block;
    }

    public String getFloor() {
        return Floor;
    }

    public void setFloor(String floor) {
        Floor = floor;
    }
}
