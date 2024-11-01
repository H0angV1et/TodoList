package com.example.todolist;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<TaskModel> tasks = new ArrayList<>();
    FloatingActionButton fabAddNewTask;
    RecyclerView taskRecView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabAddNewTask = findViewById(R.id.button_add_new_task);
        toolbar = findViewById(R.id.tool_bar);

        setSupportActionBar(toolbar);

        fabAddNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        taskRecView = findViewById(R.id.rec_view_tasks);
        taskRecView.setHasFixedSize(true);
        taskRecView.setLayoutManager(new LinearLayoutManager(this));

        tasks.add(new TaskModel(1, "A", 0));
        tasks.add(new TaskModel(2, "B", 0));
        tasks.add(new TaskModel(3, "C", 0));
        tasks.add(new TaskModel(4, "D", 1));
        tasks.add(new TaskModel(5, "E", 0));
        tasks.add(new TaskModel(6, "F", 0));
        tasks.add(new TaskModel(7, "G", 0));
        tasks.add(new TaskModel(8, "H", 0));
        tasks.add(new TaskModel(9, "I", 1));
        tasks.add(new TaskModel(10, "J", 0));
        tasks.add(new TaskModel(11, "K", 0));
        tasks.add(new TaskModel(12, "L", 0));
        tasks.add(new TaskModel(13, "M", 1));
        tasks.add(new TaskModel(14, "N", 0));

        TaskAdapter taskAdapter = new TaskAdapter(this, tasks);
        taskRecView.setAdapter(taskAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecView);
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_task);

        Button addBtn = dialog.findViewById(R.id.button_add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}