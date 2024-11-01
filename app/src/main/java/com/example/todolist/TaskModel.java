package com.example.todolist;

public class TaskModel {
    private int id, status;
    private String title;

    public TaskModel(int id, String title, int status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }

    public TaskModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
