package com.example.testappsolution.repository;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import com.example.testappsolution.R;
import com.example.testappsolution.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthAppRepository {
     Application application;
     FirebaseAuth firebaseAuth;
     MutableLiveData<FirebaseUser> userLiveData;
     MutableLiveData<Boolean> loggedOutLiveData;

    public AuthAppRepository(Application application) {
        this.application = application;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userLiveData = new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();

        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.postValue(firebaseAuth.getCurrentUser());
           // loggedOutLiveData.postValue(false);
        }
    }

       public void login(String email, String password) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    //.addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(application.getApplicationContext(), "Login Failure: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                String uid=firebaseAuth.getCurrentUser().getUid();

                                userLiveData.postValue(firebaseAuth.getCurrentUser());
                                Toast.makeText(application.getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor=application.getApplicationContext()
                                        .getSharedPreferences("uidSharePreferences", Context.MODE_PRIVATE).edit();
                                editor.putString("uid",uid);
                                editor.apply();
                            }
                        }
                    });
    }

    public void register(String username, String email, String password, String dob, String age) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(application.getApplicationContext(), "Registration Failure: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                             else {
                                String uid=firebaseAuth.getCurrentUser().getUid();
                                User user = new User(username, email, password, dob, age,uid);
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(uid)
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            userLiveData.postValue(firebaseAuth.getCurrentUser());
                                            SharedPreferences.Editor editor=application.getApplicationContext()
                                                    .getSharedPreferences("uidSharePreferences", Context.MODE_PRIVATE).edit();
                                            editor.putString("uid",uid);
                                            editor.apply();
                                            Toast.makeText(application.getApplicationContext(), "Registration success", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

                        }
                    });

    }

    public void logOut() {
        firebaseAuth.signOut();
        loggedOutLiveData.postValue(true);
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
}