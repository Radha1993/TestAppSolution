package com.example.testappsolution.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testappsolution.R;
import com.example.testappsolution.model.TaskToDo;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {
    Context context;
    List<TaskToDo> taskToDoList;
    public TaskListAdapter(Context context, List<TaskToDo> taskToDoList) {
        this.context=context;
        this.taskToDoList=taskToDoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.task_list_item, parent, false);
        return new TaskListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaskToDo taskToDo=taskToDoList.get(position);
        holder.textView_title.setText(taskToDo.getTitle());
        holder.textView_details.setText(taskToDo.getDetails());
        holder.textView_date.setText(taskToDo.getDate());
    }

    @Override
    public int getItemCount() {
        return taskToDoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_title,textView_details,textView_date;
        public ViewHolder(View itemView) {
            super(itemView);
            textView_title=itemView.findViewById(R.id.tv_title);
            textView_details=itemView.findViewById(R.id.tv_details);
            textView_date=itemView.findViewById(R.id.tv_date);
        }
    }
}