package com.example.app1.FoodDiary;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app1.R;

public class FoodAdapter extends BaseAdapter {

    Activity myActivity;
    Food_List food_list;


    public FoodAdapter(Activity myActivity, Food_List food_list) {
        this.myActivity = myActivity;
        this.food_list = food_list;
    }

    @Override
    public int getCount() {
        return food_list.getMyFoodList().size();
    }

    @Override
    public Food getItem(int pos) {
        return food_list.getMyFoodList().get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        View FoodObj;

        LayoutInflater layoutInflater = (LayoutInflater) myActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FoodObj = layoutInflater.inflate(R.layout.food_one_line, viewGroup, false);

        TextView textView_Name = FoodObj.findViewById(R.id.food_name_txt);
        TextView textView_calories = FoodObj.findViewById(R.id.calories_txt);
        TextView textView_protein = FoodObj.findViewById(R.id.protein_txt);
        ImageView imageView_picture = FoodObj.findViewById(R.id.food_image);

        Food food = this.getItem(pos);

        textView_Name.setText(food.getName());
        textView_calories.setText("Calories:  " + food.getCalories());
        textView_protein.setText("Protein:  " + food.getProtein());

        int[] icon_resource_numbers = {
                R.drawable.breakfast_icon,
                R.drawable.lunch_icon,
                R.drawable.dinner_icon,
                R.drawable.diet_logo

        };

        imageView_picture.setImageResource(icon_resource_numbers[food.getPicture()]);

        return FoodObj;
    }

}
