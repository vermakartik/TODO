package com.example.filoble.todo.data;

import android.content.Context;

import com.example.filoble.todo.MainActivity;
import com.example.filoble.todo.R;

/**
 * Created by filoble on 19/12/17.
 */

public class TodoItem {

    private String mTodoName;
    private String mTodoDetail;
    private long mUniqueID;

    public TodoItem(){}

    public TodoItem(String name, String details){
        mTodoName = name;
        mTodoDetail = details;
    }

    public String getName(){
        return mTodoName;
    }

    public String getDetail(){
        return mTodoDetail;
    }

    public void setTodo(String name, String detail){
        mTodoName = name;
        mTodoDetail = detail;
    }

    public String toString(Context context){
        return context.getString(R.string.TODO_NAME)  + mTodoName + " " + context.getString(R.string.TODO_DETAILS) + mTodoDetail;
    }

    public void setTodoName(String todoName){
        mTodoName = todoName;
    }
    public void setTodoDetail(String todoDetail){
        mTodoDetail = todoDetail;
    }

    public void setUniqueID(long uniqueID){
        mUniqueID = uniqueID;
    }

    public long getUniqueID(){
        return mUniqueID;
    }
}
