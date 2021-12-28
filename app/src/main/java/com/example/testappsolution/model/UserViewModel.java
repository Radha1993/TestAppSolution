package com.example.testappsolution.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserViewModel extends ViewModel {
    List<User> userList = new ArrayList<>();
    List<User> currentuserList = new ArrayList<>();

    public MutableLiveData<List<User>> userMutableLiveData;
    public MutableLiveData<List<User>> currentUserModel;
    public MutableLiveData<DatabaseError> databaseErrorMutableLiveData;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    public UserViewModel() {
        userMutableLiveData=new MutableLiveData<>();
        currentUserModel=new MutableLiveData<>();
        databaseErrorMutableLiveData=new MutableLiveData<>();
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

   public MutableLiveData<List<User>> getData() {

        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    String uimatch = dataSnapshot.child("uid").getValue(String.class);
                    if(uimatch.equals(uid))
                    {
                        currentuserList.add(dataSnapshot.getValue(User.class));
                        currentUserModel.setValue(currentuserList);
                    }
                    else {
                        userList.add(dataSnapshot.getValue(User.class));
                        userMutableLiveData.setValue(userList);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        MutableLiveData<List<User>> data = new MutableLiveData<>();
        data.setValue(userList);
        return data;
    }
}