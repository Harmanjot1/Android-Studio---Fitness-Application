package com.example.app1.FoodDiary;

import android.content.DialogInterface;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DietPlan_frag extends Fragment {

    Button breakfast_addFood, edit_food_button, back_btn;
    TextView header_text_button;
    Button edit_cancel_btn, add_from_fav_btn;
    TextView calories_Set_Text;
    TextView protein_Set_Text;

    ImageView help;

    //Firebase
    FirebaseAuth rauth;
    int removeFoodCalories; // new altered calories

    ListView breakfast_FoodList;

    private String todaysDate;
    private SimpleDateFormat dateFormat;

    private boolean removeFoodActive = false;



    FoodAdapter adapter;
    Food_List food_list;


    DataBaseHelper dataBaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container.removeAllViews();
        final View layout = inflater.inflate(R.layout.fragment_diet_plan_frag, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        // Get firebase instance
        rauth = FirebaseAuth.getInstance();

        breakfast_addFood = layout.findViewById(R.id.breakfast_btn_add);
        edit_food_button = layout.findViewById(R.id.edit_food_button);
        header_text_button = layout.findViewById(R.id.foodText);
        back_btn = layout.findViewById(R.id.food_diary_back_btn);
        add_from_fav_btn = layout.findViewById(R.id.add_from_fav_btn);

        help = (ImageView) layout.findViewById(R.id.DietPlan_help);

        //
        breakfast_FoodList = layout.findViewById(R.id.breakfast_listView);

        //
        calories_Set_Text = layout.findViewById(R.id.calories_set_text);
        protein_Set_Text = layout.findViewById(R.id.protein_set_text);
        edit_cancel_btn = layout.findViewById(R.id.edit_cancel_button);

        // --------------------------------------------------

        food_list = ((Activity) getActivity().getApplication()).getFood_list();
        adapter = new FoodAdapter(getActivity(), food_list);

        dataBaseHelper = new DataBaseHelper(getContext());

        loadFood();

        btnMethod();
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Help");
                alert.setMessage("To add new food click 'Add'\nTo Remove food, click 'Edit' and simply tap on the food to remove.\n\nAlternatively add food from your favourites.");
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
    public void btnMethod() {
        breakfast_addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                NewFoodForm_frag newFoodForm_frag = new NewFoodForm_frag();
                fragmentManager.beginTransaction().replace(R.id.fragment,newFoodForm_frag).addToBackStack(null).commit();
            }
        });

        edit_food_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFoodActive = true;
                header_text_button.setText("Tap on food to remove");
                removeFood();
                edit_cancel_btn.setVisibility(View.VISIBLE);


            }
        });
        edit_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                header_text_button.setText("Food Diary");
                breakfast_FoodList.setOnItemClickListener(null);
                edit_cancel_btn.setVisibility(View.GONE);
            }
        });
        add_from_fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FavFood favFood = new FavFood();
                fragmentManager.beginTransaction().replace(R.id.fragment,favFood).addToBackStack(null).commit();

            }
        });


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Main_loadingScreen dashboard_frag = new Main_loadingScreen();
                fragmentManager.beginTransaction().replace(R.id.fragment,dashboard_frag).addToBackStack(null).commit();
            }
        });
    }
    public void showAdapter() {
        adapter = new FoodAdapter(getActivity(), food_list);
        breakfast_FoodList.setAdapter(adapter);
    }

    public void loadFood() {

        if (dataBaseHelper.getSavedFood() != null) {

            food_list.getMyFoodList().clear();
            food_list.getMyFoodList().addAll(dataBaseHelper.getSavedFood());

            showAdapter();
            resetNewDay();
            getTotal();
            adapter.notifyDataSetChanged();
        }
    }

    public void getTotal() {
        getDate();
        calories_Set_Text.setText(food_list.getTotalcal() + "");
        protein_Set_Text.setText(food_list.getTotalprotein() + "");

    }

    public void getDate() {
        Calendar calendar;

        calendar = Calendar.getInstance();

        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        todaysDate = dateFormat.format(calendar.getTime());

    }


    public void removeFood() {

        if (removeFoodActive == true) {
            breakfast_FoodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Food clickedFood = (Food) parent.getItemAtPosition(position);

                    dataBaseHelper.deleteFood(clickedFood);

                    removeFoodCalories = clickedFood.getCalories();

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
                            int challenge_removeFood = Integer.parseInt(challenge_getCalories_eaten);
                            int newTotal = challenge_removeFood - removeFoodCalories;

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
                    loadFood();
                    showAdapter();
                    getTotal();
                }

            });
        }
    }

    public void resetNewDay() {

        getDate();

        if (food_list.getDateAdded().equals(todaysDate)){

        }else {
            food_list.getMyFoodList().clear();
            dataBaseHelper.deleteAllFood();
        }
    }




}