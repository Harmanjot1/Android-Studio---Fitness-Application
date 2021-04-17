package com.example.app1.LoadingScreens;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app1.Login_Logout.Login;
import com.example.app1.Push_Up.Pushup_frag;
import com.example.app1.R;
import com.example.app1.SetGoal;
import com.example.app1.UserInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Pushup_loadingscreen extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_pushup_loadingscreen, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        BottomNavigationView navBar = getActivity().findViewById(R.id.bottomNavigationView);
        navBar.removeAllViews();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } finally {

                }
                FragmentManager fragmentManager = getFragmentManager();
                Pushup_frag pushup_frag = new Pushup_frag();
                fragmentManager.beginTransaction().replace(R.id.fragment,pushup_frag).addToBackStack(null).commit();
            }

        }).start();




        return layout;
    }

}