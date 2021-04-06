package com.example.app1.FoodDiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.app1.Database.DataBaseHelper;
import com.example.app1.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewFoodForm extends AppCompatActivity {

    Button cancle, save;
    EditText ET_Name, ET_Calories, ET_Protein;
    Button breakfast_btn, lunch_btn, dinner_btn, snacks_btn;
    Switch favFoodSwitch;
    int diet_picture;
    DataBaseHelper db;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String todaysDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_new_food_form);

        cancle = findViewById(R.id.button_cancel);
        save = findViewById(R.id.button_save_add);

        ET_Name = findViewById(R.id.food_name_editTxt);
        ET_Calories = findViewById(R.id.calories_editTxt);
        ET_Protein = findViewById(R.id.protein_editTxt);

        breakfast_btn = findViewById(R.id.breakfast_btn);
        lunch_btn = findViewById(R.id.lunch_btn);
        dinner_btn = findViewById(R.id.dinner_btn);
        snacks_btn = findViewById(R.id.snacks_btn);
        favFoodSwitch = findViewById(R.id.food_fav_switch);

        breakfast_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diet_picture = 0;
            }
        });
        lunch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diet_picture = 1;
            }
        });
        dinner_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diet_picture = 2;
            }
        });
        snacks_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diet_picture = 3;
            }
        });


        db = new DataBaseHelper(this);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDate();

                String stringName = ET_Name.getText().toString();

                if (favFoodSwitch.isChecked()){
                    db.fav_addFood(new Food(-1, ET_Name.getText().toString(),
                            Integer.parseInt(ET_Calories.getText().toString()),
                            Integer.parseInt(ET_Protein.getText().toString()),
                            diet_picture, todaysDate));

                    db.addFood(new Food(-1, ET_Name.getText().toString(),
                            Integer.parseInt(ET_Calories.getText().toString()),
                            Integer.parseInt(ET_Protein.getText().toString()),
                            diet_picture, todaysDate));

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    DietPlan_frag dietPlan_frag = new DietPlan_frag();
                    fragmentManager.beginTransaction().replace(R.id.fragment,dietPlan_frag).commit();

                }else{
                    if (stringName.isEmpty() && ET_Calories!= null){
                        Toast.makeText(NewFoodForm.this, "Please enter the name and calories",
                                Toast.LENGTH_LONG).show();
                    }else {

                        db.addFood(new Food(-1, ET_Name.getText().toString(),
                                Integer.parseInt(String.valueOf(ET_Calories.getText())),
                                Integer.parseInt(String.valueOf(ET_Protein.getText())),
                                diet_picture, todaysDate));

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        DietPlan_frag dietPlan_frag = new DietPlan_frag();
                        fragmentManager.beginTransaction().replace(R.id.fragment,dietPlan_frag).commit();
                    }

                }


            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
    }

    public void getDate() {
        calendar = Calendar.getInstance();

        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        todaysDate = dateFormat.format(calendar.getTime());
    }
    

}













