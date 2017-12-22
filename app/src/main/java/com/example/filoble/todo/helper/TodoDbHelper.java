package com.example.filoble.todo.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.example.filoble.todo.MainActivity;
import com.example.filoble.todo.data.TodoItem;
import com.example.filoble.todo.helper.TodoContract.TodoEntry;
/**
 * Created by filoble on 19/12/17.
 */

public class TodoDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "todos.db";
    private static final String SQL_CREATE_TODO_ENTRY = "CREATE TABLE " + TodoEntry.TABLE_NAME + "( "
            + TodoEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TodoEntry.COLUMN_NAME_TODO_TITLE + " TEXT NOT NULL, "
            + TodoEntry.COLUMN_NAME_TODO_DETAILS + " TEXT )";

    private static Context mContext;

    private static final String SQL_DELETE_TODO_ENTRIES = "DROP TABLE IF EXISTS " + TodoEntry.TABLE_NAME;

    public TodoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TODO_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TODO_ENTRY);
    }

}

