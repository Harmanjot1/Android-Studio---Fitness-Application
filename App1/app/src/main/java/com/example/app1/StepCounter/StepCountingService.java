package com.example.app1.StepCounter;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class StepCountingService extends Service implements SensorEventListener {
    SensorManager sensorManager;
    Sensor stepCounterSensor;
    Sensor stepDetectorSensor;

    int currentStepsDetected;

    int stepCounter;
    int newStepCounter;

    boolean serviceStopped;

    Intent intent;

    private static final String TAG = "StepService";
    public static final String BROADCAST_ACTION = "com.example.app1.StepCounter.StepCountingService";

    // Creating a handler that will be used to broadcast data
    private final Handler handler = new Handler();


    // Service will be created
    @Override
    public void onCreate() {
        super.onCreate();

        intent = new Intent(BROADCAST_ACTION);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensorManager.registerListener(this, stepCounterSensor, 0);
        sensorManager.registerListener(this, stepDetectorSensor, 0);

        //currentStepCount = 0;
        currentStepsDetected = 0;

        stepCounter = 0;
        newStepCounter = 0;

        serviceStopped = false;

        // Existing callbacks to the handler are removed
        handler.removeCallbacks(updateBroadcastData);
        // handler call
        handler.post(updateBroadcastData);

        return START_STICKY;
    }

    // Binding the service
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Service is being destroyed when not in use
    @Override
    public void onDestroy() {
        super.onDestroy();

        serviceStopped = true;
    }

    // system is running low on memory,actively running processes should reduce their memory usage. */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    //Here Step detector was more accurate than step counter
    @Override
    public void onSensorChanged(SensorEvent event) {

        //Step count sensor
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int countSteps = (int) event.values[0];

            // Initially stepCounter will be zero
            if (stepCounter == 0) {
                stepCounter = (int) event.values[0];
            }
            newStepCounter = countSteps - stepCounter;
        }

        // Step detector sensor
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            int detectSteps = (int) event.values[0];
            currentStepsDetected += detectSteps;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    //Update data by broadcasting
    private final Runnable updateBroadcastData = new Runnable() {
        public void run() {
            if (!serviceStopped) {
                // Broadcast data to the Activity
                broadcastSensorValue();
                handler.postDelayed(this, 1000);
            }
        }
    };

    // Broadcast data through intent
    private void broadcastSensorValue() {
        Log.d(TAG, "Data to Activity");
        // add step counter to intent.
        intent.putExtra("Counted_Step_Int", newStepCounter);
        intent.putExtra("Counted_Step", String.valueOf(newStepCounter));
        // add step detector to intent.
        intent.putExtra("Detected_Step_Int", currentStepsDetected);
        intent.putExtra("Detected_Step", String.valueOf(currentStepsDetected));
        //sendBroadcast sends a message to whoever is registered to receive it.
        sendBroadcast(intent);
    }
}