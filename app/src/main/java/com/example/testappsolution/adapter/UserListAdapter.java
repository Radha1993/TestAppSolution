package com.example.testappsolution.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testappsolution.R;
import com.example.testappsolution.model.User;
import com.example.testappsolution.views.TaskList;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
   Context context;
   List<User> userList;

    public UserListAdapter(Context context, List<User> userList) {
        this.context=context;
        this.userList=userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user=userList.get(position);
        holder.textView.setText(user.getUsername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, TaskList.class);

                SharedPreferences.Editor editor=context
                        .getSharedPreferences("uidSharePreferences", Context.MODE_PRIVATE).edit();
                editor.putString("uid",user.getUid());
                editor.apply();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.tv_username);
        }
    }
}