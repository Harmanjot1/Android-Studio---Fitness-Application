package com.example.app1.Calories_Burned;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.app1.Database.DataBaseHelper;
import com.example.app1.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewCaloriesBurned_Form extends Fragment {

    Button cancel,save;
    EditText reason, amount;

    DataBaseHelper db;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String todaysDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container.removeAllViews();
        final View layout = inflater.inflate(R.layout.fragment_new_calories_burned__form, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        reason = (EditText) layout.findViewById(R.id.add_calories_reason);
        amount = (EditText) layout.findViewById(R.id.add_Calories_amount);

        save = (Button) layout.findViewById(R.id.Calories_burned_add_btnSave);
        cancel = (Button) layout.findViewById(R.id.Calories_Burned_add_btnCancel);

        db = new DataBaseHelper(getContext());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDate();

                String Stringreason = reason.getText().toString();
                if (Stringreason.isEmpty() && amount != null){
                    Toast.makeText(getContext(), "Please enter the Reason and amount burned",
                            Toast.LENGTH_LONG).show();
                }else {
                    db.addCaloriesBurned(new Calories_Burned_Object(-1,Stringreason,Integer.parseInt(String.valueOf(amount.getText())),todaysDate));
                }
                // Get String

                // put string into message for Calories Burned

                // Start calories_burned

                FragmentManager fragmentManager = getFragmentManager();
                Calories_Burned newCaloriesBurned = new Calories_Burned();
                fragmentManager.beginTransaction().replace(R.id.fragment,newCaloriesBurned).addToBackStack(null).commit();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Calories_Burned newCaloriesBurned = new Calories_Burned();
                fragmentManager.beginTransaction().replace(R.id.fragment,newCaloriesBurned).addToBackStack(null).commit();
            }
        });



        return layout;
    }

    public void getDate() {
        calendar = Calendar.getInstance();

        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        todaysDate = dateFormat.format(calendar.getTime());
    }

}