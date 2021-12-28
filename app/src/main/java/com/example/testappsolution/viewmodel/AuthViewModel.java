package com.example.testappsolution.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.testappsolution.model.User;
import com.example.testappsolution.repository.AuthAppRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class AuthViewModel extends AndroidViewModel {
     AuthAppRepository authAppRepository;
     MutableLiveData<FirebaseUser> userLiveData;
     MutableLiveData<Boolean> loggedStatus;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        authAppRepository = new AuthAppRepository(application);
        userLiveData = authAppRepository.getUserLiveData();
        loggedStatus = authAppRepository.getLoggedOutLiveData();
    }

    public void login(String email, String password) {
        authAppRepository.login(email, password);
    }

    public void register(String username, String email, String password, String dob, String age) {
        authAppRepository.register(username,email, password,dob,age);
    }

    public void logout() {
        authAppRepository.logOut();
    }

        public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoggedStatus() {
        return loggedStatus;
    }
}