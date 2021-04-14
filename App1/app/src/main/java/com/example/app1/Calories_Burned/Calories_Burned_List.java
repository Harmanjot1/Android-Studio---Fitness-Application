package com.example.app1.Calories_Burned;

import java.util.ArrayList;
import java.util.List;

public class Calories_Burned_List {

    List<Calories_Burned_Object> reason_burned;

    String reason;

    Calories_Burned_Object calories_burned_object;

    private int total = 0;

    public Calories_Burned_List(){
        this.reason_burned = new ArrayList<>();


    }

    public List<Calories_Burned_Object> getMyCalories_Burned_List(){
        return reason_burned;
    }

    public void  setCalories_Burned_List(List<Calories_Burned_Object> reason_burned){
        this.reason_burned = reason_burned;
    }
    public int getTotal(){
        total = 0;
        for (int i = 0; i<reason_burned.size();i++){
            calories_burned_object = reason_burned.get(i);
            total += calories_burned_object.getAmount_burned();
        }
        return total;
    }

}
