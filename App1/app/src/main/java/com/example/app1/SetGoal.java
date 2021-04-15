package com.example.app1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class SetGoal extends Fragment {
    EditText Calories_Burned,Calories_Eaten,Distance,Push_ups;
    Spinner gender;
    Button cancel,save;

    boolean saved = false;
    FirebaseAuth rauth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_set_goal, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        rauth = FirebaseAuth.getInstance();

        Calories_Burned = (EditText) layout.findViewById(R.id.Caloried_burned_editText);
        Calories_Eaten = (EditText) layout.findViewById(R.id.Caloried_Eaten_edit_text);
        Distance = (EditText) layout.findViewById(R.id.Running_edit_text);
        Push_ups = (EditText) layout.findViewById(R.id.push_ups_edit_text);

        cancel = (Button) layout.findViewById(R.id.Calories_Burned_add_btnCancel);
        save = (Button) layout.findViewById(R.id.Calories_burned_add_btnSave);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Profile_frag Profile_frag = new Profile_frag();
                fragmentManager.beginTransaction().replace(R.id.fragment,Profile_frag).addToBackStack(null).commit();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserId = rauth.getCurrentUser().getUid();
                final DatabaseReference current_User_db = FirebaseDatabase.getInstance().getReference().child("User").child(UserId).child("Targets");


                current_User_db.addListenerForSingleValueEvent(new ValueEventListener() {
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


                        int calories_burned = 0;
                        int calories_eaten = 0;
                        float distance = 0;
                        int pushups = 0;

                        if (TextUtils.isEmpty(Calories_Burned.getText().toString())){
                            Calories_Burned.setError("Set Goal for calories burned");
                        }else {
                            calories_burned = Integer.parseInt(Calories_Burned.getText().toString());
                        }
                        if (TextUtils.isEmpty(Calories_Eaten.getText().toString())){
                            Calories_Eaten.setError("Set Goal for calories burned");
                        }else {
                            calories_eaten = Integer.parseInt(Calories_Eaten.getText().toString());
                        }
                        if (TextUtils.isEmpty(Distance.getText().toString())){
                            Distance.setError("Set Goal for calories burned");
                        }else {
                            distance = Float.valueOf(Distance.getText().toString());
                        }
                        if (TextUtils.isEmpty(Push_ups.getText().toString())){
                            Push_ups.setError("Set Goal for calories burned");
                        }else {
                            pushups = Integer.parseInt(Push_ups.getText().toString());
                        }

                        if (calories_burned !=0){
                            if (calories_eaten != 0){
                                if (distance != 0){
                                    if (pushups != 0)
                                    {
                                        Map newPost = new HashMap<>();

                                        newPost.put("Challenge: Calories Burned",challenge_getCalories_burned);
                                        newPost.put("Challenge: Calories Eaten",challenge_getCalories_eaten);
                                        newPost.put("Challenge: Running Distance",challenge_getDistance);
                                        newPost.put("Challenge: Push-up's", challenge_getPushups);

                                        newPost.put("Goal: Calories Burned",calories_burned);
                                        newPost.put("Goal: Calories Eaten",calories_eaten);
                                        newPost.put("Goal: Running Distance",distance);
                                        newPost.put("Goal: Push-up's", pushups);

                                        current_User_db.setValue(newPost);
                                    }
                                }
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        LoadInfo();

        return layout;
    }

    public void LoadInfo(){
        String UserId = rauth.getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(UserId).child("Targets");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String caloried_burned = String.valueOf(snapshot.child("Calories Burned").getValue());
                String calories_eaten = String.valueOf(snapshot.child("Calories Eaten").getValue());
                String distance = String.valueOf(snapshot.child("Running Distance").getValue());
                String pushups = String.valueOf(snapshot.child("Push-up's").getValue());

                Calories_Burned.setHint(caloried_burned);
                Calories_Eaten.setHint(calories_eaten);
                Distance.setHint(distance);
                Push_ups.setHint(pushups);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}