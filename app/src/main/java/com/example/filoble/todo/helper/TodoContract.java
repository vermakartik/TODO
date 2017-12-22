package com.example.filoble.todo.helper;

import android.provider.BaseColumns;

/**
 * Created by filoble on 19/12/17.
 */

public class TodoContract {

    private TodoContract(){}

    public static class TodoEntry implements BaseColumns{

        public static final String TABLE_NAME = "todoEntry";
        public static final String COLUMN_NAME_TODO_TITLE = "TodoName";
        public static final String COLUMN_NAME_TODO_DETAILS = "TodoDetails";
        public static final String COLUMN_ID = _ID;

    }
}
