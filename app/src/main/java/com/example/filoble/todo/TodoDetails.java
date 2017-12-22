package com.example.filoble.todo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.filoble.todo.data.Constants;

public class TodoDetails extends AppCompatActivity {

    private TextView mTextViewTodoName, mTextViewTodoDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_details);
        mTextViewTodoDetails = findViewById(R.id.ac_todo_detail_tv_todo_detail);
        mTextViewTodoName = findViewById(R.id.ac_todo_detail_tv_todo_name);
        Intent intent = getIntent();
        if(intent.hasExtra(Constants.KEY_TODO_NAME)){
            mTextViewTodoName.setText(intent.getStringExtra(Constants.KEY_TODO_NAME));
        }
        if(intent.hasExtra(Constants.KEY_TODO_DETAIL)){
            mTextViewTodoDetails.setText(intent.getStringExtra(Constants.KEY_TODO_DETAIL));
        }
    }
}
