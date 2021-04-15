package com.example.app1.FoodDiary;

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
import android.widget.ListView;

import com.example.app1.Database.DataBaseHelper;
import com.example.app1.Activity;
import com.example.app1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.SyncFailedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavFood extends Fragment {

    private static final String FAV_FOOD_DIARY = "" ;
    Button fav_edit, fav_back, fav_addFood, fav_Header, fav_Done;
    ListView favListView;

    FoodAdapter adapter;
    Food_List food_list;

    //Firebase
    FirebaseAuth rauth;

    DataBaseHelper dataBaseHelper = null;
    private boolean removeFoodActive = false;
    private  List<Food> favlist = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container.removeAllViews();
        final View layout = inflater.inflate(R.layout.fragment_fav_food, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        // Get firebase instance
        rauth = FirebaseAuth.getInstance();

        fav_edit = (Button) layout.findViewById(R.id.fav_food_edit_food_button);
        fav_back = (Button) layout.findViewById(R.id.fav_food_back_btn);
        fav_addFood = (Button) layout.findViewById(R.id.fav_food_btn_add);
        fav_Header = (Button) layout.findViewById(R.id.header);
        fav_Done = (Button) layout.findViewById(R.id.fav_food_edit_cancel_button);

        favListView = (ListView) layout.findViewById(R.id.fav_food_listView);

        food_list = ((Activity) this.getActivity().getApplication()).getFood_list();
        adapter = new FoodAdapter((android.app.Activity) getContext(), food_list);

        dataBaseHelper = new DataBaseHelper(getContext());

        favListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Food clickedFood = (Food) parent.getItemAtPosition(position);
                favlist.add(clickedFood);
                final int calories = clickedFood.getCalories();

                fav_addFood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dataBaseHelper.addFood(clickedFood);

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
                                int challenge_eaten = Integer.parseInt(challenge_getCalories_eaten);
                                int newTotal = challenge_eaten + calories;

                                // Create HashMap
                                Map targetMap = new HashMap<>();

                                targetMap.put("Goal: Calories Burned",getCalories_burned);
                                targetMap.put("Goal: Calories Eaten",getCalories_eaten);
                                targetMap.put("Goal: Running Distance",getDistance);
                                targetMap.put("Goal: Push-up's",getPushups);

                                targetMap.put("Challenge: Calories Burned",challenge_getCalories_burned);
                                targetMap.put("Challenge: Calories Eaten",newTotal);
                                targetMap.put("Challenge: Running Distance",challenge_getDistance);
                                targetMap.put("Challenge: Push-up's",challenge_getPushups);

                                databaseReference.setValue(targetMap);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        FragmentManager fragmentManager = getFragmentManager();
                        DietPlan_frag dietPlan_frag = new DietPlan_frag();
                        fragmentManager.beginTransaction().replace(R.id.fragment,dietPlan_frag).commit();

                    }
                });
            }
        });


        loadFood();
        btnMethod();
        return layout;
    }
    public void loadFood() {

        if (dataBaseHelper.getSavedFavFood() != null) {

            food_list.getMyFoodList().clear();
            food_list.getMyFoodList().addAll(dataBaseHelper.getSavedFavFood());

            showAdapter();
            adapter.notifyDataSetChanged();
        }
    }
    public void showAdapter() {
        adapter = new FoodAdapter((android.app.Activity) getContext(), food_list);
        favListView.setAdapter(adapter);
    }

    public void btnMethod(){
        fav_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFoodActive = true;
                fav_Header.setText("Tap on food to remove");
                removeFood();
                fav_Done.setVisibility(View.VISIBLE);
            }
        });

        fav_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                DietPlan_frag dietPlan_frag = new DietPlan_frag();
                fragmentManager.beginTransaction().replace(R.id.fragment,dietPlan_frag).commit();

            }
        });
    }

    public void removeFood() {

        if (removeFoodActive == true) {
            favListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Food clickedFood = (Food) parent.getItemAtPosition(position);
                    dataBaseHelper.fav_deleteFood(clickedFood);

                    //////////// dataBaseHelper.fav_deleteFood(clickedFood); Change fav_deletefood to add to take food into normal list
                    loadFood();
                    showAdapter();

                }

            });
        }
    }



}