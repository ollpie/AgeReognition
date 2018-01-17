package com.example.olive.agerecognitionstudy;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by olive on 17.01.2018.
 */

public class MotionSensorUtil implements SensorEventListener {

    float xAcc = 0.0F;
    float yAcc = 0.0F;
    float zAcc = 0.0F;
    float xGrav = 0.0F;
    float yGrav = 0.0F;
    float zGrav = 0.0F;
    float xGyro = 0.0F;
    float yGyro = 0.0F;
    float zGyro = 0.0F;
    float xRot = 0.0F;
    float yRot = 0.0F;
    float zRot = 0.0F;

    MotionSensorDataModel motionSensorData;
    private SensorManager sensorManager;

    public MotionSensorUtil(String participant, String taskID, SensorManager systemService) {
        motionSensorData = new MotionSensorDataModel(participant, taskID);
        sensorManager = systemService;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        motionSensorData.setTimestamp(System.currentTimeMillis());
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xAcc = event.values[0];
            yAcc = event.values[1];
            zAcc = event.values[2];
        }
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            xGrav = event.values[0];
            yGrav = event.values[1];
            zGrav = event.values[2];
        }
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            xGyro = event.values[0];
            yGyro = event.values[1];
            zGyro = event.values[2];
        }
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            xRot = event.values[0];
            yRot = event.values[1];
            zRot = event.values[2];
        }
        motionSensorData.setXAcc(xAcc);
        motionSensorData.setYAcc(yAcc);
        motionSensorData.setZAcc(zAcc);
        motionSensorData.setXGravity(xGrav);
        motionSensorData.setYGravity(yGrav);
        motionSensorData.setZGravity(zGrav);
        motionSensorData.setXGyroscope(xGyro);
        motionSensorData.setYGyroscope(yGyro);
        motionSensorData.setZGyroscope(zGyro);
        motionSensorData.setXRotation(xRot);
        motionSensorData.setYRotation(yRot);
        motionSensorData.setZRotation(zRot);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void registerListeners() {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    public MotionSensorDataModel getMotionSensorData() {
        return motionSensorData;
    }
}
