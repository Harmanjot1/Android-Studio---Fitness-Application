package com.example.app1;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;


public class Dashboard_frag extends Fragment {

    ImageView push_up_btn, step_detector, stopwatch, diet;

    TextView calories_burned_txt,calories_eaten_txt,running_txt,pushups_txt;

    DataBaseHelper db;

    private int todays_pushup;

    private ProgressBar calories_burned_progressBar, calories_eaten_progressBar, running_progressBar, pushups_porgressBar;
    private int pushup_progress_process = 0;
    private int calories_progress_process = 0;
    private int steps_progress_process = 0;
    private int totalcal = 0;

    private static int target_calories_burned, target_calories_eaten, target_pushups;
    private float target_running;

    private int calories_burned_status = 0,calories_eaten_status = 0,running_status = 0,pushups_status = 0;

    private Handler handler = new Handler();

    private List<Push_Up> pushup_list = new ArrayList<>();
    private List<Food> food_list = new ArrayList<>();

    Push_Up push_up;
    Food food;

    FirebaseAuth rauth;

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

        calories_burned_progressBar = (ProgressBar) layout.findViewById(R.id.Calories_burned_progressBar);
        calories_eaten_progressBar = (ProgressBar) layout.findViewById(R.id.calories_eaten_progressBar);
        running_progressBar = (ProgressBar) layout.findViewById(R.id.running_progressBar);
        pushups_porgressBar = (ProgressBar) layout.findViewById(R.id.pushups_progressBar);

        calories_burned_txt = (TextView) layout.findViewById(R.id.calories_burned_status_text);

        Drawable calories_burned_drawable =  new ProgressDrawable(0x99FF6700, 0x446B6869);
        Drawable calories_eaten_drawable =  new ProgressDrawable(0x99F23B5F, 0x446B6869);
        Drawable running_drawable =  new ProgressDrawable(0x9910877E, 0x446B6869);
        Drawable pushups_drawable =  new ProgressDrawable(0x990F750F, 0x446B6869);

        calories_burned_progressBar.setProgressDrawable(calories_burned_drawable);
        calories_eaten_progressBar.setProgressDrawable(calories_eaten_drawable);
        running_progressBar.setProgressDrawable(running_drawable);
        pushups_porgressBar.setProgressDrawable(pushups_drawable);

        LoadInfo();

        System.out.println(target_calories_burned);

       // pushup_progressBar = (ProgressBar) layout.findViewById(R.id.pushup_progressBar);

        //pushup_txt = (TextView) layout.findViewById(R.id.pushup_progress_txt);

        db = new DataBaseHelper(getContext());

        if (db.getPushups() != null){
            pushup_list.addAll(db.getPushups());
        }
        if (db.getSavedFood() != null){
            food_list.addAll(db.getSavedFood());
        }
        getTodays_Pushups();
        getTotalcal();
        // Start long running operation in a background thread

        push_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Pushup_frag pushup_frag = new Pushup_frag();
                fragmentManager.beginTransaction().replace(R.id.fragment,pushup_frag).commit();
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

    public int getTodays_Pushups(){
        for (int i=0; i<pushup_list.size();i++){
            push_up = pushup_list.get(i);
            todays_pushup = push_up.getTodays_Pushups();
        }
        return todays_pushup;
    }
    public int getTotalcal() {
        totalcal = 0;
        for (int i = 0; i < food_list.size(); i++) {

            food = food_list.get(i);
            totalcal += food.getCalories();
        }
        return totalcal;
    }

    public void calories_burned_thread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //calories_burned_txt.setText(target_calories_burned);
                while (calories_burned_status < target_calories_burned){
                    calories_burned_status+= 100;
                    android.os.SystemClock.sleep(50);
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
                while (calories_eaten_status < target_calories_eaten){
                    calories_eaten_status++;
                    android.os.SystemClock.sleep(50);
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
                while (running_status < target_running){
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
                while (pushups_status < target_pushups){
                    pushups_status++;
                    android.os.SystemClock.sleep(50);
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
                String calories_burned = String.valueOf(snapshot.child("Calories Burned").getValue());
                String calories_eaten = String.valueOf(snapshot.child("Calories Eaten").getValue());
                String distance = String.valueOf(snapshot.child("Running Distance").getValue());
                String pushups = String.valueOf(snapshot.child("Push-up's").getValue());

                System.out.println(Integer.parseInt(calories_burned));
                target_calories_burned = Integer.parseInt(calories_burned);
                target_calories_eaten  = Integer.parseInt(calories_eaten);
                target_running = Float.parseFloat(distance);
                target_pushups = Integer.parseInt(pushups);

                calories_burned_thread();
                calories_eaten_thread();
                running_thread();
                pushup_thread();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}