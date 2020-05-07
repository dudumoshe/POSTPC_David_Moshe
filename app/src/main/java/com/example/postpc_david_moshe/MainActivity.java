package com.example.postpc_david_moshe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TodosAdapterClickCallback {
    TodosAdapter adapter = new TodosAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TodoApp todoApp = (TodoApp)getApplicationContext();

        if(savedInstanceState != null) {
            EditText insertItemsEditText = findViewById(R.id.insert_items_edit_text);
            insertItemsEditText.setText(savedInstanceState.getString("todoEditText", ""));
        }

        RecyclerView todosRecycler = findViewById(R.id.items_list_recycler_view);
        todosRecycler.setAdapter(adapter);
        todosRecycler.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        );
        adapter.setTodos(todoApp.todoManager.getTodos());
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
                TodoManager todoManager = ((TodoApp)getApplicationContext()).todoManager;
                todoManager.addTodo(new Todo(todoDescription));
                adapter.setTodos(todoManager.getTodos());
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        EditText insertItemsEditText = findViewById(R.id.insert_items_edit_text);
        super.onSaveInstanceState(outState);
        outState.putString("todoEditText", insertItemsEditText.getText().toString());
    }

    @Override
    public void onClickTodo(int position) {
        TodoManager todoManager = ((TodoApp)getApplicationContext()).todoManager;
        if (todoManager.isDone(position)) {
            return;
        }
        todoManager.setAsDone(position);
        adapter.setTodos(todoManager.getTodos());
        Toast.makeText(getApplicationContext(), "TODO " + todoManager.getTodoDescription(position) +
                getString(R.string.todo_done),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClickTodo(int position) {
        final int pos = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                TodoManager todoManager = ((TodoApp)getApplicationContext()).todoManager;
                todoManager.removeTodo(pos);
                adapter.setTodos(todoManager.getTodos());
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        builder.setTitle(R.string.approve_delete_ask);
        builder.show();
    }
}
