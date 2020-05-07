package com.example.postpc_david_moshe;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class TodoManager {
    ArrayList<Todo> todos;
    SharedPreferences sp;
    Gson gson;

    public TodoManager(Context context) {
        this.todos = new ArrayList<>();
        this.sp = context.getSharedPreferences("todosList", MODE_PRIVATE);
        this.gson = new Gson();

        int savedTodoListSize = sp.getInt("todo_list_size", 0);
        Log.d("TodoManager", "The current size is of todos " + savedTodoListSize + ".");
        for(int i = 0; i < savedTodoListSize; i++) {
            String todoJson = sp.getString("todo_item_" + i, null);
            if (todoJson != null) {
                todos.add(gson.fromJson(todoJson, Todo.class));
            }
        }
    }

    public ArrayList<Todo> getTodos() {
        return todos;
    }

    public void addTodo(Todo todo) {
        todos.add(todo);
        sp.edit()
          .putInt("todo_list_size", todos.size())
          .putString("todo_item_" + (todos.size() - 1), gson.toJson(todo))
          .apply();
    }

    public Todo removeTodo(int index) {
        Todo removedTodo = todos.remove(index);
        saveCurrentTodosListInMemory();
        return removedTodo;
    }

    public boolean isDone(int index) {
        return todos.get(index).isDone;
    }

    public String getTodoDescription(int index) {
        return todos.get(index).description;
    }

    public void setAsDone(int index) {
        todos.get(index).isDone = true;
        sp.edit()
          .putString("todo_item_" + index, gson.toJson(todos.get(index)))
          .apply();
    }

    private void saveCurrentTodosListInMemory() {
        int savedTodoListSize = sp.getInt("todo_list_size", 0);
        SharedPreferences.Editor editor = sp.edit();
        for (int i = 0; i < savedTodoListSize; i++) {
            editor.remove("todo_item_" + i);
        }
        editor.putInt("todo_list_size", todos.size());
        for (int i = 0; i < todos.size(); i++) {
            editor.putString("todo_item_" + i, gson.toJson(this.todos.get(i)));
        }
        editor.apply();
    }
}
