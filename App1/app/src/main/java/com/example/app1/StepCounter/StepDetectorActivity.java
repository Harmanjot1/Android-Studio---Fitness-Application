package com.example.app1.StepCounter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app1.Dashboard_frag;
import com.example.app1.Database.DataBaseHelper;
import com.example.app1.LoadingScreens.Challenge_loadingscreen;
import com.example.app1.Push_Up.Pushup_frag;
import com.example.app1.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StepDetectorActivity extends AppCompatActivity {

    EditText inchStep;
    TextView counter;

    Button start;
    Button stop;
    Button back;
    Button save;

    ImageView help;

    String countedStep;
    String DetectedStep;
    int counted;
    static final String State_Count = "Counter";
    static final String State_Detect = "Detector";

    boolean isServiceStopped;

    private SimpleDateFormat dateFormat;
    private String todaysDate,previousDate = "";

    // Database
    DataBaseHelper db;

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
        db = new DataBaseHelper(this);
        intent = new Intent(this, StepCountingService.class);

        viewInit(); // Call view initialisation method.
    }


    private SensorManager mSensorManager;

    // Initialise step_detector layout view
    public void viewInit() {

        isServiceStopped = true; //Current Service state

        start = (Button) findViewById(R.id.broadcaster_start_btn);
        counter = (TextView) findViewById(R.id.counter);
        save = (Button) findViewById(R.id.step_save_btn);

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
                Stop();
            }
        });
        back = (Button) findViewById(R.id.step_back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        help = findViewById(R.id.StepDetector_help);

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getBaseContext());
                alert.setTitle("Help");
                alert.setMessage("Press 'Start' to initiate counting steps.\nPress 'Stop' to halt StepCounter or press 'Save' to halt StepCounter and save current steps taken.");
                alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.create().show();
            }
        });

        //Handling back button click
        //Going back from current StepDetector Activity to Main Activity
        //back = (Button)findViewById(R.id.button_back);


        counter = (TextView) findViewById(R.id.step_counter);
    }
    public void Stop(){
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
        getDate();

        counter.setText(String.valueOf(countedStep));

        counted = Integer.parseInt(countedStep);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counted >0){
                    Stop();
                    db.addSteps(new Steps(-1,counted,todaysDate));

                }
                finish();
            }
        });


    }

    public void getDate() {
        Calendar calendar;

        calendar = Calendar.getInstance();

        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        todaysDate = dateFormat.format(calendar.getTime());

    }
}