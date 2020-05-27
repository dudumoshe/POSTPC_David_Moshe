package com.example.postpc_david_moshe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EditInactiveTodo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_inactive_todo);

        final TodoManager todoManager = ((TodoApp)getApplicationContext()).todoManager;

        Intent intent = getIntent();
        final int todoID = intent.getIntExtra("TodoID", -1);
        if(todoID == -1) {
            Log.e("EditActiveTodo", "don't got any item to present");
            finish();
        }

        String descriptionText = todoManager.getTodoDescription(todoID);
        TextView description = findViewById(R.id.done_todo_item_text);
        description.setText(descriptionText);

        Button markAsUnDone = findViewById(R.id.mark_todo_as_undone);
        Button deleteTodo = findViewById(R.id.delete_done_todo);

        markAsUnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoManager.setDone(todoID, false);
                Toast.makeText(getApplicationContext(), "TODO description marked as undone!",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        deleteTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditInactiveTodo.this);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TodoManager todoManager = ((TodoApp)getApplicationContext()).todoManager;
                        todoManager.removeTodo(todoID);
                        finish();
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
        });
    }
}
