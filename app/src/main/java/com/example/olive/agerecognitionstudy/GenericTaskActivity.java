package com.example.olive.agerecognitionstudy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

public class GenericTaskActivity extends AppCompatActivity {

    private static final int MIN = 0;
    private static final int OFFSET = 13;
    private static final int X_AMOUNT = 5;
    private static final int Y_AMOUNT = 3;
    private static final int PADDING = 80;
    private static final String TASK_ID = "Generic Task";

    DatabaseHandler database;

    private ImageView target;

    private Random randomX;
    private Random randomY;
    private int randomXValue;
    private int randomYValue;

    private float xTarget = 0.0F;
    private float yTarget = 0.0F;
    private float xTouch = 0.0F;
    private float yTouch = 0.0F;

    private float touchPressure = 0.0F;
    private float touchSize = 0.0F;
    private float touchOrientation = 0.0F;
    private float touchMajor = 0.0F;
    private float touchMinor = 0.0F;
    private long timestamp;

    private int touch_counter = 0;
    private String userID = "";

    private GenericTaskDataModel taskmodel;
    private MotionSensorUtil motionSensorUtil;

    private float xPositions[];
    private float yPositions[];
    private int maxX;
    private int maxY;
    private LatinSquareUtil latinSquareUtil;

    private boolean positionChecker[][] = new boolean[X_AMOUNT][Y_AMOUNT];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        initPositions();
        initPositionChecker();
        latinSquareUtil = MainActivity.latinSquareUtil;
        maxX = xPositions.length-1;
        maxY = yPositions.length-1;
        userID = MainActivity.currentUserID;
        database = MainActivity.getDbHandler();
        taskmodel = new GenericTaskDataModel(userID);
        motionSensorUtil = new MotionSensorUtil(userID, TASK_ID, (SensorManager) getSystemService(SENSOR_SERVICE));

        randomX = new Random();
        randomY = new Random();
        randomXValue = randomX.nextInt(((maxX - MIN) +1)+MIN);
        randomYValue = randomY.nextInt(((maxY - MIN) +1)+MIN);
        target.setX(xPositions[randomXValue]);
        target.setY(yPositions[randomYValue]);
        positionChecker[randomXValue][randomYValue] = true;
        touch_counter++;
        Log.d("Total Positions", String.valueOf(xPositions.length*yPositions.length));
    }

    public void targetClicked() {
        if (didTouchEveryPosition()){
            target.setVisibility(View.GONE);
            motionSensorUtil.stop();
            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.execute();
        } else {
            randomXValue = randomX.nextInt(((maxX - MIN) +1)+MIN);
            randomYValue = randomY.nextInt(((maxY - MIN) +1)+MIN);
            if (positionChecker[randomXValue][randomYValue]){
                do {
                    randomXValue = randomX.nextInt(((maxX - MIN) +1)+MIN);
                    randomYValue = randomY.nextInt(((maxY - MIN) +1)+MIN);
                } while (positionChecker[randomXValue][randomYValue]);
            }
            positionChecker[randomXValue][randomYValue] = true;
            touch_counter++;
            Log.d("Positions", String.valueOf(touch_counter));
            target.setX(xPositions[randomXValue]-OFFSET);
            target.setY(yPositions[randomYValue]-OFFSET);
            Log.d("X, Y", String.valueOf(target.getX()) + ", " + String.valueOf(target.getY()));
        }
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        xTarget = target.getX();
        yTarget = target.getY();
        xTouch = event.getX();
        yTouch = event.getY()-MainActivity.statusbarOffset;
        touchPressure = event.getPressure();
        touchSize = event.getSize();
        touchOrientation = event.getOrientation();
        touchMajor = event.getTouchMajor();
        touchMinor = event.getTouchMinor();
        timestamp = event.getEventTime();
        switch(eventAction) {
            case MotionEvent.ACTION_DOWN:
                writeDataIntoLists("Down");
                break;

            case MotionEvent.ACTION_MOVE:
                writeDataIntoLists("Move");
                break;

            case MotionEvent.ACTION_UP:
                writeDataIntoLists("Up");
                targetClicked();
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    private void writeDataIntoLists(String eventType) {
        taskmodel.setParticipantId(userID);
        taskmodel.setTargetId(touch_counter);
        taskmodel.setTimestamp(timestamp);
        taskmodel.setXTarget(xTarget);
        taskmodel.setYTarget(yTarget);
        taskmodel.setXTouch(xTouch);
        taskmodel.setYTouch(yTouch);
        Log.d("Touches", "X: "+String.valueOf(xTouch)+" y: "+String.valueOf(yTouch) + " Event: "+eventType);
        taskmodel.setTouchPressure(touchPressure);
        taskmodel.setTouchSize(touchSize);
        taskmodel.setTouchOrientation(touchOrientation);
        taskmodel.setTouchMajor(touchMajor);
        taskmodel.setTouchMinor(touchMinor);
        taskmodel.setEventType(eventType);
    }

    private void initPositions(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y-MainActivity.statusbarOffset;
        Log.d("Size", String.valueOf(width) + " x " + String.valueOf(height));
        xPositions = new float[X_AMOUNT];
        yPositions = new float[Y_AMOUNT];
        xPositions[0] = ((width-PADDING*2)/X_AMOUNT)+PADDING;
        yPositions[0] = ((height-PADDING*2)/Y_AMOUNT)+PADDING;
        for (int i = 1; i<X_AMOUNT; i++){
            xPositions[i] = xPositions[i-1] + ((width-PADDING*2)/X_AMOUNT);
            Log.d("X Werte", String.valueOf(xPositions[i]));
        }
        for (int i = 1; i<Y_AMOUNT; i++){
            yPositions[i] = yPositions[i-1] + ((height-PADDING*2)/Y_AMOUNT);
            Log.d("Y Werte", String.valueOf(yPositions[i]));
        }
    }

    private void initPositionChecker() {
        for (int i = 0; i<X_AMOUNT; i++){
            for (int j = 0; j<Y_AMOUNT; j++){
                positionChecker[i][j] = false;
            }
        }
    }

    private boolean didTouchEveryPosition() {
        for (int i = 0; i<X_AMOUNT; i++){
            for (int j = 0; j<Y_AMOUNT; j++){
                if (!positionChecker[i][j]){
                    return false;
                }
            }
        }
        return true;
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
        target = findViewById(R.id.targetView);
    }

    private void startIntent(){
        int activity = latinSquareUtil.getNext();
        Intent intent;
        switch (activity){
            case 0:
                intent = new Intent(this, GenericTaskActivity.class);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(this, PinTaskActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(this, UnlockActivityTask.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(this, ReadingTaskActivity.class);
                startActivity(intent);
                break;
            default:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            try {
                database.createGenericTaskData(taskmodel);
                database.createMotionSensorData(motionSensorUtil.getMotionSensorData());
                database.closeDB();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String params) {
            progressDialog.dismiss();
            startIntent();
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(GenericTaskActivity.this,
                    "Sichere Daten...", "Bitte warten.");
        }
    }
}
