package com.owenherbert.cp3406.spiritlevel;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LevelActivity extends AppCompatActivity {

    SensorService sensorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView orientationTextView = findViewById(R.id.orientationTextView);
        SpiritLevelView spiritLevelView = (SpiritLevelView) findViewById(R.id.spiritLevelView);

        // create gps service
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorService = new SensorService(sensorManager, orientationTextView, spiritLevelView);
    }

    /**
     * Called after onRestoreInstanceState(Bundle), onRestart(), or onPause(), for your activity
     * to start interacting with the user. This is an indicator that the activity became active and
     * ready to receive input. It is on top of an activity stack and visible to user.
     */
    @Override
    protected void onResume() {

        super.onResume();

        // register listeners
        sensorService.registerListeners();
    }

    /**
     * Called as part of the activity lifecycle when the user no longer actively interacts with the
     * activity, but it is still visible on screen. The counterpart to onResume().
     */
    @Override
    protected void onPause() {

        super.onPause();

        // unregister listeners
        sensorService.unregisterListeners();
    }
}