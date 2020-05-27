package com.example.postpc_david_moshe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditActiveTodo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_active_todo);
        final TodoManager todoManager = ((TodoApp)getApplicationContext()).todoManager;

        Intent intent = getIntent();
        final int todoID = intent.getIntExtra("TodoID", -1);
        if(todoID == -1) {
            Log.e("EditActiveTodo", "don't got any item to present");
            finish();
        }

        Todo todoItemToShow = todoManager.getTodoByID(todoID);
        EditText description = findViewById(R.id.edit_todo_item);
        description.setText(todoItemToShow.todoDescription());
        TextView createTime = findViewById(R.id.todo_create_time);
        createTime.setText("Created: " + todoItemToShow.showableCreateTime());
        TextView editTime = findViewById(R.id.todo_edit_time);
        editTime.setText("Last Edited: " + todoItemToShow.showableEditTime());

        Button markAsDone = findViewById(R.id.mark_todo_as_done);
        Button applyChanges = findViewById(R.id.apply_todo_text);

        markAsDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoManager.setDone(todoID, true);
                Toast.makeText(getApplicationContext(), "TODO description marked as done!",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText description = findViewById(R.id.edit_todo_item);
                todoManager.setDescription(todoID, description.getText().toString());
                Toast.makeText(getApplicationContext(), "TODO description was updated",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
