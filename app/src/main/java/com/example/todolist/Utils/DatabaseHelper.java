package com.example.todolist.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.todolist.Model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final static String DATABASE = "sqlite_todoList";
    private final static String TABLE = "task";
    private final static String ID = "id";
    private final static String TITLE = "title";
    private final static String DUE_DATE = "dueDate";
    private final static String STATUS = "status";

    private SQLiteDatabase database;

    private final static String TABLE_CREATE = String.format("CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT, " +
                    "%s INTEGER, " +
                    "%s TEXT)",
            TABLE, ID, TITLE, DUE_DATE, STATUS);

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    public void openDatabase() {
        database = getWritableDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);

        Log.v(this.getClass().getName(), TABLE +
                "database upgrade to version" + i1 + " - old data lost"
        );
        onCreate(db);
    }

    public void insertTask(TaskModel task){
        ContentValues cv = new ContentValues();
        cv.put(TITLE, task.getTitle());
        cv.put(DUE_DATE, task.getDueDate());
        cv.put(STATUS, 0);
        database.insert(TABLE, null, cv);
    }

    public List<TaskModel> getAllTasks(){
        List<TaskModel> taskList = new ArrayList<>();
        Cursor cur = null;
        database.beginTransaction();
        try{
            cur = database.query(TABLE, null,
                    null, null,
                    null, null,
                    null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        TaskModel task = new TaskModel();
                        task.setId(cur.getInt(cur.getColumnIndexOrThrow(ID)));
                        task.setTitle(cur.getString(cur.getColumnIndexOrThrow(TITLE)));
                        task.setDueDate(cur.getString(cur.getColumnIndexOrThrow(DUE_DATE)));
                        task.setStatus(cur.getInt(cur.getColumnIndexOrThrow(STATUS)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            database.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    public void updateStatusById(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        database.update(TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTitle(int id, String title) {
        ContentValues cv = new ContentValues();
        cv.put(TITLE, title);
        database.update(TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateDueDate(int id, String dueDate){
        ContentValues cv = new ContentValues();
        cv.put(DUE_DATE, dueDate);
        database.update(TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        database.delete(TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}
