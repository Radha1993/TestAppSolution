package com.example.testappsolution.model;

public class TaskToDo {
    public TaskToDo() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String title;
    String details;
    String date;
    public TaskToDo(String title, String details, String date) {
        this.title=title;
        this.details=details;
        this.date=date;
    }
}
