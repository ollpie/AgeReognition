package com.example.olive.agerecognitionstudy;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Random;

public class GenericTaskActivity extends AppCompatActivity implements SensorEventListener{

    //, Float xAcc, Float yAcc, Float zAcc, Float xGravity, Float yGravity, Float zGravity, Float xGyroscope, Float yGyroscope, Float zGyroscope, Float xRotation, Float yRotation, Float zRotation

    private static final int MIN = 0;
    private static final int MAX_X = 14;
    private static final int MAX_Y = 25;
    private static final int TOUCH_AMOUNT = 3;
    private static final int OFFSET = 13;

    DatabaseHandler database;

    private Button nextButton;
    private ImageButton target;

    private Random randomX;
    private Random randomY;

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

    private int touch_counter = 0;
    private String userID = "";

    private GenericTaskDataModel taskmodel;
    private SensorManager sensorManager;

    private float xPositions[] = new float[]{30.0F, 42.0F, 74.0F, 102.0F, 134.0F, 166.0F, 198.0F, 230.0F, 262.0F, 294.0F, 326.0F, 358.0F, 390.0F, 422.0F, 454.0F};
    private float yPositions[] = new float[]{30.0F, 55.0F, 100.0F, 145.0F, 190.0F, 235.0F, 280.0F, 325.0F, 370.0F, 415.0F, 460.0F, 505.0F, 550.0F, 595.0F, 640.0F, 685.0F, 735.0F, 780.0F, 825.0F, 870.0F, 915.0F, 960.0F, 1005.0F, 1050.0F, 1095.0F, 1140.0F, 1185.0F};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        userID = MainActivity.currentUserID;
        database = MainActivity.getDbHandler();
        taskmodel = new GenericTaskDataModel();
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);

        randomX = new Random();
        randomY = new Random();

        target.setX(xPositions[randomX.nextInt(((MAX_X - MIN) +1)+MIN)]);
        target.setY(yPositions[randomY.nextInt(((MAX_Y - MIN) +1)+MIN)]);


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

    public void targetClicked(View view) {
        touch_counter++;

        if (touch_counter > TOUCH_AMOUNT){
            target.setVisibility(View.GONE);
            database.createGenericTaskEntry(taskmodel);
            database.closeDB();
            nextButton.setVisibility(View.VISIBLE);
        } else {
            target.setX(xPositions[randomX.nextInt(((MAX_X - MIN) +1)+MIN)]);
            target.setY(yPositions[randomY.nextInt(((MAX_Y - MIN) +1)+MIN)]);
        }
    }

    public void nextButtonClicked(View view) {

    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();

        switch(eventAction) {
            case MotionEvent.ACTION_DOWN:
                writeDataIntoLists("Down", event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                //writeDataIntoLists("Move", event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_UP:
                writeDataIntoLists("Up", event.getX(), event.getY());
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    private void writeDataIntoLists(String eventType, Float xTouch, Float yTouch) {
        taskmodel.setUserId(userID);
        taskmodel.setTargetId(touch_counter);
        taskmodel.setTimestamp(System.currentTimeMillis());
        taskmodel.setEventType(eventType);
        taskmodel.setXTarget(target.getX() + OFFSET);
        taskmodel.setYTarget(target.getY() + OFFSET);
        taskmodel.setXTouch(xTouch);
        taskmodel.setYTouch(yTouch);
        taskmodel.setXAcc(x_Acc);
        taskmodel.setYAcc(y_Acc);
        taskmodel.setZAcc(z_Acc);
        taskmodel.setXGravity(x_Gravity);
        taskmodel.setYGravity(y_Gravity);
        taskmodel.setZGravity(z_Gravity);
        taskmodel.setXGyroscope(x_Gyroscope);
        taskmodel.setYGyroscope(y_Gyroscope);
        taskmodel.setZGyroscope(z_Gyroscope);
        taskmodel.setXRotation(x_Rotation);
        taskmodel.setYRotation(y_Rotation);
        taskmodel.setZRotation(z_Rotation);
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


    private void setupUI () {
        setContentView(R.layout.activity_generic_task);
        nextButton = findViewById(R.id.next_btn);
        target = findViewById(R.id.targetBtn);
    }
}
