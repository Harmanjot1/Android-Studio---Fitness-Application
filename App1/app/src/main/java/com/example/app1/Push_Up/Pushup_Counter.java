package com.example.app1.Push_Up;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.app1.Dashboard_frag;
import com.example.app1.Database.DataBaseHelper;
import com.example.app1.FoodDiary.FavFood;
import com.example.app1.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Pushup_Counter extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mProximity;
    //private static final int SENSOR_SENSITIVITY = 4;
    private boolean state;
    private int counter = 0;
    private Button sensorMode, touchMode, backButton, saveButton,counterbtn;

    DataBaseHelper db;

    private String todaysDate,previousDate;
    private SimpleDateFormat dateFormat;

    Push_Up push_up;

    private int todays_pushup, PB_pushup;
    private String pushup_date;
    private List<Push_Up> pushup_list = new ArrayList<>();

    // Edit texts
    TextView todays_pushups_txt, PB_pushups_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_pushup__counter);

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        sensorMode = (Button) findViewById(R.id.sensorMode_button);
        touchMode = (Button) findViewById(R.id.touchMode_button);
        backButton = (Button) findViewById(R.id.backCounter_button);
        counterbtn = (Button) findViewById(R.id.counter);
        saveButton = (Button) findViewById(R.id.pushup_save_btn);

        todays_pushups_txt = (TextView) findViewById(R.id.todays_pushups_text);
        PB_pushups_txt = (TextView) findViewById(R.id.PB_pushups_text) ;

        db = new DataBaseHelper(this);

        final int todays_pushups = 0;
        final int pb_pushups = 0;

        if (db.getPushups() != null){
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



            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Dashboard_frag dashboard_frag = new Dashboard_frag();
                fragmentManager.beginTransaction().replace(R.id.container,dashboard_frag).commit();
            }
        });

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
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        onResume();
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        state = true;
        TextView actionEvent = findViewById(R.id.counter);


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
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
    }

    public void screenTouch() {
        final TextView actionEvent = findViewById(R.id.counter);

        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.pushup_layout);
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
        for (int i =0; i<pushup_list.size();i++){
            push_up = pushup_list.get(i);
            previousDate = push_up.getDate();
        }
        return previousDate;
    }

    public void resetNewDay() {

        getDate();
        get_Date_Pushups();
        getPB_pushups();


        if (previousDate.equals(todaysDate)){

        }else {
            db.delete_pushup();
            db.addPushUp(new Push_Up(0,PB_pushup,todaysDate));


        }
    }



}