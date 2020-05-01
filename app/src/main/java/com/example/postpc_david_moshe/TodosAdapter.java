package com.example.postpc_david_moshe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TodosAdapter extends RecyclerView.Adapter<TodoViewHolder> {
    private ArrayList<Todo> todos = new ArrayList<>();
    private TodosAdapterClickCallback todosAdapterClickCallback = null;

    public void setTodosAdapterClickCallback(TodosAdapterClickCallback todosAdapterClickCallback) {
        this.todosAdapterClickCallback = todosAdapterClickCallback;
    }

    public void setTodos(List<Todo> todos) {
        this.todos.clear();
        this.todos.addAll(todos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.todo_layout, parent, false);
        final TodoViewHolder todoHolder = new TodoViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (todosAdapterClickCallback != null) {
                    todosAdapterClickCallback.onClickTodo(todoHolder.getAdapterPosition());
                }
            }
        });

        return todoHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo todo = this.todos.get(position);
        holder.todo.setText(todo.description);
        holder.todo.setAlpha(todo.isDone ? 0.3f : 1.0f);
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }
}

class TodoViewHolder extends RecyclerView.ViewHolder {
    TextView todo;

    public TodoViewHolder(@NonNull View itemView) {
        super(itemView);
        this.todo = itemView.findViewById(R.id.task_description);
    }
}

interface TodosAdapterClickCallback {
    void onClickTodo(int position);
}
