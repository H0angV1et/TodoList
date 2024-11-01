package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final Context context;
    private DatabaseHelper db;
    private List<TaskModel> taskList;

    public TaskAdapter(Context context, List<TaskModel> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    public Context getContext() {
        return context;
    }

    public void setTasks(List<TaskModel> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.taskCheckBox.setText(taskList.get(position).getTitle());
        holder.taskCheckBox.setChecked(toBoolean(taskList.get(position).getStatus()));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    public void deleteItem(int position) {
        TaskModel item = taskList.get(position);
        db.deleteTask(item.getId());
        taskList.remove(position);
        notifyItemRemoved(position);
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox taskCheckBox;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskCheckBox = itemView.findViewById(R.id.check_box_task);
        }
    }
}
