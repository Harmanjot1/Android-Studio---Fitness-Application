package com.example.app1;

import android.app.Application;

import com.example.app1.Calories_Burned.Calories_Burned_List;
import com.example.app1.FoodDiary.Food_List;


public class Activity extends Application {
    private Food_List food_list = new Food_List();

    private Calories_Burned_List calories_burned_list = new Calories_Burned_List();

    public Food_List getFood_list() {
        return food_list;
    }
    public Calories_Burned_List getCalories_burned_list(){
        return calories_burned_list;
    }

    public void setFood_list(Food_List food_list) {
        this.food_list = food_list;
    }

}
