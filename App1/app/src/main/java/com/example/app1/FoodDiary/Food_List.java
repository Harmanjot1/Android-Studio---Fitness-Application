package com.example.app1.FoodDiary;

import com.example.app1.FoodDiary.Food;

import java.util.ArrayList;
import java.util.List;

public class Food_List {

    List<Food> myFoodList;
    private int totalcal = 0;
    private int totalprotein = 0;
    private String dateAdded = " ";

    Food food;


    public Food_List() {

        this.myFoodList = new ArrayList<>();

    }

    public int getTotalcal() {

        totalcal = 0;
        for (int i = 0; i < myFoodList.size(); i++) {

            food = myFoodList.get(i);
            totalcal += food.getCalories();
        }
        return totalcal;
    }

    public int getTotalprotein() {

        totalprotein = 0;
        for (int i = 0; i < myFoodList.size(); i++) {

            food = myFoodList.get(i);
            totalprotein += food.getProtein();
        }
        return totalprotein;
    }

    public String getDateAdded() {
        for (int i = 0; i < myFoodList.size(); i++) {
            food = myFoodList.get(i);
            dateAdded = food.getDate();
        }
        return dateAdded;
    }


    public List<Food> getMyFoodList() {
        return myFoodList;
    }

    public void setMyFoodList(List<Food> myFoodList) {
        this.myFoodList = myFoodList;
    }
}
