package com.example.app1;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.app1.FoodDiary.DietPlan_frag;
import com.example.app1.Login_Logout.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Profile_frag extends Fragment {
    private Button Logout;

    private ImageView editProfile,setGoals;

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth rauth;


    TextView name_txt,age_txt,currentweight_txt;
    ImageView UserImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_profile_frag, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        editProfile = (ImageView) layout.findViewById(R.id.profile_editprofile_btn);
        setGoals = (ImageView) layout.findViewById(R.id.setGoals_btn);

        name_txt = (TextView) layout.findViewById(R.id.UserInfo_Name_txt);
        age_txt = (TextView) layout.findViewById(R.id.UserInfo_Age_txt);
        currentweight_txt = (TextView) layout.findViewById(R.id.UserInfo_currentWeight_txt);
        UserImage = (ImageView) layout.findViewById(R.id.Profile_UserImage);


        rauth = FirebaseAuth.getInstance();

        setupFireBaseListner();

        Logout = (Button) layout.findViewById(R.id.buttonLogout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                UserInfo UserInfo = new UserInfo();
                fragmentManager.beginTransaction().replace(R.id.fragment,UserInfo).addToBackStack(null).commit();
            }
        });

        setGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                SetGoal SetGoal = new SetGoal();
                fragmentManager.beginTransaction().replace(R.id.fragment,SetGoal).addToBackStack(null).commit();
            }
        });

        LoadData();

        return layout;
    }

    private void setupFireBaseListner() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                } else {
                    Intent intent = new Intent(getActivity(), Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }
    }
    public void LoadData(){
        String UserId = rauth.getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(UserId).child("Info");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name = String.valueOf(snapshot.child("Name").getValue());
                String age = String.valueOf(snapshot.child("Age").getValue());
                String gender = String.valueOf(snapshot.child("Gender").getValue());
                String currentweight = String.valueOf(snapshot.child("Current Weight").getValue());

                name_txt.setText("Name: "+name);
                age_txt.setText("DOB: "+age);
                currentweight_txt.setText("Current Weight :"+currentweight);

                if (gender == "Male"){
                    UserImage.setBackgroundResource(R.drawable.male_icon);
                }
                if (gender == "Female"){
                    UserImage.setBackgroundResource(R.drawable.female_icon);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}