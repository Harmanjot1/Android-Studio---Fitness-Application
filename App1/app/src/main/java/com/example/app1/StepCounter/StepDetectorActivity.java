package com.example.app1.StepCounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app1.R;

public class StepDetectorActivity extends AppCompatActivity {

    EditText inchStep;
    TextView counter;

    Button start;
    Button stop;
    Button back;

    String countedStep;
    String DetectedStep;
    static final String State_Count = "Counter";
    static final String State_Detect = "Detector";

    boolean isServiceStopped;


    private Intent intent;
    private static final String TAG = "SensorEvent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_step_detector);
        intent = new Intent(this, StepCountingService.class);

        viewInit(); // Call view initialisation method.
    }


    private SensorManager mSensorManager;

    // Initialise step_detector layout view
    public void viewInit() {

        isServiceStopped = true; //Current Service state

        start = (Button) findViewById(R.id.broadcaster_start_btn);
        counter = (TextView) findViewById(R.id.counter);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) == null) {
                    String msg1 = "No Sensor counter";
                    Toast toast = Toast.makeText(StepDetectorActivity.this, msg1, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    String startmessage = "Step counter started";
                    Toast start_toast = Toast.makeText(StepDetectorActivity.this, startmessage, Toast.LENGTH_SHORT);
                    start_toast.show();
                }

                // start Service.
                startService(new Intent(getBaseContext(), StepCountingService.class));

                // register BroadcastReceiver
                registerReceiver(broadcastReceiver, new IntentFilter(StepCountingService.BROADCAST_ACTION));
                isServiceStopped = false;
            }
        });

        //To stop step detector service
        stop = (Button) findViewById(R.id.broadcaster_stop_btn);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isServiceStopped) {
                    String stopmessage = "Step counter stopped";
                    Toast toast = Toast.makeText(StepDetectorActivity.this, stopmessage, Toast.LENGTH_SHORT);
                    toast.show();
                    //unregisterReceiver
                    unregisterReceiver(broadcastReceiver);
                    isServiceStopped = true;

                    // stop Service.
                    stopService(new Intent(getBaseContext(), StepCountingService.class));

                }
            }
        });
        back = (Button) findViewById(R.id.step_back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Handling back button click
        //Going back from current StepDetector Activity to Main Activity
        //back = (Button)findViewById(R.id.button_back);


        counter = (TextView) findViewById(R.id.step_counter);
    }

    /*
     *Method to save the contact in to the local database(text file)
     */

    protected void onPause() {
        super.onPause();
    }

    // OnResume resetting to previously left state of step_detector layout
    protected void onResume() {
        super.onResume();
        intent = new Intent(this, StepCountingService.class);
        viewInit();
    }


    // BroadcastReceiver to receive the message from the Step Detector Service
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateViews(intent);
        }
    };

    // Retrieve and set data of counter
    private void updateViews(Intent intent) {
        countedStep = intent.getStringExtra("Counted_Step");
        DetectedStep = intent.getStringExtra("Detected_Step");
        Log.d(TAG, String.valueOf(countedStep));
        Log.d(TAG, String.valueOf(DetectedStep));

        counter.setText(String.valueOf(countedStep));
    }
}