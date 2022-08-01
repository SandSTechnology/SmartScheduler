package com.example.smartscheduler.model;

public class TimeTableWithFacultyModel {
    String timeslot;
    String course;
    String faculty;
    String Day;
    String Room;
    String Block_Num;
    String Floor_num;

    public TimeTableWithFacultyModel(String timeslot, String course, String faculty, String day, String room, String block_Num, String floor_num) {
        this.timeslot = timeslot;
        this.course = course;
        this.faculty = faculty;
        Day = day;
        Room = room;
        Block_Num = block_Num;
        Floor_num = floor_num;
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

    public String getBlock_Num() {
        return Block_Num;
    }

    public void setBlock_Num(String block_Num) {
        Block_Num = block_Num;
    }

    public String getFloor_num() {
        return Floor_num;
    }

    public void setFloor_num(String floor_num) {
        Floor_num = floor_num;
    }
}
