package com.example.app1;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;
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


    TextView name_txt,age_txt,currentweight_txt,bmr;
    ImageView UserImage;

    private float height;

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
        bmr = (TextView) layout.findViewById(R.id.bmr);


        rauth = FirebaseAuth.getInstance();

        setupFireBaseListner();

        BottomNavigationView bottomNavigationView = layout.findViewById(R.id.bottomNavigationView_profile);
        NavController navController = Navigation.findNavController(getActivity(), R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

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
                String targetweight = String.valueOf(snapshot.child("Target Weight").getValue());
                String getheight = String.valueOf(snapshot.child("Height").getValue());
                String getSleep = String.valueOf(snapshot.child("Sleep").getValue());
                String getActive = String.valueOf(snapshot.child("Active").getValue());

                name_txt.setText("Name: "+name);
                age_txt.setText("Age: "+age);
                currentweight_txt.setText("Current Weight :"+currentweight);

                if (gender == "Male"){
                    UserImage.setBackgroundResource(R.drawable.male_icon);
                }
                if (gender == "Female"){
                    UserImage.setBackgroundResource(R.drawable.female_icon);
                }
                float Floatheight = Float.parseFloat(getheight);
                int IntAge = Integer.parseInt(age);
                int IntWeight = Integer.parseInt(currentweight);

                double BMR = 0;

                if (gender.matches("Male")){
                    BMR = 66.47 +(6.24 * IntWeight)+(12.71*Floatheight) - (6.78*IntAge);
                }else if (gender.matches("Female")){
                    BMR = 665.1 +(4.34 * IntWeight)+(4.7*Floatheight) - (4.68*IntAge);
                }
                int BMRint = (int)BMR;
                int currentWeight = Integer.parseInt(currentweight);
                int targetWeight = Integer.parseInt(targetweight);
                int weight_difference = 0;
                if (currentWeight<targetWeight){
                    weight_difference=targetWeight - currentWeight;
                }else if(currentWeight>targetWeight){
                    weight_difference = currentWeight-targetWeight;
                }else if (currentWeight == targetWeight){
                    weight_difference = 0;
                }

                double BMTdouble = (double) BMRint;

                double activityFloat = 0;
                if (getActive.equals("Sedentary")){
                    activityFloat = 1.2;
                }else if (getActive.equals("Lightly active")) {
                    activityFloat = 1.375;
                }else if (getActive.equals("Moderately active")) {
                    activityFloat = 1.55;
                }else if (getActive.equals("Very active")) {
                    activityFloat = 1.725;
                }else if (getActive.equals("Extra active")) {
                    activityFloat = 1.9;
                }

                double totalBMR = BMTdouble*activityFloat;
                double calories_to_eat = (weight_difference / 2) *250;

                int total_to_eat = (int)totalBMR + (int)calories_to_eat;

                if (currentWeight<targetWeight){
                    bmr.setText("BMR: "+totalBMR+"\nTarget Weight: "+targetweight+"\n\nTo Achieve your target weight in 2 months, you must consume "+calories_to_eat+" more calories each day.\n\nThis brings your total calories to eat each day to:"+total_to_eat);
                }else if(currentWeight>targetWeight){
                    bmr.setText("BMR: "+totalBMR+"\nTarget Weight: "+targetweight+"\n\nTo Achieve your target weight in 2 months, you must consume "+calories_to_eat+" less calories each day.\n\nThis brings your total calories to eat each day to:"+total_to_eat);

                }else if (currentWeight == targetWeight){
                    bmr.setText("BMR: "+BMRint+"\nTarget Weight: "+targetweight+"\n\nTo maintain your in next 2 months, you must consume "+calories_to_eat+" more calories each day.\n\nThis brings your total calories to eat each day to:"+total_to_eat);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}