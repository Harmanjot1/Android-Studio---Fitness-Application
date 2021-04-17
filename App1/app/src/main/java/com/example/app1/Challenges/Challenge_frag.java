package com.example.app1.Challenges;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.app1.Activity;
import com.example.app1.Calories_Burned.Calories_Burned_Adapter;
import com.example.app1.Calories_Burned.Calories_Burned_List;
import com.example.app1.Calories_Burned.Calories_Burned_Object;
import com.example.app1.Calories_Burned.NewCaloriesBurned_Form;
import com.example.app1.Dashboard_frag;
import com.example.app1.Database.DataBaseHelper;
import com.example.app1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Challenge_frag extends Fragment {


    TextView push250,push500,push1000,push2000;
    TextView eaten10000,eaten20000,eaten50000,eaten100000;
    TextView burned5000,burned10000,burned25000,burned50000;

    // ImageViews for completed badges
    ImageView push250_completed,push500_completed,push1000_completed,push2000_completed;
    ImageView eaten10000_completed,eaten20000_completed,eaten50000_completed,eaten100000_completed;
    ImageView burned5000_completed,burned10000_completed,burned25000_completed,burned50000_completed;

    private static int target_calories_burned = 0, target_calories_eaten = 0, target_pushups = 0;
    FirebaseAuth rauth;

    Button backBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container.removeAllViews();
        final View layout = inflater.inflate(R.layout.fragment_challenge_frag, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        // Initiate all the textviews
        // For pushups
        push250 = (TextView) layout.findViewById(R.id.pushup_250);
        push500 = (TextView) layout.findViewById(R.id.pushup_500);
        push1000 = (TextView) layout.findViewById(R.id.pushup_1000);
        push2000 = (TextView) layout.findViewById(R.id.pushup_2000);

        push250_completed = (ImageView) layout.findViewById(R.id.pushup_250_completed);
        push500_completed = (ImageView) layout.findViewById(R.id.pushup_500_completed);
        push1000_completed = (ImageView) layout.findViewById(R.id.pushup_1000_completed);
        push2000_completed = (ImageView) layout.findViewById(R.id.pushup_2000_completed);

        // For consumed calories
        eaten10000 = (TextView) layout.findViewById(R.id.calories_consumed10000);
        eaten20000 = (TextView) layout.findViewById(R.id.calories_consumed20000);
        eaten50000 = (TextView) layout.findViewById(R.id.calories_consumed50000);
        eaten100000 = (TextView) layout.findViewById(R.id.calories_consumed100000);

        eaten10000_completed = (ImageView) layout.findViewById(R.id.calories_consumed10000_completed);
        eaten20000_completed = (ImageView) layout.findViewById(R.id.calories_consumed20000_completed);
        eaten50000_completed = (ImageView) layout.findViewById(R.id.calories_consumed50000_completed);
        eaten100000_completed = (ImageView) layout.findViewById(R.id.calories_consumed100000_completed);

        // for calories burned
        burned5000 = (TextView) layout.findViewById(R.id.calories_burned5000);
        burned10000 = (TextView) layout.findViewById(R.id.calories_burned10000);
        burned25000 = (TextView) layout.findViewById(R.id.calories_burned25000);
        burned50000 = (TextView) layout.findViewById(R.id.calories_burned50000);

        burned5000_completed = (ImageView) layout.findViewById(R.id.calories_burned5000_completed);
        burned10000_completed = (ImageView) layout.findViewById(R.id.calories_burned10000_completed);
        burned25000_completed = (ImageView) layout.findViewById(R.id.calories_burned25000_completed);
        burned50000_completed = (ImageView) layout.findViewById(R.id.calories_burned50000_completed);

        backBtn = (Button) layout.findViewById(R.id.challenge_btnBack);
        // getting firebase instance
        rauth = FirebaseAuth.getInstance();

        LoadInfo();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Dashboard_frag dashboard_frag = new Dashboard_frag();
                fragmentManager.beginTransaction().replace(R.id.fragment,dashboard_frag).addToBackStack(null).commit();
            }
        });
        return layout;
    }

    public void LoadInfo(){
        String UserId = rauth.getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(UserId).child("Targets");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String challenge_getCalories_burned = String.valueOf(snapshot.child("Challenge: Calories Burned").getValue());
                String challenge_getCalories_eaten = String.valueOf(snapshot.child("Challenge: Calories Eaten").getValue());
                String challenge_getDistance = String.valueOf(snapshot.child("Challenge: Running Distance").getValue());
                String challenge_getPushups = String.valueOf(snapshot.child("Challenge: Push-up's").getValue());


                int pushup = Integer.parseInt(challenge_getPushups);
                int caloriesEaten = Integer.parseInt(challenge_getCalories_eaten);
                int caloriesBurned = Integer.parseInt(challenge_getCalories_burned);

                push250.setText(pushup+"/"+"250");
                push500.setText(pushup+"/"+"500");
                push1000.setText(pushup+"/"+"1,000");
                push2000.setText(pushup+"/"+"2,000");

                eaten10000.setText(caloriesEaten+"/"+"10,000");
                eaten20000.setText(caloriesEaten+"/"+"20,000");
                eaten50000.setText(caloriesEaten+"/"+"50,000");
                eaten100000.setText(caloriesEaten+"/"+"100,000");

                burned5000.setText(caloriesBurned+"/"+"5,000");
                burned10000.setText(caloriesBurned+"/"+"10,000");
                burned25000.setText(caloriesBurned+"/"+"25,000");
                burned50000.setText(caloriesBurned+"/"+"50,000");

                if (pushup >=2000){
                    push250.setVisibility(View.INVISIBLE);
                    push250_completed.setVisibility(View.VISIBLE);

                    push500.setVisibility(View.INVISIBLE);
                    push500_completed.setVisibility(View.VISIBLE);

                    push1000.setVisibility(View.INVISIBLE);
                    push1000_completed.setVisibility(View.VISIBLE);

                    push2000.setVisibility(View.INVISIBLE);
                    push2000_completed.setVisibility(View.VISIBLE);
                }else if (pushup >= 1000){
                    push250.setVisibility(View.INVISIBLE);
                    push250_completed.setVisibility(View.VISIBLE);

                    push500.setVisibility(View.INVISIBLE);
                    push500_completed.setVisibility(View.VISIBLE);

                    push1000.setVisibility(View.INVISIBLE);
                    push1000_completed.setVisibility(View.VISIBLE);
                }else if (pushup>=500){
                    push250.setVisibility(View.INVISIBLE);
                    push250_completed.setVisibility(View.VISIBLE);

                    push500.setVisibility(View.INVISIBLE);
                    push500_completed.setVisibility(View.VISIBLE);
                }else if (pushup >=250){
                    push250.setVisibility(View.INVISIBLE);
                    push250_completed.setVisibility(View.VISIBLE);
                }

                if (caloriesEaten >= 50000){
                    eaten10000.setVisibility(View.INVISIBLE);
                    eaten10000_completed.setVisibility(View.VISIBLE);

                    eaten20000.setVisibility(View.INVISIBLE);
                    eaten20000_completed.setVisibility(View.VISIBLE);

                    eaten50000.setVisibility(View.INVISIBLE);
                    eaten50000_completed.setVisibility(View.VISIBLE);

                    eaten100000.setVisibility(View.INVISIBLE);
                    eaten100000_completed.setVisibility(View.VISIBLE);

                }else if (caloriesEaten >= 25000){
                    eaten10000.setVisibility(View.INVISIBLE);
                    eaten10000_completed.setVisibility(View.VISIBLE);

                    eaten20000.setVisibility(View.INVISIBLE);
                    eaten20000_completed.setVisibility(View.VISIBLE);

                    eaten50000.setVisibility(View.INVISIBLE);
                    eaten50000_completed.setVisibility(View.VISIBLE);
                }else if (caloriesEaten >= 10000){
                    eaten10000.setVisibility(View.INVISIBLE);
                    eaten10000_completed.setVisibility(View.VISIBLE);

                    eaten20000.setVisibility(View.INVISIBLE);
                    eaten20000_completed.setVisibility(View.VISIBLE);
                }else if (caloriesEaten >= 5000){
                    eaten10000.setVisibility(View.INVISIBLE);
                    eaten10000_completed.setVisibility(View.VISIBLE);
                }

                if (caloriesBurned >= 100000){
                    burned5000.setVisibility(View.INVISIBLE);
                    burned5000_completed.setVisibility(View.VISIBLE);

                    burned10000.setVisibility(View.INVISIBLE);
                    burned10000_completed.setVisibility(View.VISIBLE);

                    burned25000.setVisibility(View.INVISIBLE);
                    burned25000_completed.setVisibility(View.VISIBLE);

                    burned50000.setVisibility(View.INVISIBLE);
                    burned50000_completed.setVisibility(View.VISIBLE);
                }else if (caloriesBurned >= 50000){
                    burned5000.setVisibility(View.INVISIBLE);
                    burned5000_completed.setVisibility(View.VISIBLE);

                    burned10000.setVisibility(View.INVISIBLE);
                    burned10000_completed.setVisibility(View.VISIBLE);

                    burned25000.setVisibility(View.INVISIBLE);
                    burned25000_completed.setVisibility(View.VISIBLE);
                }else if (caloriesBurned >= 20000){
                    burned5000.setVisibility(View.INVISIBLE);
                    burned5000_completed.setVisibility(View.VISIBLE);

                    burned10000.setVisibility(View.INVISIBLE);
                    burned10000_completed.setVisibility(View.VISIBLE);
                }else if (caloriesBurned >= 10000){
                    burned5000.setVisibility(View.INVISIBLE);
                    burned5000_completed.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}