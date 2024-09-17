package com.example.sensordata;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer, gyroscope, magnetometer;
    private FileWriter fileWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize SensorManager and sensors
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Register listeners for each sensor
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);

        // Create file to store sensor data
        try {
            fileWriter = new FileWriter("/sdcard/sensor_data.csv", true);
            fileWriter.append("Timestamp, Acc_X, Acc_Y, Acc_Z, Gyro_X, Gyro_Y, Gyro_Z, Mag_X, Mag_Y, Mag_Z\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        StringBuilder data = new StringBuilder();
        data.append(System.currentTimeMillis()).append(",");

        // Collect sensor data
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            data.append(event.values[0]).append(",").append(event.values[1]).append(",").append(event.values[2]).append(",");
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            data.append(event.values[0]).append(",").append(event.values[1]).append(",").append(event.values[2]).append(",");
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            data.append(event.values[0]).append(",").append(event.values[1]).append(",").append(event.values[2]);
        }

        try {
            fileWriter.append(data.toString()).append("\n");
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing for this example
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);

        // Close file when app is destroyed
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
