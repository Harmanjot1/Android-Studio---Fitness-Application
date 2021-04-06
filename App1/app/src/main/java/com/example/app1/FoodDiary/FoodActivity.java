package com.example.app1.FoodDiary;

import android.app.Application;

import com.example.app1.FoodDiary.Food_List;

public class FoodActivity extends Application {
    private Food_List food_list = new Food_List();

    public Food_List getFood_list() {
        return food_list;
    }

    public void setFood_list(Food_List food_list) {
        this.food_list = food_list;
    }

}
