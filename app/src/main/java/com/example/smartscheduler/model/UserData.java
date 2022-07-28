package com.example.smartscheduler.model;

public class UserData {
    public String username;
    public String email;
    public String usertype;
    public String semester;
    public String department;
    String facultyID;
    public String uid;

    public UserData() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserData(String username, String email, String usertype,String facultyID, String uid) {
        this.username = username;
        this.email = email;
        this.usertype = usertype;
        this.facultyID = facultyID;
        this.uid = uid;
    }

    public UserData(String username, String email, String usertype, String semester, String department, String uid) {
        this.username = username;
        this.email = email;
        this.usertype = usertype;
        this.semester = semester;
        this.department = department;
        this.uid = uid;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFacultyID() {
        return facultyID;
    }

    public void setFacultyID(String facultyID) {
        this.facultyID = facultyID;
    }
}