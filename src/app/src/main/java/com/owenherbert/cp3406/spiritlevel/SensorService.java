package com.owenherbert.cp3406.spiritlevel;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * SensorService class is a Service that performs long-running location and sensor listening
 * operations in the background. The SensorService class also provides some utility methods.
 */
public class SensorService extends Service implements SensorEventListener {

    // device hardware sensors
    private final SensorManager sensorManager;
    private final Sensor sensorRotationVector;

    private final TextView orientationTextView;
    private final SpiritLevelView spiritLevelView;

    float[] orientations;

    /**
     * Constructs a SensorService object
     *
     * @param sensorManager a SensorManager object
     */
    public SensorService(SensorManager sensorManager, TextView orientationTextView, SpiritLevelView spiritLevelView) {

        this.sensorManager = sensorManager;

        this.orientationTextView = orientationTextView;
        this.spiritLevelView = spiritLevelView;

        orientations = new float[3];

        // get default sensor
        sensorRotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        registerListeners();
    }

    /**
     * Register sensors and location updates.
     */
    @SuppressLint("MissingPermission")
    public void registerListeners() {

        sensorManager.registerListener(this, sensorRotationVector,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Unregister sensors and location updates.
     */
    public void unregisterListeners() {

        sensorManager.unregisterListener(this, sensorRotationVector);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    /**
     * Called when there is a new sensor event. Note that "on changed" is somewhat of a misnomer,
     * as this will also be called if we have a new reading from a sensor with the exact same
     * sensor values (but a newer timestamp).
     *
     * @param sensorEvent the SensorEvent
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        float[] rotationMatrix = new float[16];

        SensorManager.getRotationMatrixFromVector(rotationMatrix, sensorEvent.values);

        float[] mappedRotationMatrix = new float[16];
        SensorManager.remapCoordinateSystem(rotationMatrix,
                SensorManager.AXIS_X, SensorManager.AXIS_Z, mappedRotationMatrix);

        // convert to orientations
        SensorManager.getOrientation(mappedRotationMatrix, orientations);
        for (int i = 0; i < 3; i++) {
            orientations[i] = (float) (Math.toDegrees(orientations[i]));
        }

        // log for for info
        Log.i("SensorService", String.format("X: %s Y: %s Z: %s", orientations[0],
                orientations[1], orientations[2]));

        String orientationString = Math.round(orientations[2]) + "°";

        // if there is a negative character remove it
        if (orientationString.charAt(0) == '-') {
            orientationString = orientationString.substring(1);
        }

        orientationTextView.setText(orientationString);
        orientationTextView.setRotation(-orientations[2]);
        spiritLevelView.setOrientation(orientations);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        unregisterListeners();
    }
}
