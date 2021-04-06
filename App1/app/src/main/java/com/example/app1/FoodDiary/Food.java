package com.example.app1.FoodDiary;

import java.util.Date;

public class Food {

    private String Name;
    private int ID;
    private int Calories;
    private int Protein;
    private int Picture;
    private String Date;


    public Food(int id, String name, int calories, int protein, int picture, String date) {
        Date = date;
        ID = id;
        Name = name;
        Calories = calories;
        Protein = protein;
        Picture = picture;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getCalories() {
        return Calories;
    }

    public void setCalories(int calories) {
        Calories = calories;
    }

    public int getProtein() {
        return Protein;
    }

    public void setProtein(int protein) {
        Protein = protein;
    }

    public int getPicture() {
        return Picture;
    }

    public void setPicture(int picture) {
        Picture = picture;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }


}
