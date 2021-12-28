package com.example.testappsolution.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testappsolution.R;
import com.example.testappsolution.model.User;
import com.example.testappsolution.model.UserViewModel;
import com.example.testappsolution.adapter.UserListAdapter;
import com.example.testappsolution.viewmodel.AuthViewModel;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainPage extends Fragment {
    private UserListAdapter userListAdapter;
    private RecyclerView recyclerView;
    CardView cardView;
    TextView textView_uname,textView_uemail,textView_udob,textView_uage;
    private UserViewModel userViewModel;
    FloatingActionButton floatingActionButton;
    List<User> data=new ArrayList<>();
    String uid;
    BottomAppBar bottomAppBar;
    private AuthViewModel authViewModel;
    ImageView imageView_logout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.userMutableLiveData.observe(this, users ->  {
                userListAdapter.notifyDataSetChanged();
        });

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        authViewModel.getLoggedStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                {
                    Navigation.findNavController(getView()).navigate(R.id.action_mainPage_to_signInFragment);
                }
            }
        });

        userViewModel.currentUserModel.observe(this,users -> {
            if(!userViewModel.currentUserModel.getValue().isEmpty()){
                 data=userViewModel.currentUserModel.getValue();
                Log.e("currentuser", String.valueOf(data.size()));
                for (User user:data)
                {
                    uid=user.getUid();
                    textView_uname.setText(user.getUsername());
                    textView_uemail.setText(user.getEmail());
                    textView_udob.setText(user.getDob());
                    textView_uage.setText(user.getAge());
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        bottomAppBar = view.findViewById(R.id.bottomnavar);
        recyclerView = view.findViewById(R.id.rv_userlist);
        cardView = view.findViewById(R.id.cardview_currentuser);
        textView_uname = view.findViewById(R.id.tv_uname);
        textView_uemail = view.findViewById(R.id.tv_uemail);
        textView_udob = view.findViewById(R.id.tv_udob);
        textView_uage = view.findViewById(R.id.tv_uage);
        imageView_logout = view.findViewById(R.id.iv_logout);

        imageView_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authViewModel.logout();
            }
        });

        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id=item.getItemId();
                if (id == R.id.on) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                if (id == R.id.off) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                return false;
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                SharedPreferences.Editor editor=getContext().getSharedPreferences("uidSharePreferences", Context.MODE_PRIVATE).edit();
                editor.putString("uid",uid);
                editor.apply();

                Intent intent=new Intent(getContext(),TaskList.class);
                getContext().startActivity(intent);
            }
        });
        floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewTask.getInstance().show(getActivity().getSupportFragmentManager(),NewTask.TAG);
            }
        });
        initRecyclerView();
    }

    private void initRecyclerView() {
        List<User> data=userViewModel.getData().getValue();
       // userListAdapter = new UserListAdapter(getContext(), userViewModel.getData().getValue());
        userListAdapter = new UserListAdapter(getContext(), data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(userListAdapter);
    }
}