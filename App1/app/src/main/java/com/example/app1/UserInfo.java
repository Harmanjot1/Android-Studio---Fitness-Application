package com.example.app1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.app1.Calories_Burned.Calories_Burned_Object;
import com.example.app1.Database.DataBaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;


public class UserInfo extends Fragment {
    EditText Name,Age,CurrentWeight,TargetWeight,height;
    Spinner gender;

    FirebaseAuth rauth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    Button Save,Cancel;

    DataBaseHelper db;
    Calories_Burned_Object calories_burned_object;

    private List<Calories_Burned_Object> calories_burned_list = new ArrayList<>();

    private SimpleDateFormat dateFormat;
    private String todaysDate= "",previousDate = "";

    String[] Gender = {"Male","Female"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_user_info, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        rauth = FirebaseAuth.getInstance();

        db = new DataBaseHelper(getContext());

        Name = (EditText) layout.findViewById(R.id.UserInfo_Name);
        Age = (EditText) layout.findViewById(R.id.UserInfo_Date);
        CurrentWeight = (EditText) layout.findViewById(R.id.UserInfo_CurrentWeight);
        TargetWeight = (EditText) layout.findViewById(R.id.UserInfo_TargetWeight);
        height = (EditText) layout.findViewById(R.id.UserInfo_height);

        gender = (Spinner) layout.findViewById(R.id.UserInfo_Spinner);

        Save = (Button) layout.findViewById(R.id.UserInfo_btnSave);
        Cancel = (Button) layout.findViewById(R.id.UserInfo_btnCancel);

        ArrayAdapter ad
                = new ArrayAdapter(
                getContext(),
                android.R.layout.simple_spinner_item,
                Gender);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        gender.setAdapter(ad);
        loadInfo();

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String UserId = rauth.getCurrentUser().getUid();
                DatabaseReference current_User_db = FirebaseDatabase.getInstance().getReference().child("User").child(UserId).child("Info");

                String nameString = Name.getText().toString();
                String AgeString = Age.getText().toString();
                String GenderString = gender.getSelectedItem().toString();
                String CurrentWeight_string = CurrentWeight.getText().toString();
                String TargetWeight_String = TargetWeight.getText().toString();
                String Height_String = height.getText().toString();

                //SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                if (nameString.isEmpty()){
                    Name.setError("Please enter your Name");
                }
                if (AgeString.isEmpty()){
                    Age.setError("Please enter your age");
                }
                if (CurrentWeight_string.isEmpty()){
                    CurrentWeight.setError("Please enter your current weight in kg");
                }
                if (TargetWeight_String.isEmpty()){
                    TargetWeight.setError("Please enter your target weight in kg");
                }
                if (Height_String.isEmpty()){
                    height.setError("Please enter your height in inches");
                }


                if (AgeString!= null && !AgeString.trim().isEmpty()){
                    if (nameString != null && !nameString.trim().isEmpty()){
                        Map newPost = new HashMap<>();

                        newPost.put("Name",nameString);
                        newPost.put("Age",AgeString);
                        newPost.put("Gender",GenderString);
                        newPost.put("Current Weight",CurrentWeight_string);
                        newPost.put("Target Weight",TargetWeight_String);
                        newPost.put(("Height"),Height_String);

                        current_User_db.setValue(newPost);
                        ArrayList<String> arrayList = new ArrayList<>();
                        if (db.getCalories_Burned() != null){
                            calories_burned_list.addAll(db.getCalories_Burned());


                            for (int i = 0; i<calories_burned_list.size();i++){
                                calories_burned_object = calories_burned_list.get(i);

                                System.out.println(calories_burned_list+"kjadsfhgkjsabhfkjdsfbkjasdkjafghiukjabg");

                                String reason  = calories_burned_object.getReason();
                                System.out.println(reason);

                                arrayList.add(reason);

                            }

                        }

                        getDate();
                        if (arrayList.contains("BMR")){

                        }else {
                            float Floatheight = Float.parseFloat(Height_String);
                            int IntAge = Integer.parseInt(AgeString);
                            int IntWeight = Integer.parseInt(CurrentWeight_string);

                            double BMR = 0;

                            if (GenderString.matches("Male")){
                                BMR = 66.47 +(6.24 * IntWeight)+(12.71*Floatheight) - (6.78*IntAge);
                            }else if (GenderString.matches("Female")){
                                BMR = 665.1 +(4.34 * IntWeight)+(4.7*Floatheight) - (4.68*IntAge);
                            }
                            int BMRint = (int)BMR;
                            db.addCaloriesBurned(new Calories_Burned_Object(-1,"BMR",BMRint,todaysDate));
                        }

                        FragmentManager fragmentManager = getFragmentManager();
                        Profile_frag Profile_frag = new Profile_frag();
                        fragmentManager.beginTransaction().replace(R.id.fragment,Profile_frag).addToBackStack(null).commit();

                    }
                }

            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Profile_frag Profile_frag = new Profile_frag();
                fragmentManager.beginTransaction().replace(R.id.fragment,Profile_frag).addToBackStack(null).commit();
            }
        });


        return layout;
    }

    public void getDate() {
        Calendar calendar;

        calendar = Calendar.getInstance();

        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        todaysDate = dateFormat.format(calendar.getTime());

    }

    public void loadInfo(){
        String UserId = rauth.getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(UserId).child("Info");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name = String.valueOf(snapshot.child("Name").getValue());
                String age = String.valueOf(snapshot.child("Age").getValue());
                String gender = String.valueOf(snapshot.child("Gender").getValue());
                String currentweight = String.valueOf(snapshot.child("Current Weight").getValue());
                String targetweight = String.valueOf(snapshot.child("Target Weight").getValue());
                String getheight = String.valueOf(snapshot.child("Height").getValue());

                if (name.matches("null")){
                    Name.setHint("Name");
                }else {
                    Name.setHint(name+"");
                }
                if (age.matches("null")){
                    Age.setHint("Age");
                }else {
                    Age.setHint(age+"");
                }
                if (currentweight.matches("null")){
                    CurrentWeight.setHint("Current weight");
                }else {
                    CurrentWeight.setHint(currentweight+"");
                }
                if (targetweight.matches("null")){
                    TargetWeight.setHint("Target Weight");
                }else {
                    TargetWeight.setHint(targetweight+"");
                }
                if (getheight.matches("null")){
                    height.setHint("Height (inches)");
                }else {
                    height.setHint(getheight);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}