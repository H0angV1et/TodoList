package com.example.todolist;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.example.todolist.Model.TaskModel;
import com.example.todolist.Utils.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class TaskDetailActivity extends AppCompatActivity {

    public static final String IS_NEW_TASK = "isNewTask";
    boolean isDueDateCleared = false;
    boolean isNewTask;
    Bundle bundle;
    Intent intent;
    String titleDetail;
    String dueDateDetail;

    DatabaseHelper database;
    Toolbar toolbar;
    EditText title;
    EditText dueDate;
    ImageView imageCancel;
    FloatingActionButton fabAddOrSave;

    public void updateDueDate(String date) {
        dueDate = findViewById(R.id.edit_input_date);
        dueDate.setText(date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        setBackButton();

        title = findViewById(R.id.edit_input_title);
        dueDate = findViewById(R.id.edit_input_date);
        imageCancel = findViewById(R.id.image_cancel);

        database = new DatabaseHelper(this);
        database.openDatabase();

        bundle = getIntent().getExtras();

        intent = new Intent(TaskDetailActivity.this, MainActivity.class);

        dueDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    imageCancel.setVisibility(View.VISIBLE);
                    isDueDateCleared = false;
                } else {
                    imageCancel.setVisibility(View.GONE);
                }
            }
        });

        dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), DatePickerFragment.TAG);
            }
        });

        imageCancel.setOnClickListener(view -> {
            dueDate.setText("");
            isDueDateCleared = true;
            imageCancel.setVisibility(View.GONE);
            if (!isNewTask) {
                database.updateDueDate(bundle.getInt("id"), "");
            }
        });

        checkActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isNewTask) {
            getMenuInflater().inflate(R.menu.menu_toolbar_detail, menu);
            for (int i = 0; i < toolbar.getMenu().size(); i++) {
                Drawable icon = toolbar.getMenu().getItem(i).getIcon();
                if (icon != null) {
                    icon.mutate();
                    icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                }
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (!isNewTask) {
                if (checkIsChanged()) {
                    DialogHelper.showUnsavedChangesDialog(this, new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                } else {
                    finish();
                }
            } else {
                finish();
            }
            return true;
        } else if (item.getItemId() == R.id.delete) {
            DialogHelper.showDeleteConfirmationDialog(this, new Runnable() {
                @Override
                public void run() {
                    database.deleteTask(bundle.getInt("id"));
                    intent.putExtra("message", "Task Deleted");
                    startActivity(intent);
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setBackButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void checkActivity() {
        Intent intent = getIntent();
        isNewTask = intent.getBooleanExtra(IS_NEW_TASK, true);
        fabAddOrSave = findViewById(R.id.button_add_or_save);
        if (isNewTask) {
            getSupportActionBar().setTitle("New Task");
            toolbar.setTitleTextAppearance(this, R.style.ToolbarTitleText);
            fabAddOrSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addTask();
                }
            });
        } else {
            getSupportActionBar().setTitle("");
            getDetailTask();
        }
    }

    public boolean checkIsChanged() {
        return !title.getText().toString().equals(titleDetail) ||
                !dueDate.getText().toString().equals(dueDateDetail);
    }

    public void addTask() {
        if (title.getText().toString().trim().isEmpty()) {
            Snackbar.make(fabAddOrSave, "Enter the title first", Snackbar.LENGTH_LONG).show();
        } else {
            TaskModel task = new TaskModel();
            task.setStatus(0);
            task.setTitle(title.getText().toString());
            task.setDueDate(dueDate.getText().toString());
            database.insertTask(task);
            intent.putExtra("message", "Task Added");
            startActivity(intent);
        }
    }

    public void getDetailTask() {
        titleDetail = bundle.getString("title");
        dueDateDetail = bundle.getString("dueDate");
        title.setText(titleDetail);
        dueDate.setText(dueDateDetail);
        updateTask(bundle);
    }

    public void updateTask(Bundle bundle) {
        fabAddOrSave = findViewById(R.id.button_add_or_save);
        fabAddOrSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().toString().trim().isEmpty()) {
                    Snackbar.make(fabAddOrSave, "Enter the title first", Snackbar.LENGTH_LONG).show();
                } else {
                    if (checkIsChanged()) {
                        database.updateTitle(bundle.getInt("id"), title.getText().toString());
                        database.updateDueDate(bundle.getInt("id"), dueDate.getText().toString());
                        intent.putExtra("message", "Task Saved");
                    } else {
                        intent.putExtra("message", "Task not modified");
                    }
                    startActivity(intent);
                }
            }
        });
    }
}
