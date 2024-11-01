package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final static String DATABASE = "sqlite_todoList";
    private final static String TABLE = "task";
    private final static String ID = "id";
    private final static String TITLE = "title";
    private final static String STATUS = "status";

    private SQLiteDatabase database;

    private final static String TABLE_CREATE = String.format("CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT, " +
                    "%s TEXT)",
            TABLE, ID, TITLE, STATUS);

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
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

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        database.update(TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        database.delete(TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }

}
