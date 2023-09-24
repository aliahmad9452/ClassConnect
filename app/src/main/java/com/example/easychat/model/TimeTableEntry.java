package com.example.easychat.model;

public class TimeTableEntry {
    private String lectureDetails;
    private String time;
    private String teacherName;


    public TimeTableEntry(){

    }
    public TimeTableEntry(String lectureDetails, String time, String teacherName) {
        this.lectureDetails = lectureDetails;
        this.time = time;
        this.teacherName = teacherName;
    }

    public String getLectureDetails() {
        return lectureDetails;
    }

    public void setLectureDetails(String lectureDetails) {
        this.lectureDetails = lectureDetails;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
