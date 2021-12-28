package com.example.testappsolution.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.testappsolution.R;
import com.example.testappsolution.model.TaskToDo;
import com.example.testappsolution.adapter.TaskListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TaskList extends AppCompatActivity {

     RecyclerView recyclerView;
    TaskListAdapter taskListAdapter;
     List<TaskToDo> tasklist;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        firebaseAuth = FirebaseAuth.getInstance();

        SharedPreferences sharedPreferences=this.getSharedPreferences("uidSharePreferences", Context.MODE_PRIVATE);
        String uid= sharedPreferences.getString("uid",null);

        recyclerView = findViewById(R.id.rv_tasklist);
        tasklist=new ArrayList<>();
        getTaskList(uid);
    }

    private void getTaskList(String uid) {
        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Tasks");
        DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {

                    String uimatch = dataSnapshot.child("uid").getValue(String.class);
                    if(uimatch.equals(uid))
                    {
                        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(uimatch).child("Tasks");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {

                                        tasklist.add(dataSnapshot.getValue(TaskToDo.class));
                                        if(!tasklist.isEmpty()){
                                            Log.e("listsize", String.valueOf(tasklist.size()));
                                        }
                                }
                                taskListAdapter = new TaskListAdapter(TaskList.this,tasklist);
                                recyclerView.setLayoutManager(new LinearLayoutManager(TaskList.this));
                               // recyclerView.addItemDecoration(new DividerItemDecoration(TaskList.this,DividerItemDecoration.VERTICAL));
                                recyclerView.setAdapter(taskListAdapter);
                                taskListAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(TaskList.this, "failed to load data", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(TaskList.this, "failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}