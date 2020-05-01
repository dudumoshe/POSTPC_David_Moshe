package com.example.postpc_david_moshe;

public class Todo {
    String description;
    boolean isDone;

    public Todo(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void setAsDone() {
        isDone = true;
    }
}
