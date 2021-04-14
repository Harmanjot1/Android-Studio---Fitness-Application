package com.example.app1.Push_Up;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.app1.Dashboard_frag;
import com.example.app1.Database.DataBaseHelper;
import com.example.app1.R;
import com.example.app1.StopWatch.StopWatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Pushup_frag extends Fragment implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mProximity;
    //private static final int SENSOR_SENSITIVITY = 4;
    private boolean state;
    private int counter = 0;
    private Button sensorMode, touchMode, backButton, saveButton,counterbtn;

    DataBaseHelper db;

    private String todaysDate,previousDate = "";
    private SimpleDateFormat dateFormat;

    Push_Up push_up;

    private int todays_pushup, PB_pushup;
    private String pushup_date;
    private List<Push_Up> pushup_list = new ArrayList<>();

    // Edit texts
    TextView todays_pushups_txt, PB_pushups_txt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_pushup_frag, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        sensorMode = (Button) layout.findViewById(R.id.sensorMode_button);
        touchMode = (Button) layout.findViewById(R.id.touchMode_button);
        backButton = (Button) layout.findViewById(R.id.backCounter_button);
        counterbtn = (Button) layout.findViewById(R.id.counter);
        saveButton = (Button) layout.findViewById(R.id.pushup_save_btn);

        todays_pushups_txt = (TextView) layout.findViewById(R.id.todays_pushups_text);
        PB_pushups_txt = (TextView) layout.findViewById(R.id.PB_pushups_text) ;

        db = new DataBaseHelper(getContext());


        if (db.getPushups() != null){

            getTodays_Pushups();
            getPB_pushups();
            get_Date_Pushups();
            getDate();


            pushup_list.addAll(db.getPushups());
            resetNewDay();
        }

        getTodays_Pushups();
        getPB_pushups();
        get_Date_Pushups();
        getDate();

        todays_pushups_txt.setText(todays_pushup+"");
        PB_pushups_txt.setText(PB_pushup+"");



        preference_btns();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delete_pushup();
                int newcurrent_pushups = todays_pushup + counter;
                if (newcurrent_pushups > PB_pushup){
                    db.addPushUp(new Push_Up(newcurrent_pushups,newcurrent_pushups,todaysDate));
                }else {
                    db.addPushUp(new Push_Up(newcurrent_pushups,PB_pushup,todaysDate));
                }
                FragmentManager fragmentManager = getFragmentManager();
                Dashboard_frag dashboard_frag = new Dashboard_frag();
                fragmentManager.beginTransaction().replace(R.id.fragment,dashboard_frag).commit();



            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Dashboard_frag dashboard_frag = new Dashboard_frag();
                fragmentManager.beginTransaction().replace(R.id.fragment,dashboard_frag).commit();
            }
        });

        return layout;
    }
    public void preference_btns(){
        sensorMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //state = true;
                sensorMode.setVisibility(View.GONE);
                touchMode.setVisibility(View.GONE);
                sensorMode();

            }
        });
        touchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPause();
                sensorMode.setVisibility(View.GONE);
                touchMode.setVisibility(View.GONE);
                screenTouch();
            }
        });
    }
    public void sensorMode(){
        counter = 0;
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        onResume();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        state = true;
        TextView actionEvent = getActivity().findViewById(R.id.counter);


        if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {

            actionEvent.setText("" + counter);
            if (sensorEvent.values[0] == 0) {
                counter++;

            } else {

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
    }

    public void screenTouch() {
        final TextView actionEvent = getActivity().findViewById(R.id.counter);

        ConstraintLayout constraintLayout = (ConstraintLayout) getActivity().findViewById(R.id.pushup_layout);
        counterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter += 1;
                actionEvent.setText("" + counter);
            }
        });

        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                counter += 1;
                actionEvent.setText("" + counter);
                return false;
            }
        });
    }

    public void getDate() {
        Calendar calendar;

        calendar = Calendar.getInstance();

        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        todaysDate = dateFormat.format(calendar.getTime());

    }
    public int getTodays_Pushups(){
        for (int i=0; i<pushup_list.size();i++){
            push_up = pushup_list.get(i);
            todays_pushup = push_up.getTodays_Pushups();
        }
        return todays_pushup;
    }
    public int getPB_pushups(){
        for (int i =0; i<pushup_list.size();i++){
            push_up = pushup_list.get(i);
            PB_pushup = push_up.getPB_Pushups();
        }
        return PB_pushup;
    }

    public String get_Date_Pushups(){
        if (db.getPushups() != null){
            for (int i =0; i<pushup_list.size();i++){
                push_up = pushup_list.get(i);
                previousDate = push_up.getDate();
            }
        }
        return previousDate;
    }

    public void resetNewDay() {
        get_Date_Pushups();

        if (previousDate.equals(todaysDate)){


        }else {
            db.delete_pushup();
            db.addPushUp(new Push_Up(0,PB_pushup,todaysDate));


        }
    }
}