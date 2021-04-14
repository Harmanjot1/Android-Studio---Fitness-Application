package com.example.app1.Calories_Burned;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.app1.R;

public class Calories_Burned_Adapter extends BaseAdapter {

    Activity mActivity;
    Calories_Burned_List calories_burned_list;

    public Calories_Burned_Adapter(Activity mActivity, Calories_Burned_List calories_burned_list) {
        this.mActivity = mActivity;
        this.calories_burned_list = calories_burned_list;
    }

    @Override
    public int getCount() {
        return calories_burned_list.getMyCalories_Burned_List().size();
    }

    @Override
    public Calories_Burned_Object getItem(int position) {
        return calories_burned_list.getMyCalories_Burned_List().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View Calories_BurnedLine;

        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Calories_BurnedLine = inflater.inflate(R.layout.calories_burned_line,parent,false);

        TextView calories_reason = Calories_BurnedLine.findViewById(R.id.calories_burned_activity);
        TextView calories_amount = Calories_BurnedLine.findViewById(R.id.calories_burned_amount);

        Calories_Burned_Object object = this.getItem(position);

        calories_reason.setText(object.getReason());
        calories_amount.setText(Integer.toString(object.getAmount_burned()));


        return Calories_BurnedLine;
    }
}
