package com.example.app1.LoadingScreens;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app1.Calories_Burned.Calories_Burned;
import com.example.app1.Push_Up.Pushup_frag;
import com.example.app1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Calories_Burned_loadingscreen extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_calories__burned_loadingscreen, container, false);
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
                Calories_Burned calories_burned = new Calories_Burned();
                fragmentManager.beginTransaction().replace(R.id.fragment,calories_burned).addToBackStack(null).commit();
            }

        }).start();

        return layout;
    }

}