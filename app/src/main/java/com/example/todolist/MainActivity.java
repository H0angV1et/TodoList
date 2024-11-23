package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Adapter.TaskAdapter;
import com.example.todolist.Model.TaskModel;
import com.example.todolist.Utils.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper database;
    TaskAdapter taskAdapter;

    List<TaskModel> taskList = new ArrayList<>();
    FloatingActionButton fabAddNewTask;
    RecyclerView taskRecView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new DatabaseHelper(this);
        database.openDatabase();

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        taskRecView = findViewById(R.id.rec_view_tasks);
        taskRecView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(this, taskList);
        taskRecView.setAdapter(taskAdapter);

        taskList = database.getAllTasks();
        Collections.reverse(taskList);
        taskAdapter.setTasks(taskList);

        Intent i_task_detail = new Intent(MainActivity.this, TaskDetailActivity.class);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String message = bundle.getString("message");
            if (message != null) {
                Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
            }
        }

        fabAddNewTask = findViewById(R.id.button_add_new_task);
        fabAddNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i_task_detail.putExtra(TaskDetailActivity.IS_NEW_TASK, true);
                startActivity(i_task_detail);
            }
        });
    }
}
