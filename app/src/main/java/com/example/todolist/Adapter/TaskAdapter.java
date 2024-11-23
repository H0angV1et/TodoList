package com.example.todolist.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Model.TaskModel;
import com.example.todolist.R;
import com.example.todolist.TaskDetailActivity;
import com.example.todolist.Utils.DatabaseHelper;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final Context context;
    private DatabaseHelper database;
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
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        database = new DatabaseHelper(getContext());
        database.openDatabase();

        final TaskModel item = taskList.get(position);
        holder.taskCheckBox.setChecked(toBoolean(taskList.get(position).getStatus()));
        holder.taskCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    database.updateStatusById(item.getId(), 1);
                    item.setStatus(1);

                } else {
                    database.updateStatusById(item.getId(), 0);
                    item.setStatus(0);
                }
            }
        });
        holder.taskTitle.setText(item.getTitle());
        if (!item.getDueDate().isEmpty()) {
            holder.taskDueDate.setVisibility(View.VISIBLE);
            holder.taskDueDate.setText(item.getDueDate());
        } else {
            holder.taskDueDate.setVisibility(View.GONE);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TaskDetailActivity.class);
                intent.putExtra(TaskDetailActivity.IS_NEW_TASK, false);
                intent.putExtra("id", taskList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("title", taskList.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("dueDate", taskList.get(holder.getAdapterPosition()).getDueDate());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox taskCheckBox;
        TextView taskTitle, taskDueDate;
        CardView cardView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskCheckBox = itemView.findViewById(R.id.check_box_task);
            taskTitle = itemView.findViewById(R.id.text_title);
            taskDueDate = itemView.findViewById(R.id.text_due_date);
            cardView = itemView.findViewById(R.id.item_task);
        }
    }
}
