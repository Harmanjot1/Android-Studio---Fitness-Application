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


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;


public class UserInfo extends Fragment {
    EditText Name,Age,CurrentWeight,TargetWeight;
    Spinner gender;

    FirebaseAuth rauth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    Button Save,Cancel;


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


        Name = (EditText) layout.findViewById(R.id.UserInfo_Name);
        Age = (EditText) layout.findViewById(R.id.UserInfo_Date);
        CurrentWeight = (EditText) layout.findViewById(R.id.UserInfo_CurrentWeight);
        TargetWeight = (EditText) layout.findViewById(R.id.UserInfo_TargetWeight);

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
                DatabaseReference current_User_db = FirebaseDatabase.getInstance().getReference().child("User").child(UserId);

                String nameString = Name.getText().toString();
                String AgeString = Age.getText().toString();
                String GenderString = gender.getSelectedItem().toString();
                String CurrentWeight_string = CurrentWeight.getText().toString();
                String TargetWeight_String = TargetWeight.getText().toString();

                //SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                if (nameString.isEmpty()){
                    Name.setError("Please enter your Name");
                }
                if (AgeString.isEmpty()){
                    Age.setError("Please enter your age");
                }

                if (AgeString!= null && !AgeString.trim().isEmpty()){
                    if (nameString != null && !nameString.trim().isEmpty()){
                        Map newPost = new HashMap<>();

                        newPost.put("Name",nameString);
                        newPost.put("Age",AgeString);
                        newPost.put("Gender",GenderString);
                        newPost.put("Current Weight",CurrentWeight_string);
                        newPost.put("Target Weight",TargetWeight_String);

                        current_User_db.setValue(newPost);
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
    public void loadInfo(){
        String UserId = rauth.getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(UserId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name = String.valueOf(snapshot.child("Name").getValue());
                String age = String.valueOf(snapshot.child("Age").getValue());
                String gender = String.valueOf(snapshot.child("Gender").getValue());
                String currentweight = String.valueOf(snapshot.child("Current Weight").getValue());
                String targetweight = String.valueOf(snapshot.child("Target Weight").getValue());

                Name.setText(name);
                Age.setText(age);
                CurrentWeight.setText(currentweight);
                TargetWeight.setText(targetweight);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}