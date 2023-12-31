package com.example.easychat.model;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class AssignmentModel {

    private String id;
    private String title;
    private String description;
    private String dueDate;
    private String status;
    private String userClass;
    private String teacherName;
    private String userSemester;
    private String pdfDownloadUrl;

    @ServerTimestamp
    private Date timestamp;

    // Required public no-argument constructor
    public AssignmentModel() {
        // Default constructor required for calls to DataSnapshot.getValue(AssignmentModel.class)
    }



    public AssignmentModel(String id, String teacherName, String title, String description, String dueDate, String status, String userClass, String userSemester, String pdfDownloadUrl) {
       this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.teacherName=teacherName;
        this.userClass = userClass;
        this.userSemester = userSemester;
        this.pdfDownloadUrl = pdfDownloadUrl;
    }

    // Getter and setter methods for all fields
    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserClass() {
        return userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public String getUserSemester() {
        return userSemester;
    }

    public void setUserSemester(String userSemester) {
        this.userSemester = userSemester;
    }

    public String getPdfDownloadUrl() {
        return pdfDownloadUrl;
    }

    public void setPdfDownloadUrl(String pdfDownloadUrl) {
        this.pdfDownloadUrl = pdfDownloadUrl;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
