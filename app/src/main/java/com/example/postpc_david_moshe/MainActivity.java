package com.example.postpc_david_moshe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TodosAdapterClickCallback, OnTodosChanges {
    TodosAdapter adapter = new TodosAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            EditText insertItemsEditText = findViewById(R.id.insert_items_edit_text);
            insertItemsEditText.setText(savedInstanceState.getString("todoEditText", ""));
        }

        RecyclerView todosRecycler = findViewById(R.id.items_list_recycler_view);
        todosRecycler.setAdapter(adapter);
        todosRecycler.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        );
        adapter.setTodosAdapterClickCallback(this);
        updateTodosContent();

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
                TodoManager todoManager = ((TodoApp)getApplicationContext()).todoManager;
                todoManager.addTodo(todoDescription);
                adapter.setTodos(todoManager.getTodos());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTodosContent();
        TodoApp todoApp = (TodoApp)getApplicationContext();
        todoApp.listenTodosChange("MainActivity", this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TodoApp todoApp = (TodoApp)getApplicationContext();
        todoApp.stopListenTodosChange("MainActivity");
    }

    public void updateTodosContent() {
        TodoApp todoApp = (TodoApp)getApplicationContext();
        adapter.setTodos(todoApp.todoManager.getTodos());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        EditText insertItemsEditText = findViewById(R.id.insert_items_edit_text);
        super.onSaveInstanceState(outState);
        outState.putString("todoEditText", insertItemsEditText.getText().toString());
    }

    @Override
    public void onClickTodo(int ID) {
        TodoManager todoManager = ((TodoApp)getApplicationContext()).todoManager;
        Log.d("Main Activity", "Editing todo with ID " + ID);
        if (todoManager.isDone(ID)) {
            Intent intent = new Intent(this, EditInactiveTodo.class);
            intent.putExtra("TodoID", ID);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, EditActiveTodo.class);
            intent.putExtra("TodoID", ID);
            startActivity(intent);
        }
    }

    @Override
    public void todosHaveBeenChanged() {
        updateTodosContent();
        Toast.makeText(getApplicationContext(),"Todos Updated! check it out",
                Toast.LENGTH_SHORT).show();
    }
}
