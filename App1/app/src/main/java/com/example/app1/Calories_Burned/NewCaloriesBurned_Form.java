package com.example.app1.Calories_Burned;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.app1.Database.DataBaseHelper;
import com.example.app1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewCaloriesBurned_Form extends Fragment {

    Button cancel,save;
    EditText reason, amount;

    ImageView help;
    //Database
    DataBaseHelper db;
    //Firebase
    FirebaseAuth rauth;


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
        final View layout = inflater.inflate(R.layout.fragment_new_calories_burned__form, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        // Get firebase instance
        rauth = FirebaseAuth.getInstance();

        reason = (EditText) layout.findViewById(R.id.add_calories_reason);
        amount = (EditText) layout.findViewById(R.id.add_Calories_amount);

        save = (Button) layout.findViewById(R.id.Calories_burned_add_btnSave);
        cancel = (Button) layout.findViewById(R.id.Calories_Burned_add_btnCancel);
        help = (ImageView) layout.findViewById(R.id.Calories_Burned_Form_help);

        db = new DataBaseHelper(getContext());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDate();

                String Stringreason = reason.getText().toString();
                if (Stringreason.isEmpty() && amount != null){
                    Toast.makeText(getContext(), "Please enter the Reason and amount burned",
                            Toast.LENGTH_LONG).show();
                }else {
                    db.addCaloriesBurned(new Calories_Burned_Object(-1,Stringreason,Integer.parseInt(String.valueOf(amount.getText())),todaysDate));

                    // Firebase Update Code
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
                            int Intchallenge_getCalories_burned = Integer.parseInt(challenge_getCalories_burned);
                            int newTotal = Intchallenge_getCalories_burned + Integer.parseInt(String.valueOf(amount.getText()));

                            // Create HashMap
                            Map targetMap = new HashMap<>();

                            targetMap.put("Goal: Calories Burned",getCalories_burned);
                            targetMap.put("Goal: Calories Eaten",getCalories_eaten);
                            targetMap.put("Goal: Running Distance",getDistance);
                            targetMap.put("Goal: Push-up's",getPushups);

                            targetMap.put("Challenge: Calories Burned",newTotal);
                            targetMap.put("Challenge: Calories Eaten",challenge_getCalories_eaten);
                            targetMap.put("Challenge: Running Distance",challenge_getDistance);
                            targetMap.put("Challenge: Push-up's",challenge_getPushups);

                            databaseReference.setValue(targetMap);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                // Get String

                // put string into message for Calories Burned

                // Start calories_burned

                FragmentManager fragmentManager = getFragmentManager();
                Calories_Burned newCaloriesBurned = new Calories_Burned();
                fragmentManager.beginTransaction().replace(R.id.fragment,newCaloriesBurned).addToBackStack(null).commit();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Calories_Burned newCaloriesBurned = new Calories_Burned();
                fragmentManager.beginTransaction().replace(R.id.fragment,newCaloriesBurned).addToBackStack(null).commit();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Help");
                alert.setMessage("Please enter reason for calories burned in 'Activity' and number of calories burned in 'Calories Burned'");
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