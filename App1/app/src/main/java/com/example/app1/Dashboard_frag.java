package com.example.app1;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.app1.Calories_Burned.Calories_Burned;
import com.example.app1.Calories_Burned.Calories_Burned_Object;
import com.example.app1.Database.DataBaseHelper;
import com.example.app1.FoodDiary.DietPlan_frag;
import com.example.app1.FoodDiary.Food;
import com.example.app1.Push_Up.Push_Up;
import com.example.app1.Push_Up.Pushup_frag;
import com.example.app1.StepCounter.StepDetectorActivity;
import com.example.app1.StopWatch.StopWatch;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Dashboard_frag extends Fragment {

    ImageView calories_burned,push_up_btn, step_detector, stopwatch, diet;

    TextView calories_burned_txt,calories_eaten_txt,running_txt,pushups_txt;


    private ProgressBar calories_burned_progressBar, calories_eaten_progressBar, running_progressBar, pushups_porgressBar;
    private int pushup_progress_process = 0;
    private int calories_progress_process = 0;
    private int steps_progress_process = 0;
    private int totalcal = 0;

    private static int target_calories_burned, target_calories_eaten, target_pushups;
    private float target_running;

    private int calories_burned_status = 0,calories_eaten_status = 0,running_status = 0,pushups_status = 0;
    String getCalories_burned,getCalories_eaten,getDistance,getPushups;

    private Handler handler = new Handler();

    private List<Push_Up> pushup_list = new ArrayList<>();
    private List<Food> food_list = new ArrayList<>();
    private List<Calories_Burned_Object> calories_burned_list = new ArrayList<>();

    Push_Up push_up;
    Food food;
    Calories_Burned_Object calories_burned_object;

    FirebaseAuth rauth;

    // Database
    DataBaseHelper db;
    private int todays_pushup,PB_pushup,total_calories_Burned;
    private String todaysDate= "",previousDate = "";
    private String caloriesBurnedPreviousDate = "",caloriesEatenPreviousDate = "";
    private SimpleDateFormat dateFormat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View layout = inflater.inflate(R.layout.fragment_dashboard_frag, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        rauth = FirebaseAuth.getInstance();

        push_up_btn = (ImageView) layout.findViewById(R.id.push_ups_btn);
        step_detector = (ImageView) layout.findViewById(R.id.running_btn);
        stopwatch = (ImageView) layout.findViewById(R.id.stopwatch_btn);
        diet = (ImageView) layout.findViewById(R.id.caloried_EatenBtn);
        calories_burned = (ImageView) layout.findViewById(R.id.caloried_burned_btn);

        calories_burned_progressBar = (ProgressBar) layout.findViewById(R.id.Calories_burned_progressBar);
        calories_eaten_progressBar = (ProgressBar) layout.findViewById(R.id.calories_eaten_progressBar);
        running_progressBar = (ProgressBar) layout.findViewById(R.id.running_progressBar);
        pushups_porgressBar = (ProgressBar) layout.findViewById(R.id.pushups_progressBar);

        calories_burned_txt = (TextView) layout.findViewById(R.id.calories_burned_status_text);
        calories_eaten_txt = (TextView) layout.findViewById(R.id.calories_eaten_status_text);
        running_txt = (TextView) layout.findViewById(R.id.running_status_text);
        pushups_txt = (TextView) layout.findViewById(R.id.pushups_status_text);

        Drawable calories_burned_drawable =  new ProgressDrawable(0x99FF6700, 0x446B6869);
        Drawable calories_eaten_drawable =  new ProgressDrawable(0x99F23B5F, 0x446B6869);
        Drawable running_drawable =  new ProgressDrawable(0x9910877E, 0x446B6869);
        Drawable pushups_drawable =  new ProgressDrawable(0x990F750F, 0x446B6869);

        calories_burned_progressBar.setProgressDrawable(calories_burned_drawable);
        calories_eaten_progressBar.setProgressDrawable(calories_eaten_drawable);
        running_progressBar.setProgressDrawable(running_drawable);
        pushups_porgressBar.setProgressDrawable(pushups_drawable);

        LoadInfo();


        // Database---------------------------------------------------------------------------------
        db = new DataBaseHelper(getContext());

        if (db.getPushups() != null){
            pushup_list.addAll(db.getPushups());
            get_Date_Pushups();
        }
        if (db.getSavedFood() != null){
            food_list.addAll(db.getSavedFood());
            getCalories_EatenPreviousDate();
        }
        if (db.getCalories_Burned() != null){
            calories_burned_list.addAll(db.getCalories_Burned());
            getCalories_BurnedPreviousDate();
        }

        resetNewDay();
        getTodays_Pushups();
        getTotalcal();
        getCaloriesBurned();

        getDate();
        // Resetting database if date is changed ---------------------------------------------------
        System.out.println("Todays Date"+todaysDate);
        System.out.println("pushup Date"+previousDate);
        System.out.println("calories burned Date"+caloriesBurnedPreviousDate);
        System.out.println("calories eaten Date"+caloriesEatenPreviousDate);

        // Button click listeners ------------------------------------------------------------------

        push_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Pushup_frag pushup_frag = new Pushup_frag();
                fragmentManager.beginTransaction().replace(R.id.fragment,pushup_frag).commit();
            }
        });
        calories_burned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Calories_Burned calories_burned = new Calories_Burned();
                fragmentManager.beginTransaction().replace(R.id.fragment,calories_burned).commit();
            }
        });
        step_detector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), StepDetectorActivity.class);
                startActivity(intent);
            }
        });
        stopwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), StopWatch.class);
                startActivity(intent);
            }
        });
        diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                DietPlan_frag dietPlan_frag = new DietPlan_frag();
                fragmentManager.beginTransaction().replace(R.id.fragment,dietPlan_frag).commit();
            }
        });

        return layout;
    }

    public void calories_burned_thread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //calories_burned_txt.setText(target_calories_burned);
                while (total_calories_Burned != target_calories_burned){
                    int speed = total_calories_Burned /50;
                    android.os.SystemClock.sleep(50);
                    if (calories_burned_status != total_calories_Burned){
                        calories_burned_status +=speed;
                    }
                    if (calories_burned_status >= total_calories_Burned){
                        calories_burned_status = total_calories_Burned;
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            calories_burned_progressBar.setProgress(calories_burned_status);
                        }
                    });
                }

            }
        }).start();
    }

    public void calories_eaten_thread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (totalcal != target_calories_eaten){
                    int speed = totalcal /50;
                    android.os.SystemClock.sleep(50);
                    if (totalcal != calories_eaten_status){
                        calories_eaten_status+=speed;
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            calories_eaten_progressBar.setProgress(calories_eaten_status);
                        }
                    });
                }


            }
        }).start();
    }

    public void running_thread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (running_status != target_running){
                    running_status++;
                    android.os.SystemClock.sleep(50);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            running_progressBar.setProgress(running_status);
                        }
                    });
                }

            }
        }).start();
    }
    public void pushup_thread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (todays_pushup != target_pushups){
                    int speed = todays_pushup/50;
                    android.os.SystemClock.sleep(50);
                    if (todays_pushup != pushups_status){
                        pushups_status += speed;
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pushups_porgressBar.setProgress(pushups_status);
                        }
                    });
                }

            }
        }).start();
    }


    public void LoadInfo(){
        String UserId = rauth.getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(UserId).child("Goals");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                getCalories_burned = String.valueOf(snapshot.child("Calories Burned").getValue());
                getCalories_eaten = String.valueOf(snapshot.child("Calories Eaten").getValue());
                getDistance = String.valueOf(snapshot.child("Running Distance").getValue());
                getPushups = String.valueOf(snapshot.child("Push-up's").getValue());

                System.out.println(Integer.parseInt(getCalories_burned));
                target_calories_burned = Integer.parseInt(getCalories_burned);
                target_calories_eaten  = Integer.parseInt(getCalories_eaten);
                target_running = Float.parseFloat(getDistance);
                target_pushups = Integer.parseInt(getPushups);

                pushups_porgressBar.setMax(target_pushups);
                calories_burned_progressBar.setMax(target_calories_burned);
                calories_eaten_progressBar.setMax(target_calories_eaten);
                running_progressBar.setMax((int) target_running);

                calories_burned_thread();
                calories_eaten_thread();
                running_thread();
                pushup_thread();

                pushups_txt.setText(todays_pushup+"/"+target_pushups);
                calories_burned_txt.setText(total_calories_Burned+"/"+getCalories_burned);
                calories_eaten_txt.setText(totalcal +"/"+getCalories_eaten);
                running_txt.setText("/"+getDistance);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getDate() {
        Calendar calendar;

        calendar = Calendar.getInstance();

        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        todaysDate = dateFormat.format(calendar.getTime());

    }

    public int getTodays_Pushups(){
        for (int i=0; i<pushup_list.size();i++){
            push_up = pushup_list.get(i);
            todays_pushup = push_up.getTodays_Pushups();
        }
        return todays_pushup;
    }
    public int getCaloriesBurned(){
        for (int i = 0; i < calories_burned_list.size(); i++){

            calories_burned_object = calories_burned_list.get(i);
            total_calories_Burned += calories_burned_object.getAmount_burned();
        }
        return total_calories_Burned;
    }
    public int getTotalcal() {
        totalcal = 0;
        for (int i = 0; i < food_list.size(); i++) {

            food = food_list.get(i);
            totalcal += food.getCalories();
        }
        return totalcal;
    }

    public String get_Date_Pushups(){
        if (db.getPushups() != null){
            for (int i =0; i<pushup_list.size();i++){
                push_up = pushup_list.get(i);
                previousDate = push_up.getDate();
            }
        }
        return previousDate;
    }

    public String getCalories_BurnedPreviousDate(){
        if (db.getCalories_Burned() != null){
            for (int i =0; i<calories_burned_list.size();i++){
                calories_burned_object = calories_burned_list.get(i);
                caloriesBurnedPreviousDate = calories_burned_object.getDATE();
            }
        }
        return caloriesBurnedPreviousDate;
    }
    public String getCalories_EatenPreviousDate(){
        if (db.getCalories_Burned() != null){
            for (int i =0; i<food_list.size();i++){
                food = food_list.get(i);
                caloriesEatenPreviousDate = food.getDate();
            }
        }
        return caloriesBurnedPreviousDate;
    }

    public int getPB_pushups(){
        for (int i =0; i<pushup_list.size();i++){
            push_up = pushup_list.get(i);
            PB_pushup = push_up.getPB_Pushups();
        }
        return PB_pushup;
    }


    public void resetNewDay() {
        get_Date_Pushups();
        getPB_pushups();
        getCalories_BurnedPreviousDate();
        getCalories_EatenPreviousDate();
        getDate();
        boolean pushup = false;
        boolean eaten = false;
        boolean burned = false;

        if (previousDate.equals(todaysDate) || previousDate.isEmpty()){
        }else {
            db.delete_pushup();
            db.addPushUp(new Push_Up(0,PB_pushup,todaysDate));
            pushup = true;
        }

        if (caloriesEatenPreviousDate.equals(todaysDate) ||caloriesEatenPreviousDate.isEmpty()){
        }else {
            db.deleteAllFood();
            eaten = true;
        }

        if (caloriesBurnedPreviousDate.equals(todaysDate) || caloriesBurnedPreviousDate.isEmpty()){
        }else {
            db.deleteAllCalories_Burned();
            burned = true;
        }
        if (burned == true || eaten == true || pushup == true){
            reloadFragment();
        }



    }

    public void reloadFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        Dashboard_frag dietPlan_frag = new Dashboard_frag();
        fragmentManager.beginTransaction().replace(R.id.fragment,dietPlan_frag).commit();
    }


}