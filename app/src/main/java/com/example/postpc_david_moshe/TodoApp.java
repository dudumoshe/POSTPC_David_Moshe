package com.example.postpc_david_moshe;

import android.app.Application;
import java.util.HashMap;

public class TodoApp extends Application implements OnTodosChanges {
    public TodoManager todoManager;
    private HashMap<String, OnTodosChanges> notifyTodosChangedMap;

    @Override
    public void onCreate() {
        super.onCreate();
        todoManager = new TodoManager(this);
        notifyTodosChangedMap = new HashMap<>();
    }

    public void listenTodosChange(String name, OnTodosChanges context) {
        notifyTodosChangedMap.put(name, context);
    }

    public void stopListenTodosChange(String name) {
        notifyTodosChangedMap.remove(name);
    }

    @Override
    public void todosHaveBeenChanged() {
        for(OnTodosChanges context: notifyTodosChangedMap.values()) {
            context.todosHaveBeenChanged();
        }
    }
}
