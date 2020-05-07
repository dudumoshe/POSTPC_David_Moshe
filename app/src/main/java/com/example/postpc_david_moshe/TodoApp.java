package com.example.postpc_david_moshe;

import android.app.Application;
import android.content.SharedPreferences;

public class TodoApp extends Application {
    public TodoManager todoManager;

    @Override
    public void onCreate() {
        super.onCreate();
        todoManager = new TodoManager(this);
    }
}
