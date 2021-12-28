package com.example.testappsolution.views;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.testappsolution.R;
import com.example.testappsolution.viewmodel.AuthViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

public class SignInFragment extends Fragment {
    private AuthViewModel authViewModel;
    TextInputEditText editText_email,editText_password;
    TextInputLayout  textInputLayout_email,textInputLayout_password;
    Button button_signup,button_signin;
    String email,password;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        authViewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Navigation.findNavController(getView()).navigate(R.id.action_signInFragment_to_mainPage);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText_email=view.findViewById(R.id.tie_email);
        editText_password=view.findViewById(R.id.tie_password);

        textInputLayout_email=view.findViewById(R.id.til_email);
        textInputLayout_password=view.findViewById(R.id.til_password);

        button_signup=view.findViewById(R.id.btn_register);
        button_signin=view.findViewById(R.id.btn_login);

        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_signInFragment_to_signUpFragment);
            }
        });

        button_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=editText_email.getText().toString();
                password=editText_password.getText().toString();

                if(validateEmail(email)&&validatePassword(password))
                {
                    authViewModel.login(email, password);
                    // Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            textInputLayout_password.setError("Password required");
            editText_password.requestFocus();
            return  false;
        }
        else {
            textInputLayout_password.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            textInputLayout_email.setError("Email required");
            editText_email.requestFocus();
            return false;
        } else if (!email.trim().matches(emailPattern)) {
            textInputLayout_email.setError("Invalid Email address");
            editText_email.requestFocus();
            return false;
        }
        else {
            textInputLayout_email.setErrorEnabled(false);
        }
        return true;
    }
}