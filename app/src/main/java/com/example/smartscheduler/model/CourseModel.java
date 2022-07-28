package com.example.smartscheduler.model;

import androidx.annotation.Nullable;

import java.util.Objects;

public class CourseModel {
    String ProfName;
    String Course;
    String CreditHour;

    public CourseModel(String profName, String course, String creditHour) {
        ProfName = profName;
        Course = course;
        CreditHour = creditHour;
    }

    public String getProfName() {
        return ProfName;
    }

    public void setProfName(String profName) {
        ProfName = profName;
    }

    public String getCourse() {
        return Course;
    }

    public void setCourse(String course) {
        Course = course;
    }

    public String getCreditHour() {
        return CreditHour;
    }

    public void setCreditHour(String creditHour) {
        CreditHour = creditHour;
    }

    @Override
    public boolean equals (Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            CourseModel courseModel = (CourseModel) object;
            if (this.getCourse().equals(courseModel.getCourse()) && this.getProfName().equals(courseModel.getProfName()) && Objects.equals(this.getCreditHour(), courseModel.getCreditHour())) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
