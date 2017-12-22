package com.example.filoble.todo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.filoble.todo.Adapter.TodoAdapter;
import com.example.filoble.todo.data.Constants;
import com.example.filoble.todo.data.TodoItem;
import com.example.filoble.todo.helper.TodoDbHelper;
import com.example.filoble.todo.helper.TodoContract.TodoEntry;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private static TodoAdapter mTodoAdapter;
    private SwipeController mSwipeController;
    private ItemTouchHelper mItemTouchHelper;
    private FloatingActionButton mFab;
    private TodoDbHelper mTodoHelper;
    private List<TodoItem> mTodoItems;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFab = findViewById(R.id.fab_main_activity);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mToast != null){
                    mToast.cancel();
                }
                Intent intent = new Intent(MainActivity.this, AddTodo.class);
                startActivityForResult(intent, Constants.REQUEST_ADD_TODO);
            }
        });
        mTodoHelper = new TodoDbHelper(getApplicationContext());
        mSwipeController = new SwipeController();
        mItemTouchHelper = new ItemTouchHelper(mSwipeController);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_todo_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        mTodoAdapter = new TodoAdapter(mTodoItems, new TodoAdapter.OnItemCLickListener() {
            @Override
            public void onItemCLick(TodoItem item) {
                if(mToast != null){
                    mToast.cancel();
                }
                Intent intent = new Intent(MainActivity.this, TodoDetails.class);
                intent.putExtra(Constants.KEY_TODO_NAME, item.getName());
                intent.putExtra(Constants.KEY_TODO_DETAIL, item.getDetail());
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mTodoAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.REQUEST_ADD_TODO && resultCode == RESULT_OK){
            TodoItem todoItem = new TodoItem();
            if(data.hasExtra(Constants.KEY_TODO_NAME)){
                todoItem.setTodoName(data.getStringExtra(Constants.KEY_TODO_NAME));
            }else{
                return;
            }
            if(data.hasExtra(Constants.KEY_TODO_DETAIL)){
                todoItem.setTodoDetail(data.getStringExtra(Constants.KEY_TODO_DETAIL));
            }else {
                return;
            }
            insert(todoItem);
        }
    }

    @Override
    protected void onStart() {
        readAll();
        super.onStart();
    }

    private void readAll(){
        class Execute extends AsyncTask<Void, Void, Cursor>{
            @Override
            protected Cursor doInBackground(Void... voids) {
                SQLiteDatabase db = mTodoHelper.getReadableDatabase();
                String[] projections = {TodoEntry.COLUMN_ID, TodoEntry.COLUMN_NAME_TODO_TITLE, TodoEntry.COLUMN_NAME_TODO_DETAILS};
                Cursor cursor = db.query(TodoEntry.TABLE_NAME, projections, null, null, null, null, null);
                return cursor;
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                mTodoItems = new ArrayList<>();
                while(cursor.moveToNext()){
                    TodoItem todoItem = new TodoItem();
                    todoItem.setTodoName(cursor.getString(cursor.getColumnIndex(TodoEntry.COLUMN_NAME_TODO_TITLE)));
                    todoItem.setTodoDetail(cursor.getString(cursor.getColumnIndex(TodoEntry.COLUMN_NAME_TODO_DETAILS)));
                    todoItem.setUniqueID(cursor.getInt(cursor.getColumnIndex(TodoEntry.COLUMN_ID)));
                    mTodoItems.add(todoItem);
                }
                mTodoAdapter.setTodoItems(mTodoItems);
                super.onPostExecute(cursor);
            }
        }
        new Execute().execute();
    }

    private void insert(final TodoItem todoItem){
        class Execute extends AsyncTask<Void, Void, Void>{
            long mId;
            @Override
            protected Void doInBackground(Void... voids) {
                SQLiteDatabase db = mTodoHelper.getWritableDatabase();
//                Log.i(TodoDbHelper.class.getName(), "Got the instance");
                ContentValues contentValues = new ContentValues();
                contentValues.put(TodoEntry.COLUMN_NAME_TODO_TITLE, todoItem.getName());
                contentValues.put(TodoEntry.COLUMN_NAME_TODO_DETAILS, todoItem.getDetail());
                mId = db.insert(TodoEntry.TABLE_NAME, "", contentValues);
//                Log.i(MainActivity.class.getSimpleName(), "Id ..... " + mId);
                todoItem.setUniqueID(mId);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
//                Log.i(TodoDbHelper.class.getName(), "String added the values!");
                mTodoAdapter.appendItem(todoItem);
                createToast(getString(R.string.TODO_ADDED));
            }
        }
        new Execute().execute();
    }
    private class SwipeController extends ItemTouchHelper.Callback{

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(0, ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            Log.i(MainActivity.class.getName(), Integer.toString(viewHolder.getAdapterPosition()));
            mTodoAdapter.removeItem(viewHolder.getAdapterPosition());
            remove(((TodoAdapter.TodoViewHolder) viewHolder).getId());
        }
    }

    private void remove(long id){
        class Execute extends AsyncTask<Long, Void, Void>{
            @Override
            protected Void doInBackground(Long... longs) {
                SQLiteDatabase db = mTodoHelper.getWritableDatabase();
                if(longs == null) return null;
                String selection = TodoEntry._ID + "=?";
                String[] selectionArgs = { Long.toString(longs[0]).trim() };
                db.delete(TodoEntry.TABLE_NAME, selection, selectionArgs);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                createToast(getString(R.string.TODO_REMOVED));
            }
        }
        new Execute().execute(id);
    }

    private void createToast(String message){
        if(mToast != null){
            mToast.cancel();
        }
        mToast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
