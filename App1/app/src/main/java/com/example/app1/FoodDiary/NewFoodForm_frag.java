package com.example.app1.FoodDiary;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app1.Dashboard_frag;
import com.example.app1.Database.DataBaseHelper;
import com.example.app1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewFoodForm_frag extends Fragment {

    Button cancle, save;
    EditText ET_Name, ET_Calories, ET_Protein;
    Button breakfast_btn, lunch_btn, dinner_btn, snacks_btn;
    Switch favFoodSwitch;
    int diet_picture;
    DataBaseHelper db;
    ImageView help;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String todaysDate;

    //Firebase
    FirebaseAuth rauth;

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

        // Get firebase instance
        rauth = FirebaseAuth.getInstance();

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

        help = (ImageView) layout.findViewById(R.id.AddFroomForm_help);

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

                    // Firebase Update
                    String UserId = rauth.getCurrentUser().getUid();

                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(UserId).child("Targets");

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String getCalories_burned = String.valueOf(snapshot.child("Goal: Calories Burned").getValue());
                            String getCalories_eaten = String.valueOf(snapshot.child("Goal: Calories Eaten").getValue());
                            String getDistance = String.valueOf(snapshot.child("Goal: Running Distance").getValue());
                            String getPushups = String.valueOf(snapshot.child("Goal: Push-up's").getValue());

                            String challenge_getCalories_burned = String.valueOf(snapshot.child("Challenge: Calories Burned").getValue());
                            String challenge_getCalories_eaten = String.valueOf(snapshot.child("Challenge: Calories Eaten").getValue());
                            String challenge_getDistance = String.valueOf(snapshot.child("Challenge: Running Distance").getValue());
                            String challenge_getPushups = String.valueOf(snapshot.child("Challenge: Push-up's").getValue());

                            // Update pushups
                            int challenge_eaten = Integer.parseInt(challenge_getCalories_eaten);
                            int newTotal = challenge_eaten + Integer.parseInt(ET_Calories.getText().toString());

                            // Create HashMap
                            Map targetMap = new HashMap<>();

                            targetMap.put("Goal: Calories Burned",getCalories_burned);
                            targetMap.put("Goal: Calories Eaten",getCalories_eaten);
                            targetMap.put("Goal: Running Distance",getDistance);
                            targetMap.put("Goal: Push-up's",getPushups);

                            targetMap.put("Challenge: Calories Burned",challenge_getCalories_burned);
                            targetMap.put("Challenge: Calories Eaten",newTotal);
                            targetMap.put("Challenge: Running Distance",challenge_getDistance);
                            targetMap.put("Challenge: Push-up's",challenge_getPushups);

                            databaseReference.setValue(targetMap);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    FragmentManager fragmentManager = getFragmentManager();
                    DietPlan_frag dietPlan_frag = new DietPlan_frag();
                    fragmentManager.beginTransaction().replace(R.id.fragment,dietPlan_frag).addToBackStack(null).commit();

                }else{
                    if (stringName.isEmpty() && ET_Calories!= null){
                        Toast.makeText(getContext(), "Please enter the name and calories",
                                Toast.LENGTH_LONG).show();
                    }else {

                        db.addFood(new Food(-1, ET_Name.getText().toString(),
                                Integer.parseInt(String.valueOf(ET_Calories.getText())),
                                Integer.parseInt(String.valueOf(ET_Protein.getText())),
                                diet_picture, todaysDate));

                        String UserId = rauth.getCurrentUser().getUid();

                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(UserId).child("Targets");

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String getCalories_burned = String.valueOf(snapshot.child("Goal: Calories Burned").getValue());
                                String getCalories_eaten = String.valueOf(snapshot.child("Goal: Calories Eaten").getValue());
                                String getDistance = String.valueOf(snapshot.child("Goal: Running Distance").getValue());
                                String getPushups = String.valueOf(snapshot.child("Goal: Push-up's").getValue());

                                String challenge_getCalories_burned = String.valueOf(snapshot.child("Challenge: Calories Burned").getValue());
                                String challenge_getCalories_eaten = String.valueOf(snapshot.child("Challenge: Calories Eaten").getValue());
                                String challenge_getDistance = String.valueOf(snapshot.child("Challenge: Running Distance").getValue());
                                String challenge_getPushups = String.valueOf(snapshot.child("Challenge: Push-up's").getValue());

                                // Update pushups
                                int challenge_eaten = Integer.parseInt(challenge_getCalories_eaten);
                                int newTotal = challenge_eaten + Integer.parseInt(ET_Calories.getText().toString());

                                // Create HashMap
                                Map targetMap = new HashMap<>();

                                targetMap.put("Goal: Calories Burned",getCalories_burned);
                                targetMap.put("Goal: Calories Eaten",getCalories_eaten);
                                targetMap.put("Goal: Running Distance",getDistance);
                                targetMap.put("Goal: Push-up's",getPushups);

                                targetMap.put("Challenge: Calories Burned",challenge_getCalories_burned);
                                targetMap.put("Challenge: Calories Eaten",newTotal);
                                targetMap.put("Challenge: Running Distance",challenge_getDistance);
                                targetMap.put("Challenge: Push-up's",challenge_getPushups);

                                databaseReference.setValue(targetMap);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        FragmentManager fragmentManager = getFragmentManager();
                        DietPlan_frag dietPlan_frag = new DietPlan_frag();
                        fragmentManager.beginTransaction().replace(R.id.fragment,dietPlan_frag).addToBackStack(null).commit();
                    }

                }


            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DietPlan_frag dietPlan_frag = new DietPlan_frag();
                fragmentManager.beginTransaction().replace(R.id.fragment,dietPlan_frag).addToBackStack(null).commit();

            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Help");
                alert.setMessage("Select food category from Breakfast, Lunch, Dinner or Snacks. \nAdd food name, calories and protein. \n\nTo add food to favourite for a faster process in the future, simply slide 'Save to Favourite' bar to the right.");
                alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.create().show();
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