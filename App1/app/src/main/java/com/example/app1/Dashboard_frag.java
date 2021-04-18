package com.example.app1;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

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
import com.example.app1.Challenges.Challenge_frag;
import com.example.app1.Database.DataBaseHelper;
import com.example.app1.FoodDiary.DietPlan_frag;
import com.example.app1.FoodDiary.Food;
import com.example.app1.LoadingScreens.Calories_Burned_loadingscreen;
import com.example.app1.LoadingScreens.Calories_EatenLoadingScreen;
import com.example.app1.LoadingScreens.Challenge_loadingscreen;
import com.example.app1.LoadingScreens.Pushup_loadingscreen;
import com.example.app1.Push_Up.Push_Up;
import com.example.app1.Push_Up.Pushup_frag;
import com.example.app1.StepCounter.StepDetectorActivity;
import com.example.app1.StepCounter.Steps;
import com.example.app1.StopWatch.StopWatch;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

    ImageView calories_burned,push_up_btn, step_detector, stopwatch, diet,challenge;

    TextView calories_burned_txt,calories_eaten_txt,running_txt,pushups_txt,challenge_txt;

    TextView overallTxt;

    private ProgressBar calories_burned_progressBar, calories_eaten_progressBar, running_progressBar, pushups_porgressBar,challenge_progressBar;
    private ProgressBar overall_progressBar;
    private int pushup_progress_process = 0;
    private int calories_progress_process = 0;
    private int steps_progress_process = 0;
    private int totalcal = 0;
    private static int challenges_count = 0;
    private static int overall_count = 0;

    private static int target_calories_burned, target_calories_eaten, target_pushups;
    private static int completed_pushup,completed_eaten,completed_burned;
    private float target_running;

    private int calories_burned_status = 0,calories_eaten_status = 0,running_status = 0,pushups_status = 0,challenge_status = 0;
    private int overall_status;
    String getCalories_burned,getCalories_eaten,getDistance,getPushups;

    private List<Push_Up> pushup_list = new ArrayList<>();
    private List<Steps> steps_list = new ArrayList<>();
    private List<Food> food_list = new ArrayList<>();
    private List<Calories_Burned_Object> calories_burned_list = new ArrayList<>();

    Push_Up push_up;
    Food food;
    Steps steps;
    Calories_Burned_Object calories_burned_object;

    FirebaseAuth rauth;

    // Database
    DataBaseHelper db;
    private int todays_pushup,PB_pushup,total_calories_Burned,steps_counted;
    private String todaysDate= "",previousDate = "";
    private String caloriesBurnedPreviousDate = "",caloriesEatenPreviousDate = "";
    private SimpleDateFormat dateFormat;

    // for Overall progress Bar
    float overallProgress = 0;
    int overallProgressInt = 0;
    double burned_progress = 0;
    double eaten_progress = 0;
    double running_progress = 0;
    double pushups_progress = 0;

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
        challenge = (ImageView) layout.findViewById(R.id.challenge_btn);

        overall_progressBar = (ProgressBar) layout.findViewById(R.id.overall_progressbar);
        calories_burned_progressBar = (ProgressBar) layout.findViewById(R.id.Calories_burned_progressBar);
        calories_eaten_progressBar = (ProgressBar) layout.findViewById(R.id.calories_eaten_progressBar);
        running_progressBar = (ProgressBar) layout.findViewById(R.id.running_progressBar);
        pushups_porgressBar = (ProgressBar) layout.findViewById(R.id.pushups_progressBar);
        challenge_progressBar = (ProgressBar) layout.findViewById(R.id.challenge_progressBar);

        calories_burned_txt = (TextView) layout.findViewById(R.id.calories_burned_status_text);
        calories_eaten_txt = (TextView) layout.findViewById(R.id.calories_eaten_status_text);
        running_txt = (TextView) layout.findViewById(R.id.running_status_text);
        pushups_txt = (TextView) layout.findViewById(R.id.pushups_status_text);
        challenge_txt = (TextView) layout.findViewById(R.id.challenge_text_view);
        overallTxt = (TextView) layout.findViewById(R.id.overall_txt);


        Drawable calories_burned_drawable =  new ProgressDrawable(0x99FF6700, 0x446B6869);
        Drawable calories_eaten_drawable =  new ProgressDrawable(0x99F23B5F, 0x446B6869);
        Drawable running_drawable =  new ProgressDrawable(0x9910877E, 0x446B6869);
        Drawable pushups_drawable =  new ProgressDrawable(0x990F750F, 0x446B6869);
        Drawable challenge_drawable = new ProgressDrawable(0x990F750F,0x446B6869);

        calories_burned_progressBar.setProgressDrawable(calories_burned_drawable);
        calories_eaten_progressBar.setProgressDrawable(calories_eaten_drawable);
        running_progressBar.setProgressDrawable(running_drawable);
        pushups_porgressBar.setProgressDrawable(pushups_drawable);
        challenge_progressBar.setProgressDrawable(challenge_drawable);


        LoadInfo();


        // Database---------------------------------------------------------------------------------
        db = new DataBaseHelper(getContext());

        if (db.getPushups() != null){
            pushup_list.addAll(db.getPushups());
            get_Date_Pushups();
        }
        if (db.getSteps() != null){
            steps_list.addAll(db.getSteps());

        }
        if (db.getSavedFood() != null){
            food_list.addAll(db.getSavedFood());
            getCalories_EatenPreviousDate();
        }
        if (db.getCalories_Burned() != null){
            calories_burned_list.addAll(db.getCalories_Burned());
            getCalories_BurnedPreviousDate();
        }

        BottomNavigationView bottomNavigationView = layout.findViewById(R.id.bottomNavigationView_dashboard);
        NavController navController = Navigation.findNavController(getActivity(), R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        getTodays_Pushups();
        getTotalcal();
        getCaloriesBurned();
        getSteps();

        getDate();
        // Resetting database if date is changed ---------------------------------------------------
        System.out.println("Todays Date"+todaysDate);
        System.out.println("pushup Date"+previousDate);
        System.out.println("calories burned Date"+caloriesBurnedPreviousDate);
        System.out.println("calories eaten Date"+caloriesEatenPreviousDate);

        resetNewDay();

        // Button click listeners ------------------------------------------------------------------

        push_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Pushup_loadingscreen pushup_frag = new Pushup_loadingscreen();
                fragmentManager.beginTransaction().replace(R.id.fragment,pushup_frag).addToBackStack(null).commit();
            }
        });
        calories_burned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Calories_Burned_loadingscreen calories_burned = new Calories_Burned_loadingscreen();
                fragmentManager.beginTransaction().replace(R.id.fragment,calories_burned).addToBackStack(null).commit();
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
                Calories_EatenLoadingScreen dietPlan_frag = new Calories_EatenLoadingScreen();
                fragmentManager.beginTransaction().replace(R.id.fragment,dietPlan_frag).addToBackStack(null).commit();
            }
        });
        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Challenge_loadingscreen challenge_frag = new Challenge_loadingscreen();
                fragmentManager.beginTransaction().replace(R.id.fragment,challenge_frag).addToBackStack(null).commit();
            }
        });

        return layout;
    }

    public void calories_burned_thread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //calories_burned_txt.setText(target_calories_burned);
                while (total_calories_Burned != target_calories_burned) {

                    if (total_calories_Burned >= target_calories_burned) {
                        calories_burned_status = target_calories_burned;
                    }else {
                        int speed = total_calories_Burned /50;
                        android.os.SystemClock.sleep(50);

                        if (total_calories_Burned != calories_burned_status){
                            calories_burned_status+=speed;
                            if (calories_burned_status>total_calories_Burned){
                                calories_burned_status = total_calories_Burned;
                                System.out.println(speed+"speed");
                                System.out.println(total_calories_Burned);
                                System.out.println(target_calories_burned);
                                System.out.println(calories_burned_status);
                            }
                        }
                    }
                    calories_burned_progressBar.setProgress(calories_burned_status);
                }

            }
        }).start();
    }

    public void calories_eaten_thread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (totalcal != target_calories_eaten) {

                    if (totalcal >= target_calories_eaten) {
                        calories_eaten_status = target_calories_eaten;
                    }else {
                        int speed = target_calories_eaten /50;
                        android.os.SystemClock.sleep(50);

                        if (totalcal != calories_eaten_status){
                            calories_eaten_status+=speed;
                            if (calories_eaten_status>totalcal){
                                calories_eaten_status = totalcal;
                            }
                        }
                    }
                    calories_eaten_progressBar.setProgress(calories_eaten_status);
                }
            }
        }).start();
    }

    public void challenge_thread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (challenge_status != 12){
                    android.os.SystemClock.sleep(50);
                    if (challenges_count != challenge_status){
                        challenge_status+=1;
                    }
                    challenge_progressBar.setProgress(challenge_status);

                }
                if (challenges_count == 12){
                    while (challenge_status != challenges_count){
                        android.os.SystemClock.sleep(50);
                        if (challenges_count != challenge_status){
                            challenge_status+=1;
                        }
                        challenge_progressBar.setProgress(challenge_status);

                    }
                }


            }
        }).start();
    }
    public void Overall_thread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (overall_status != 100){
                    android.os.SystemClock.sleep(50);
                    if (overall_status != overallProgressInt){
                        overall_status+=1;
                    }
                    overall_progressBar.setProgress(overall_status);

                }
            }
        }).start();
    }

    public void running_thread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (steps_counted != target_running) {

                    if (steps_counted >= target_running) {
                        running_status = (int) target_running;
                    }else {
                        float speed = target_running /50;
                        android.os.SystemClock.sleep(50);

                        if (steps_counted != running_status){
                            running_status+=speed;
                            if (running_status>totalcal){
                                running_status = totalcal;
                            }
                        }
                    }
                    running_progressBar.setProgress(running_status);
                }

            }
        }).start();
    }
    public void pushup_thread(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (todays_pushup != target_pushups){
                    if (todays_pushup <50){
                        android.os.SystemClock.sleep(50);
                        if (todays_pushup != pushups_status){
                            pushups_status += 1;
                        }
                    }else {
                        int speed = todays_pushup/50;
                        android.os.SystemClock.sleep(50);
                        if (todays_pushup != pushups_status){
                            pushups_status += speed;
                        }
                    }

                    pushups_porgressBar.setProgress(pushups_status);

                }

            }
        }).start();
    }

    public void LoadInfo(){
        String UserId = rauth.getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(UserId).child("Targets");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                getCalories_burned = String.valueOf(snapshot.child("Goal: Calories Burned").getValue());
                getCalories_eaten = String.valueOf(snapshot.child("Goal: Calories Eaten").getValue());
                getDistance = String.valueOf(snapshot.child("Goal: Running Distance").getValue());
                getPushups = String.valueOf(snapshot.child("Goal: Push-up's").getValue());

                String challenge_getCalories_burned = String.valueOf(snapshot.child("Challenge: Calories Burned").getValue());
                String challenge_getCalories_eaten = String.valueOf(snapshot.child("Challenge: Calories Eaten").getValue());
                String challenge_getPushups = String.valueOf(snapshot.child("Challenge: Push-up's").getValue());


                //System.out.println(Integer.parseInt(getCalories_burned));
                target_calories_burned = Integer.parseInt(getCalories_burned);
                target_calories_eaten  = Integer.parseInt(getCalories_eaten);
                target_running = Float.parseFloat(getDistance);
                target_pushups = Integer.parseInt(getPushups);

                completed_eaten = Integer.parseInt(challenge_getCalories_eaten);
                completed_burned = Integer.parseInt(challenge_getCalories_burned);
                completed_pushup = Integer.parseInt(challenge_getPushups);

                pushups_porgressBar.setMax(target_pushups);
                calories_burned_progressBar.setMax(target_calories_burned);
                calories_eaten_progressBar.setMax(target_calories_eaten);
                running_progressBar.setMax((int) target_running);
                challenge_progressBar.setMax(12);
                overall_progressBar.setMax(100);


                challenges_count = 0;

                if (completed_pushup >= 2000){
                    challenges_count +=4;
                }else if (completed_pushup >= 1000){
                    challenges_count +=3;
                }else if (completed_pushup >= 500){
                    challenges_count +=2;
                }else if (completed_pushup >= 250){
                    challenges_count +=1;
                }else {

                }
                if (completed_burned >= 50000){
                    challenges_count +=4;
                }else if (completed_burned >= 25000){
                    challenges_count +=3;
                }else if (completed_burned >= 10000){
                    challenges_count +=2;
                }else if (completed_burned >= 5000){
                    challenges_count +=1;
                }else {

                }
                if (completed_eaten >= 1000000){
                    challenges_count +=4;
                }else if (completed_eaten >= 50000){
                    challenges_count +=3;
                }else if (completed_eaten >= 20000){
                    challenges_count +=2;
                }else if (completed_eaten >= 10000){
                    challenges_count +=1;
                }else {

                }

                pushups_txt.setText(todays_pushup+"/"+target_pushups);
                calories_burned_txt.setText(total_calories_Burned+"/"+getCalories_burned);
                calories_eaten_txt.setText(totalcal +"/"+getCalories_eaten);
                running_txt.setText(steps_counted+"/"+getDistance);
                challenge_txt.setText(challenges_count+"/"+"12");

                float calories_burned_float = Float.parseFloat(String.valueOf(total_calories_Burned));
                float target_calories_burned_float = Float.parseFloat(String.valueOf(target_calories_burned));

                float calories_eaten_float = Float.parseFloat(String.valueOf(totalcal));
                float target_eaten_burned_float = Float.parseFloat(String.valueOf(target_calories_eaten));

                float running_float = Float.parseFloat(String.valueOf(steps_counted));
                float target_running_float = Float.parseFloat(String.valueOf(target_running));

                float pushup_float = Float.parseFloat(String.valueOf(todays_pushup));
                float target_pushup_burned_float = Float.parseFloat(String.valueOf(target_pushups));

                float burnedfloat = (calories_burned_float/target_calories_burned_float) *100;
                float eatebfloat = (calories_eaten_float/target_eaten_burned_float) *100;
                float runningfloat = (running_float/target_running_float) *100;
                float pushupfloat = (pushup_float/target_pushup_burned_float) *100;

                burned_progress = (burnedfloat *0.25);
                eaten_progress = (eatebfloat *0.25);
                running_progress = (runningfloat *0.25);
                pushups_progress = (pushupfloat *0.25);
                if (burned_progress>25){
                    burned_progress = 25;
                }
                if (eaten_progress>25){
                    eaten_progress = 25;
                }
                if (running_progress>25){
                    running_progress = 25;
                }
                if (pushups_progress>25){
                    pushups_progress = 25;
                }

                overallProgress = (float) (burned_progress+eaten_progress+running_progress+pushups_progress);
                overallProgressInt = (int) overallProgress;

                overallTxt.setText(""+overallProgressInt+"%");

                Overall_thread();
                calories_burned_thread();
                calories_eaten_thread();
                running_thread();
                challenge_thread();
                pushup_thread();

                System.out.println(burned_progress);

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

    public int getSteps(){
        for (int i = 0; i<steps_list.size();i++){
            steps = steps_list.get(i);
            steps_counted += steps.getAmount();
        }
        return steps_counted;
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
            String UserId = rauth.getCurrentUser().getUid();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(UserId).child("Info");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String getheight = String.valueOf(snapshot.child("Height").getValue());
                    String getSleep = String.valueOf(snapshot.child("Sleep").getValue());
                    String age = String.valueOf(snapshot.child("Age").getValue());
                    String currentweight = String.valueOf(snapshot.child("Current Weight").getValue());
                    String gender = String.valueOf(snapshot.child("Gender").getValue());

                    float Floatheight = Float.parseFloat(getheight);
                    float FloatSleep = Float.parseFloat(getSleep);
                    int IntAge = Integer.parseInt(age);
                    int IntWeight = Integer.parseInt(currentweight);

                    double BMR = 0;

                    if (gender.matches("Male")){
                        BMR = 66.47 +(6.24 * IntWeight)+(12.71*Floatheight) - (6.78*IntAge);
                    }else if (gender.matches("Female")){
                        BMR = 665.1 +(4.34 * IntWeight)+(4.7*Floatheight) - (4.68*IntAge);
                    }
                    int BMRint = (int)BMR;
                    double Sleep = ((double)BMRint /24) * FloatSleep * 0.85;
                    int SleepInt = (int)Sleep;

                    db.deleteAllCalories_Burned();
                    db.addCaloriesBurned(new Calories_Burned_Object(-1,"Sleep",SleepInt,todaysDate));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }



    }



}