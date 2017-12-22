package com.example.filoble.todo.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.filoble.todo.R;
import com.example.filoble.todo.data.TodoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by filoble on 20/12/17.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    private List<TodoItem> mTodoItems;
    private OnItemCLickListener listener;

    public class TodoViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextViewTodoName, mTextViewTodoDetail;
        public long mId;

        public TodoViewHolder(View itemView) {
            super(itemView);
            mTextViewTodoName =  itemView.findViewById(R.id.tv_todo_name);
            mTextViewTodoDetail = itemView.findViewById(R.id.tv_todo_detail);
        }

        public boolean bind(int position, final OnItemCLickListener clickListener){
            final TodoItem todoItem = mTodoItems.get(position);
            mId = todoItem.getUniqueID();
            mTextViewTodoName.setText(todoItem.getName());
            mTextViewTodoDetail.setText(todoItem.getDetail());
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    listener.onItemCLick(todoItem);
                }
            });
            return true;
        }

        public long getId(){
            return mId;
        }
    }

    public interface OnItemCLickListener{
        void onItemCLick(TodoItem item);
    }

    public TodoAdapter(List<TodoItem> todoItems, OnItemCLickListener clickListener) {
        super();
        mTodoItems = todoItems;
        listener = clickListener;
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_list_item, parent, false);
        return new TodoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        holder.bind(position, listener);
    }

    @Override
    public int getItemCount() {
        return (mTodoItems == null)?0:mTodoItems.size();
    }

    public void appendItem(TodoItem todoItem){
        if(mTodoItems == null) {
            mTodoItems = new ArrayList<>();
        }
        mTodoItems.add(todoItem);
        notifyDataSetChanged();
    }

    public void setTodoItems(List<TodoItem> todoItems){
        mTodoItems = todoItems;
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        mTodoItems.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(TodoItem todoItem, int position){
        mTodoItems.add(position, todoItem);
        notifyItemInserted(position);
    }

}
