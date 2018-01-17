package com.example.olive.agerecognitionstudy;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Random;

public class GenericTaskActivity extends AppCompatActivity {

    private static final int MIN = 0;
    private static final int MAX_X = 14;
    private static final int MAX_Y = 25;
    private static final int TOUCH_AMOUNT = 8;
    private static final int OFFSET = 13;
    private static final String TASK_ID = "Generic Task";

    DatabaseHandler database;

    private Button nextButton;
    private ImageButton target;

    private Random randomX;
    private Random randomY;

    private float xTouch = 0.0F;
    private float yTouch = 0.0F;

    private float touchPressure = 0.0F;
    private float touchSize = 0.0F;
    private float touchOrientation = 0.0F;
    private float touchMajor = 0.0F;
    private float touchMinor = 0.0F;

    private int touch_counter = 0;
    private String userID = "";

    private GenericTaskDataModel taskmodel;
    private MotionSensorUtil motionSensorUtil;

    private float xPositions[] = new float[]{30.0F, 42.0F, 74.0F, 102.0F, 134.0F, 166.0F, 198.0F, 230.0F, 262.0F, 294.0F, 326.0F, 358.0F, 390.0F, 422.0F, 454.0F};
    private float yPositions[] = new float[]{30.0F, 55.0F, 100.0F, 145.0F, 190.0F, 235.0F, 280.0F, 325.0F, 370.0F, 415.0F, 460.0F, 505.0F, 550.0F, 595.0F, 640.0F, 685.0F, 735.0F, 780.0F, 825.0F, 870.0F, 915.0F, 960.0F, 1005.0F, 1050.0F, 1095.0F, 1140.0F, 1185.0F};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        userID = MainActivity.currentUserID;
        database = MainActivity.getDbHandler();
        taskmodel = new GenericTaskDataModel(userID);
        motionSensorUtil = new MotionSensorUtil(userID, TASK_ID, (SensorManager) getSystemService(SENSOR_SERVICE));

        randomX = new Random();
        randomY = new Random();

        target.setX(xPositions[randomX.nextInt(((MAX_X - MIN) +1)+MIN)]);
        target.setY(yPositions[randomY.nextInt(((MAX_Y - MIN) +1)+MIN)]);
    }

    public void targetClicked(View view) {
        touch_counter++;

        if (touch_counter > TOUCH_AMOUNT){
            target.setVisibility(View.GONE);
            motionSensorUtil.stop();
            database.createGenericTaskData(taskmodel);
            database.createMotionSensorData(motionSensorUtil.getMotionSensorData());
            database.closeDB();
            nextButton.setVisibility(View.VISIBLE);
        } else {
            target.setX(xPositions[randomX.nextInt(((MAX_X - MIN) +1)+MIN)]);
            target.setY(yPositions[randomY.nextInt(((MAX_Y - MIN) +1)+MIN)]);
        }
    }

    public void nextButtonClicked(View view) {
        Intent intent = new Intent(this, PinTaskActivity.class);
        startActivity(intent);
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        xTouch = event.getX();
        yTouch = event.getY()-MainActivity.statusbarOffset;
        touchPressure = event.getPressure();
        touchSize = event.getSize();
        touchOrientation = event.getOrientation();
        touchMajor = event.getTouchMajor();
        touchMinor = event.getTouchMinor();

        switch(eventAction) {
            case MotionEvent.ACTION_DOWN:
                writeDataIntoLists("Down");
                break;

            case MotionEvent.ACTION_MOVE:
                writeDataIntoLists("Move");
                break;

            case MotionEvent.ACTION_UP:
                writeDataIntoLists("Up");
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    private void writeDataIntoLists(String eventType) {
        taskmodel.setParticipantId(userID);
        taskmodel.setTargetId(touch_counter);
        taskmodel.setTimestamp(System.currentTimeMillis());
        taskmodel.setEventType(eventType);
        taskmodel.setXTarget(target.getX() + OFFSET);
        taskmodel.setYTarget(target.getY() + OFFSET);
        taskmodel.setXTouch(xTouch);
        taskmodel.setYTouch(yTouch);
        taskmodel.setTouchPressure(touchPressure);
        taskmodel.setTouchSize(touchSize);
        taskmodel.setTouchOrientation(touchOrientation);
        taskmodel.setTouchMajor(touchMajor);
        taskmodel.setTouchMinor(touchMinor);
    }

       @Override
       protected void onResume() {
           super.onResume();
           motionSensorUtil.registerListeners();
       }

       @Override
       protected void onPause() {
           super.onPause();
           motionSensorUtil.stop();
       }


    private void setupUI () {
        setContentView(R.layout.activity_generic_task);
        nextButton = findViewById(R.id.next_btn);
        target = findViewById(R.id.targetBtn);
    }
}
