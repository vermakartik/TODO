package com.example.filoble.todo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filoble.todo.data.Constants;

import java.util.zip.Inflater;

public class AddTodo extends AppCompatActivity {
    private Intent mIntent;
    private TextView mTodoNameEditText, mTodoDetailEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        mIntent = getIntent();
        mTodoNameEditText = findViewById(R.id.et_todo_name);
        mTodoDetailEditText = findViewById(R.id.et_todo_details);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedId = item.getItemId();
        switch (selectedId){
            case R.id.action_save:
                String todoName = mTodoNameEditText.getText().toString();
                String todoDetail = mTodoDetailEditText.getText().toString();
                if(checkForEmpty(todoName) == false){
                    Toast.makeText(this, getString(R.string.TITLE_NOT_EMPTY), Toast.LENGTH_SHORT).show();
                    return false;
                }
                mIntent.putExtra(Constants.KEY_TODO_NAME, todoName);
                mIntent.putExtra(Constants.KEY_TODO_DETAIL, todoDetail);
                setResult(RESULT_OK, mIntent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkForEmpty(String string){
        if(string  == null || string.isEmpty() || string.equals("")) return false;
        return true;
    }
}
