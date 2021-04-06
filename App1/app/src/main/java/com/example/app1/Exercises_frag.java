package com.example.app1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.renderscript.ScriptIntrinsicLUT;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class Exercises_frag extends Fragment {

    private final ArrayList<Exercise_Item> exercise_items = new ArrayList<>();
    private final ArrayList<Exercise_Item> bicep = new ArrayList<>();
    private final ArrayList<Exercise_Item> tricep = new ArrayList<>();
    private final ArrayList<Exercise_Item> chest = new ArrayList<>();
    private final ArrayList<Exercise_Item> shoulders = new ArrayList<>();
    private final ArrayList<Exercise_Item> back = new ArrayList<>();
    private final ArrayList<Exercise_Item> forearm = new ArrayList<>();
    private final ArrayList<Exercise_Item> core = new ArrayList<>();
    private final ArrayList<Exercise_Item> glutes = new ArrayList<>();
    private final ArrayList<Exercise_Item> upper_legs = new ArrayList<>();
    private final ArrayList<Exercise_Item> cardio = new ArrayList<>();


    private RecyclerView mRecyclerView;
    private Exercise_Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_exercises_frag, container, false);

        //showing action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        // initialising recyclerview id
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView2);
        setHasOptionsMenu(true);

        //---------------------------------------------------------------------------------------------------------------
        //Exercises
        //Chest
        chest.add(new Exercise_Item(R.drawable.barbell_pullover, "Barbell Pullover", ""));
        chest.add(new Exercise_Item(R.drawable.bench_cable_flys, "Bench Cable Flys", ""));
        chest.add(new Exercise_Item(R.drawable.butterflies, "Butterflies", ""));
        chest.add(new Exercise_Item(R.drawable.decline_push_ups, "Decline Push-Ups", ""));
        chest.add(new Exercise_Item(R.drawable.flat_bench_dumbell_flyes, "Flat Bench Dumbell Flyes", ""));

        //Biceps
        bicep.add(new Exercise_Item(R.drawable.biceps, "Bicep curl", "bottom to top"));
        bicep.add(new Exercise_Item(R.drawable.biceps, "Line 2", "Line 1"));

        //---------------------------------------------------------------------------------------------------------------


        exercise_items.addAll(chest);
        exercise_items.addAll(bicep);
        exercise_items.addAll(tricep);
        exercise_items.addAll(shoulders);
        exercise_items.addAll(back);
        exercise_items.addAll(forearm);
        exercise_items.addAll(core);
        exercise_items.addAll(glutes);
        exercise_items.addAll(upper_legs);
        exercise_items.addAll(cardio);
        Collections.shuffle(exercise_items);


        // building recyclerview using adapter and passing exercise_item through it
        mRecyclerView = v.findViewById(R.id.recyclerView2);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new Exercise_Adapter(exercise_items);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return v;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.exercise_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.Bicep_menu:
                exercise_items.clear();
                bicep.clear();
                exercise_items.addAll(bicep);
                update();
                return true;
            case R.id.Tricep_menu:
                exercise_items.clear();


                update();
                return true;

            case R.id.Shoulders_menu:
                exercise_items.clear();


                update();
                return true;

            case R.id.Back_menu:
                exercise_items.clear();


                update();
                return true;

            case R.id.Chest_menu:
                exercise_items.clear();

                exercise_items.addAll(chest);
                update();
                return true;

            case R.id.Forearm_menu:
                exercise_items.clear();


                update();
                return true;

            case R.id.Core_menu:
                exercise_items.clear();


                update();
                return true;

            case R.id.Glutes_menu:
                exercise_items.clear();


                update();
                return true;

            case R.id.Upper_Legs_menu:
                exercise_items.clear();


                update();
                return true;

            case R.id.Cardio:
                exercise_items.clear();


                update();
                return true;
        }

        return super.onOptionsItemSelected(item); // important line
    }

    public void update() {
        mAdapter.notifyDataSetChanged();
    }
}