package com.example.app1.FoodDiary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app1.Dashboard_frag;
import com.example.app1.Database.DataBaseHelper;
import com.example.app1.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewFoodForm_frag extends Fragment {

    Button cancle, save;
    EditText ET_Name, ET_Calories, ET_Protein;
    Button breakfast_btn, lunch_btn, dinner_btn, snacks_btn;
    Switch favFoodSwitch;
    int diet_picture;
    DataBaseHelper db;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String todaysDate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container.removeAllViews();
        final View layout = inflater.inflate(R.layout.fragment_new_food_form_frag, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        cancle = layout.findViewById(R.id.button_cancel);
        save = layout.findViewById(R.id.button_save_add);

        ET_Name = layout.findViewById(R.id.food_name_editTxt);
        ET_Calories = layout.findViewById(R.id.calories_editTxt);
        ET_Protein = layout.findViewById(R.id.protein_editTxt);

        breakfast_btn = layout.findViewById(R.id.breakfast_btn);
        lunch_btn = layout.findViewById(R.id.lunch_btn);
        dinner_btn = layout.findViewById(R.id.dinner_btn);
        snacks_btn = layout.findViewById(R.id.snacks_btn);
        favFoodSwitch = layout.findViewById(R.id.food_fav_switch);

        breakfast_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diet_picture = 0;
            }
        });
        lunch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diet_picture = 1;
            }
        });
        dinner_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diet_picture = 2;
            }
        });
        snacks_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diet_picture = 3;
            }
        });


        db = new DataBaseHelper(getContext());
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDate();

                String stringName = ET_Name.getText().toString();

                if (favFoodSwitch.isChecked()){
                    db.fav_addFood(new Food(-1, ET_Name.getText().toString(),
                            Integer.parseInt(ET_Calories.getText().toString()),
                            Integer.parseInt(ET_Protein.getText().toString()),
                            diet_picture, todaysDate));

                    db.addFood(new Food(-1, ET_Name.getText().toString(),
                            Integer.parseInt(ET_Calories.getText().toString()),
                            Integer.parseInt(ET_Protein.getText().toString()),
                            diet_picture, todaysDate));

                    FragmentManager fragmentManager = getFragmentManager();
                    DietPlan_frag dietPlan_frag = new DietPlan_frag();
                    fragmentManager.beginTransaction().replace(R.id.fragment,dietPlan_frag).commit();

                }else{
                    if (stringName.isEmpty() && ET_Calories!= null){
                        Toast.makeText(getContext(), "Please enter the name and calories",
                                Toast.LENGTH_LONG).show();
                    }else {

                        db.addFood(new Food(-1, ET_Name.getText().toString(),
                                Integer.parseInt(String.valueOf(ET_Calories.getText())),
                                Integer.parseInt(String.valueOf(ET_Protein.getText())),
                                diet_picture, todaysDate));

                        FragmentManager fragmentManager = getFragmentManager();
                        DietPlan_frag dietPlan_frag = new DietPlan_frag();
                        fragmentManager.beginTransaction().replace(R.id.fragment,dietPlan_frag).commit();
                    }

                }


            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DietPlan_frag dietPlan_frag = new DietPlan_frag();
                fragmentManager.beginTransaction().replace(R.id.fragment,dietPlan_frag).commit();

            }
        });


        return layout;
    }

    public void getDate() {
        calendar = Calendar.getInstance();

        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        todaysDate = dateFormat.format(calendar.getTime());
    }





}