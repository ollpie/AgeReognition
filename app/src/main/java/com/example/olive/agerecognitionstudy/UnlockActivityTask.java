package com.example.olive.agerecognitionstudy;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.PatternLockView.Dot;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.patternlockview.utils.ResourceUtils;

import java.util.List;

public class UnlockActivityTask extends AppCompatActivity implements SensorEventListener{

    private SensorManager sensorManager;
    private ImageView cross;

    private float x_Acc = 0.0F;
    private float y_Acc = 0.0F;
    private float z_Acc = 0.0F;

    private float x_Gravity = 0.0F;
    private float y_Gravity = 0.0F;
    private float z_Gravity = 0.0F;

    private float x_Gyroscope = 0.0F;
    private float y_Gyroscope = 0.0F;
    private float z_Gyroscope = 0.0F;

    private float x_Rotation = 0.0F;
    private float y_Rotation = 0.0F;
    private float z_Rotation = 0.0F;

    private PatternLockView mPatternLockView;
    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
        @Override
        public void onStarted() {
            Log.d(getClass().getName(), "Pattern drawing started");
        }

        @Override
        public void onProgress(List<Dot> progressPattern) {
            Log.d(getClass().getName(), "Pattern progress: " +
                    PatternLockUtils.patternToString(mPatternLockView, progressPattern));
        }

        @Override
        public void onComplete(List<Dot> pattern) {
            Log.d(getClass().getName(), "Pattern complete: " +
                    PatternLockUtils.patternToString(mPatternLockView, pattern));
        }

        @Override
        public void onCleared() {
            Log.d(getClass().getName(), "Pattern has been cleared");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock_task);
        setupPatternUnlock();
        cross = findViewById(R.id.cross);
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();

        switch(eventAction) {
            case MotionEvent.ACTION_DOWN:
                //writeDataIntoLists("Down", event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                //writeDataIntoLists("Move", event.getX(), event.getY());
                cross.setX(event.getX()- 13);
                cross.setY(event.getY() - MainActivity.statusbarOffset -13);
                break;

            case MotionEvent.ACTION_UP:
                //writeDataIntoLists("Up", event.getX(), event.getY());
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    private void setupPatternUnlock () {
        mPatternLockView = findViewById(R.id.pattern_lock_view);
        mPatternLockView.setDotCount(3);
        mPatternLockView.setDotNormalSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_size));
        mPatternLockView.setDotSelectedSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_selected_size));
        mPatternLockView.setPathWidth((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_path_width));
        mPatternLockView.setAspectRatioEnabled(true);
        mPatternLockView.setAspectRatio(PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS);
        mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
        mPatternLockView.setDotAnimationDuration(150);
        mPatternLockView.setPathEndAnimationDuration(100);
        mPatternLockView.setCorrectStateColor(ResourceUtils.getColor(this, R.color.pomegranate));
        mPatternLockView.setInStealthMode(false);
        mPatternLockView.setTactileFeedbackEnabled(true);
        mPatternLockView.setInputEnabled(true);
        mPatternLockView.addPatternLockListener(mPatternLockViewListener);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x_Acc = event.values[0];
            y_Acc = event.values[1];
            z_Acc = event.values[2];
        }
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            x_Gravity = event.values[0];
            y_Gravity = event.values[1];
            z_Gravity = event.values[2];
        }
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            x_Gyroscope = event.values[0];
            y_Gyroscope = event.values[1];
            z_Gyroscope = event.values[2];
        }
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            x_Rotation = event.values[0];
            y_Rotation = event.values[1];
            z_Rotation = event.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors
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

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
