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
import android.widget.Toast;

import com.example.testappsolution.R;
import com.example.testappsolution.viewmodel.AuthViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SignUpFragment extends Fragment {

    private AuthViewModel authViewModel;
    TextInputEditText editText_username,editText_email,editText_password,editText_dob,editText_age;
    TextInputLayout textInputLayout_username, textInputLayout_email,textInputLayout_password,textInputLayout_dob,textInputLayout_age;
    TextView textView_signin;
    Button button_signup,button_signin;
    String username,email,password,dob;
    int age;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
     String ageValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        authViewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Navigation.findNavController(getView()).navigate(R.id.action_signUpFragment_to_signInFragment);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText_username=view.findViewById(R.id.tie_username);
        editText_email=view.findViewById(R.id.tie_email);
        editText_password=view.findViewById(R.id.tie_password);
        editText_dob=view.findViewById(R.id.tie_dob);
        editText_age=view.findViewById(R.id.tie_age);
        textView_signin=view.findViewById(R.id.tv_alreadyuser);

        textInputLayout_username=view.findViewById(R.id.til_username);
        textInputLayout_email=view.findViewById(R.id.til_email);
        textInputLayout_password=view.findViewById(R.id.til_password);
        textInputLayout_dob=view.findViewById(R.id.til_dob);
        textInputLayout_age=view.findViewById(R.id.til_age);

        button_signup=view.findViewById(R.id.btn_register);
        button_signin=view.findViewById(R.id.btn_login);

        button_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_signUpFragment_to_signInFragment);
            }
        });

        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username=editText_username.getText().toString();
                email=editText_email.getText().toString();
                password=editText_password.getText().toString();
                dob=editText_dob.getText().toString();

               //if(validateUserName(username)&&validateEmail(email)&&validatePassword(password)&&validateDob(dob)&&validateAge(ag))
               if(validateUserName(username)&&validateEmail(email)&&validatePassword(password)&&validateDob(dob))
               {
                   authViewModel.register(username,email, password,dob,ageValue);
                   // Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
               }
               }
        });
    }

    private boolean validateAge(String age) {
        if (TextUtils.isEmpty(age)) {
            textInputLayout_age.setError("Dob required");
            editText_age.requestFocus();
            return  false;
        }
        else {
            textInputLayout_age.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateDob(String dob) {
        if (TextUtils.isEmpty(dob)) {
            textInputLayout_dob.setError("Dob required");
            editText_dob.requestFocus();
            return  false;
        }
        else {
            textInputLayout_dob.setErrorEnabled(false);
            calculateAge(dob);
        }
        return true;
    }

    private void calculateAge(String dob) {
        Date date;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        try {
            date=simpleDateFormat.parse(dob);
            Calendar calendardob=Calendar.getInstance();
            calendardob.setTime(date);
             ageValue=getAge(calendardob.get(Calendar.DATE),calendardob.get(Calendar.MONTH),calendardob.get(Calendar.YEAR));
             editText_age.setText(ageValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String getAge(int day, int month, int year) {
        Calendar calendardob=Calendar.getInstance();
        Calendar calendartoday=Calendar.getInstance();
        calendardob.set(year,month,day);
        int m_today=calendartoday.get(Calendar.MONTH);
        int m_dob=calendardob.get(Calendar.MONTH);
        int age=calendartoday.get(Calendar.YEAR)-calendardob.get(Calendar.YEAR);
        if(m_dob>m_today){
            age--;
        }
        else  if(m_dob==m_today){
            int d_today=calendartoday.get(Calendar.DAY_OF_MONTH);
            int d_dob=calendardob.get(Calendar.DAY_OF_MONTH);
            if(d_dob>d_today){
                age--;
            }
        }
        return  age+"";
    }

    private boolean validateUserName(String username) {
        if (TextUtils.isEmpty(username)) {
            textInputLayout_username.setError("User Name required");
            editText_username.requestFocus();
            return  false;
        }
        else {
            textInputLayout_username.setErrorEnabled(false);
        }
        return true;
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