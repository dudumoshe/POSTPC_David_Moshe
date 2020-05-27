package com.example.postpc_david_moshe;

import java.sql.Timestamp;

public class Todo {
    public String description;
    public int id;
    public boolean isDone;
    public Long createTimeStamp;
    public Long editTimeStamp;

    // For FireStore's reflection.
    public Todo(){}

    public Todo(String description, int ID) {
        this.description = description;
        this.id = ID;
        this.isDone = false;
        long now = System.currentTimeMillis();
        this.createTimeStamp = now;
        this.editTimeStamp = now;
    }

    public int getId() {
        return id;
    }

    public boolean checkIfDone() {
        return this.isDone;
    }

    public void setTodoStatus(boolean isDone) {
        this.isDone = isDone;
        this.editTimeStamp = System.currentTimeMillis();
    }

    public void setTodoDescription(String description) {
        this.description = description;
        this.editTimeStamp = System.currentTimeMillis();
    }

    public String todoDescription() {
        return description;
    }

    public String showableEditTime() {
        return new Timestamp(editTimeStamp).toString();
    }

    public String showableCreateTime() {
        return new Timestamp(createTimeStamp).toString();
    }
}
