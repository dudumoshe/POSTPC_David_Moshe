package com.example.postpc_david_moshe;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class TodoManager {
    ArrayList<Todo> todos;
    OnTodosChanges onTodosChanges;
    private int currentID = 0;
    FirebaseFirestore db;

    public TodoManager(OnTodosChanges onTodosChanges) {
        this.todos = new ArrayList<>();
        this.onTodosChanges = onTodosChanges;
        this.db = FirebaseFirestore.getInstance();

        updateTodoListFromFireStore();
        listenTodosChangesInFireStore();
    }

    private void updateTodoListFromFireStore() {
        db.collection("todos_list").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && task.getResult() != null) {
                    todos.clear();
                    int maxID = 0;
                    for(QueryDocumentSnapshot document: task.getResult()) {
                        Todo todo = document.toObject(Todo.class);
                        if (todo.getId() > maxID) {
                            maxID = todo.getId();
                        }
                        todos.add(todo);
                    }
                    currentID = maxID + 1;
                    onTodosChanges.todosHaveBeenChanged();
                    Log.d("TodoManager", "Loaded todos from firebase, current ID is " + currentID);
                } else {
                    Log.d("TodoManager", "Couldn't find todos on firebase");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TodoManager", "Couldn't find todos on firebase");
            }
        });
    }

    private void listenTodosChangesInFireStore() {
        db.collection("todos_list").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null || queryDocumentSnapshots == null) {
                    Log.d("TodoManager", "Couldn't find todos on firebase on listener");
                    return;
                }
                todos.clear();
                int maxID = 0;
                for(QueryDocumentSnapshot document: queryDocumentSnapshots) {
                    Todo todo = document.toObject(Todo.class);
                    if (todo.getId() > maxID) {
                        maxID = todo.getId();
                    }
                    todos.add(todo);
                }
                currentID = maxID + 1;
                onTodosChanges.todosHaveBeenChanged();
            }
        });
    }

    private void updateTodoOnFireStore(Todo todo) {
        db.collection("todos_list").document("Todo_num" + todo.getId()).set(todo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TodoManager", "added todo to FireStore successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TodoManager", "couldn't add todo to FireStore");
            }
        });
    }

    private void deleteTodoFromFireStore(int ID) {
        db.collection("todos_list").document("Todo_num" + ID).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TodoManager", "deleted todo from FireStore successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TodoManager", "couldn't delete todo from FireStore");
            }
        });
    }

    public ArrayList<Todo> getTodos() {
        return todos;
    }

    public Todo getTodoByID(int ID) {
        int index = getItemIndex(ID);
        if (index == -1) {
            Log.e("TodoManager", "couldn't find todo by ID");
            return null;
        }
        return todos.get(index);
    }

    public void addTodo(String description) {
        Todo todo = new Todo(description, currentID);
        currentID++;
        todos.add(todo);
        updateTodoOnFireStore(todo);
    }

    public Todo removeTodo(int ID) {
        int index = getItemIndex(ID);
        if (index == -1) {
            Log.e("TodoManager", "couldn't find todo by ID");
            return null;
        }
        Todo removedTodo = todos.remove(index);
        deleteTodoFromFireStore(ID);
        return removedTodo;
    }

    public boolean isDone(int ID) {
        int index = getItemIndex(ID);
        if (index == -1) {
            Log.e("TodoManager", "couldn't find todo by ID");
            return false;
        }
        return todos.get(index).checkIfDone();
    }

    public String getTodoDescription(int ID) {
        int index = getItemIndex(ID);
        if (index == -1) {
            Log.e("TodoManager", "couldn't find todo by ID");
            return "";
        }
        return todos.get(index).todoDescription();
    }

    public void setDone(int ID, boolean isDone) {
        int index = getItemIndex(ID);
        if (index == -1) {
            Log.e("TodoManager", "couldn't find todo by ID");
            return;
        }
        Todo todo = todos.get(index);
        todo.setTodoStatus(isDone);
        todos.set(index, todo);
        updateTodoOnFireStore(todo);
    }

    public void setDescription(int ID, String description) {
        int index = getItemIndex(ID);
        if (index == -1) {
            Log.e("TodoManager", "couldn't find todo by ID");
            return;
        }
        Todo todo = todos.get(index);
        todo.setTodoDescription(description);
        todos.set(index, todo);
        updateTodoOnFireStore(todo);
    }

    private int getItemIndex(int ID) {
        for (int index = 0; index < todos.size(); index++) {
            if (todos.get(index).getId() == ID) {
                return index;
            }
        }
        return -1;
    }
}

interface OnTodosChanges {
    public void todosHaveBeenChanged();
}
