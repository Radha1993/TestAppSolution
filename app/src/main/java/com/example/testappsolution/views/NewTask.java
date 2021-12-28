package com.example.testappsolution.views;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testappsolution.R;
import com.example.testappsolution.model.TaskToDo;
import com.example.testappsolution.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewTask extends BottomSheetDialogFragment {
    public static final String TAG =NewTask.class.getSimpleName() ;
    TextInputEditText editText_title,editText_descriptione;
    TextView textView_date;
    Button button_save;
    String title,details,date;
     FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    public static NewTask getInstance() {
        return new NewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     // Inflate the layout for this fragment
        return inflater.inflate(R.layout.newtask, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            editText_title=view.findViewById(R.id.tie_title);
            editText_descriptione=view.findViewById(R.id.tie_description);
            textView_date=view.findViewById(R.id.tv_date);
            button_save=view.findViewById(R.id.btn_save);
             textView_date.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     Calendar calendar=Calendar.getInstance();
                     int day=calendar.get(Calendar.DATE);
                     int month=calendar.get(Calendar.MONTH);
                     int year=calendar.get(Calendar.YEAR);

                     DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                         @Override
                         public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                             month=month+1;
                             textView_date.setText(day+ "/" +month+ "/" +year);
                         }
                     },year,month,day);
                     datePickerDialog.show();
                 }
             });

             button_save.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     title=editText_title.getText().toString();
                     details=editText_descriptione.getText().toString();
                     date=textView_date.getText().toString();

                     TaskToDo task = new TaskToDo(title, details, date);
                     String uid=firebaseAuth.getCurrentUser().getUid();
                     String key = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Tasks").push().getKey();

                     Map<String, Object> childUpdates = new HashMap<>();
                     childUpdates.put( key, task);


                     FirebaseDatabase.getInstance().getReference("Users")
                             .child(uid).child("Tasks").updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                         @Override
                         public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                             SharedPreferences.Editor editor=getContext().getSharedPreferences("uidSharePreferences", Context.MODE_PRIVATE).edit();
                             editor.putString("uid",uid);
                             editor.apply();

                             Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();
                             Intent intent=new Intent(getContext(),TaskList.class);
                             //intent.putExtra("uid",uid);
                             getContext().startActivity(intent);
                         }
                     });
                    /* FirebaseDatabase.getInstance().getReference("Users")
                             .child(uid).child("Tasks")
                             .setValue(task).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if (task.isSuccessful()) {
                                // userLiveData.postValue(firebaseAuth.getCurrentUser());
                                 Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();
                                 Intent intent=new Intent(getContext(),TaskList.class);
                                 getContext().startActivity(intent);
                             }
                         }
                     });*/
                 }
             });
           }
}