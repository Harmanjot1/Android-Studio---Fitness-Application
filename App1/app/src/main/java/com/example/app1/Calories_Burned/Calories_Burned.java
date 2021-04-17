package com.example.app1.Calories_Burned;

import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.Toast;

import com.example.app1.Dashboard_frag;
import com.example.app1.Database.DataBaseHelper;

import com.example.app1.Activity;
import com.example.app1.LoadingScreens.Main_loadingScreen;
import com.example.app1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Calories_Burned extends Fragment {

    ListView Calories_Burned_ListView;

    Button btn_Add, btn_back,btn_edit,btn_done;
    ImageView help;

    Calories_Burned_Adapter adapter;
    Calories_Burned_List calories_burned_list;

    TextView totalBurned,header;

    DataBaseHelper dataBaseHelper = null;

    private boolean removeCalories = false;
    //Firebase
    FirebaseAuth rauth;
    int removeBurnedCalories; // new altered calories

    private static final int LONG_DELAY = 5000; // 3.5 seconds
    private static final int SHORT_DELAY = 2000; // 2 seconds

    private Toast mToastToShow;

    float height;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container.removeAllViews();
        final View layout = inflater.inflate(R.layout.fragment_calories__burned, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        // Get firebase instance
        rauth = FirebaseAuth.getInstance();

        // Getting button listeners
        btn_Add = (Button) layout.findViewById(R.id.Calories_burned_Add);
        btn_back = (Button) layout.findViewById(R.id.challenge_btnBack);
        btn_edit = (Button) layout.findViewById(R.id.calories_burned_editbtn);
        btn_done = (Button) layout.findViewById(R.id.calories_burned_donebtn);
        // Help ImageView
        help = (ImageView) layout.findViewById(R.id.CaloriesBurned_help);

        totalBurned = (TextView) layout.findViewById(R.id.calories_burned_totalcount_Text);
        header = (TextView) layout.findViewById(R.id.calories_burned_Header);
        // Getting ListView Listener
        Calories_Burned_ListView = (ListView) layout.findViewById(R.id.calories_burned_ListView);

        calories_burned_list = ((Activity)getActivity().getApplication()).getCalories_burned_list();

        adapter = new Calories_Burned_Adapter(getActivity(),calories_burned_list);

        // getting database
        dataBaseHelper = new DataBaseHelper(getContext());
        loadCalories();

        btnMetod();
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Help");
                alert.setMessage("Add calories burned by pressing add button.\n\nRemove added Activities by press edit button.\n\nBRM = base metabolic rate of calories burned calculated from your personal data.\n\nRe-evaluate your BMR or add BMR by entering your information in EditProfile on profile Page.");
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



    public void loadCalories(){
        if (dataBaseHelper.getCalories_Burned() != null){
            calories_burned_list.getMyCalories_Burned_List().clear();
            calories_burned_list.getMyCalories_Burned_List().addAll(dataBaseHelper.getCalories_Burned());

            totalBurned.setText("Total Burned: "+calories_burned_list.getTotal() +"");
            showAdapter();
            adapter.notifyDataSetChanged();
        }
    }

    public void btnMetod(){
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                NewCaloriesBurned_Form newCaloriesBurned_form = new NewCaloriesBurned_Form();
                fragmentManager.beginTransaction().replace(R.id.fragment,newCaloriesBurned_form).addToBackStack(null).commit();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Main_loadingScreen dashboard_frag = new Main_loadingScreen();
                fragmentManager.beginTransaction().replace(R.id.fragment,dashboard_frag).addToBackStack(null).commit();
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCalories = true;
                btn_done.setVisibility(View.VISIBLE);
                header.setText("Tap on activity to Remove");
                removeCalories();
            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calories_Burned_ListView.setOnItemClickListener(null);
                btn_done.setVisibility(View.GONE);
                header.setText("Calories Burned");
            }
        });
    }

    public void showAdapter() {
        adapter = new Calories_Burned_Adapter(getActivity(),calories_burned_list);
        Calories_Burned_ListView.setAdapter(adapter);
    }

    public void removeCalories(){
        if (removeCalories == true){
            Calories_Burned_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Calories_Burned_Object clickeditem = (Calories_Burned_Object) parent.getItemAtPosition(position);

                    removeBurnedCalories = clickeditem.getAmount_burned();

                    dataBaseHelper.deleteCalories_Burned(clickeditem);

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
                            int challenge_removeFood = Integer.parseInt(challenge_getCalories_burned);
                            int newTotal = challenge_removeFood - removeBurnedCalories;

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

                    loadCalories();
                    showAdapter();
                    totalBurned.setText("Total Burned: "+calories_burned_list.getTotal() +"");
                }

            });
        }
    }


}