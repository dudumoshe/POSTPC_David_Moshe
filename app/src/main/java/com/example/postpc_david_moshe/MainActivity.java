package com.example.postpc_david_moshe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TodosAdapterClickCallback {
    ArrayList<Todo> todos = new ArrayList<>();
    TodosAdapter adapter = new TodosAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            todos.clear();
            int savedTodosSize = savedInstanceState.getInt("todos_size",0);
            for(int i = 0; i < savedTodosSize; i++) {
                String description = savedInstanceState.getString("todo_description_" + i);
                boolean isDone = savedInstanceState.getBoolean("todo_is_done_" + i , false);
                Todo todo = new Todo(description);
                todo.isDone = isDone;
                todos.add(todo);
            }
        }

        RecyclerView todosRecycler = findViewById(R.id.items_list_recycler_view);
        todosRecycler.setAdapter(adapter);
        todosRecycler.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        );
        adapter.setTodos(todos);
        adapter.setTodosAdapterClickCallback(this);

        Button addItembutton = findViewById(R.id.add_item_button);
        addItembutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText insertItemsEditText = findViewById(R.id.insert_items_edit_text);
                String todoDescription = insertItemsEditText.getText().toString();
                insertItemsEditText.setText("");
                if (todoDescription.isEmpty()) {
                    Toast.makeText(getApplicationContext(),getString(R.string.empty_todo), Toast.LENGTH_SHORT).show();
                    return;
                }
                todos.add(new Todo(todoDescription));
                adapter.setTodos(todos);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("todos_size",todos.size());
        for(int i = 0; i < todos.size(); i++) {
            Todo todo = todos.get(i);
            outState.putString("todo_description_" + i, todo.description);
            outState.putBoolean("todo_is_done_" + i , todo.isDone);
        }
    }

    @Override
    public void onClickTodo(int position) {
        Todo todo = todos.get(position);
        if (todo.isDone) {
            return;
        }
        todo.setAsDone();
        todos.set(position, todo);
        adapter.setTodos(todos);
        Toast.makeText(getApplicationContext(), "TODO " + todo.description +
                getString(R.string.todo_done),Toast.LENGTH_SHORT).show();
    }
}
