package com.example.olive.agerecognitionstudy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

public class PinTaskActivity extends AppCompatActivity{

    private static final int REPETITIONS = 2;
    private static final String[] PINS = {"0537", "8683"};
    private static final String TASK_NAME = "Pin Task";

    private GridLayout layout;
    private TextView pinView;
    private TextView pinDisplay;

    private int repetitionCount = 0;
    private int currentIndexCount = 0;
    private String receivedDigits = "";
    private int pinIndex = 0;
    private boolean didStartIntent = false;
    private PinTaskDataModel taskmodel;

    private float xTarget;
    private float yTarget;

    private float xTouch = 0.0F;
    private float yTouch = 0.0F;

    private float touchPressure = 0.0F;
    private float touchSize = 0.0F;
    private float touchOrientation = 0.0F;
    private float touchMajor = 0.0F;
    private float touchMinor = 0.0F;

    private String currentButtonPress = "";
    private String userID = "";
    private char actualDigit;
    private char currentDigit;

    DatabaseHandler database;
    private MotionSensorUtil motionSensorUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_task);
        pinView = findViewById(R.id.pin_text_view);
        pinDisplay = findViewById(R.id.pin_display);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_pin_task, null);
        layout = findViewById(R.id.gridLayout);
        database = MainActivity.getDbHandler();
        taskmodel = new PinTaskDataModel(userID);
        userID = MainActivity.currentUserID;
        motionSensorUtil = new MotionSensorUtil(userID, TASK_NAME, (SensorManager) getSystemService(SENSOR_SERVICE));
        pinDisplay.setText("Pin: " + PINS[pinIndex]);
        currentDigit = PINS[pinIndex].charAt(0);
    }

    public void buttonPressed(View view) {
        Button b = (Button) view;
        xTarget = (layout.getX() + b.getX() + b.getWidth()/2);
        yTarget = (layout.getY() + b.getY() + b.getHeight()/2);

        if (!(receivedDigits.length() > PINS[0].length()-1) || b.getText().equals("Delete") || b.getText().equals("Done")){
            if (!b.getText().equals("Done") && !b.getText().equals("Delete")){
                currentDigit = PINS[pinIndex].charAt(currentIndexCount);
            }

            if (b.getText().equals("Delete") && !receivedDigits.equals("")){
                receivedDigits = receivedDigits.substring(0, receivedDigits.length()-1);
                pinView.setText(receivedDigits);
                currentIndexCount--;
            }
            if (b.getText().equals("Done")) {
                if (receivedDigits.equals(PINS[PINS.length-1]) && repetitionCount == REPETITIONS-1) {
                    motionSensorUtil.stop();
                    didStartIntent = true;
                    pinView.setText("FERTIG");
                    Intent intent = new Intent(this, UnlockActivityTask.class);
                    PinTaskActivity.AsyncTaskRunner runner = new PinTaskActivity.AsyncTaskRunner();
                    runner.execute();
                }
                if (receivedDigits.equals(PINS[pinIndex]) && !didStartIntent){
                    repetitionCount++;
                    if (repetitionCount == REPETITIONS){
                        repetitionCount = 0;
                        if (pinIndex != PINS.length - 1) {
                            pinIndex++;
                        }
                    }
                    currentIndexCount = 0;
                    receivedDigits = "";
                    pinView.setText(receivedDigits);
                    pinDisplay.setText("Pin: " + PINS[pinIndex]);
                }
            }else{
                if(!b.getText().equals("Delete") && !b.getText().equals("Done")){
                    currentIndexCount++;
                    receivedDigits += b.getText();
                    actualDigit = b.getText().charAt(0);
                    pinView.setText(receivedDigits);
                }
            }
        }
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
                Log.d("Pin", PINS[pinIndex]);
                Log.d("Repetition", String.valueOf(repetitionCount));
                Log.d("Progress", receivedDigits);
                Log.d("Current Digit", String.valueOf(currentDigit));
                Log.d("Actual Digit", String.valueOf(actualDigit));
                Log.d("-------", "----------------------------------");
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
        taskmodel.setPin(PINS[pinIndex]);
        taskmodel.setEventType(eventType);
        taskmodel.setRepetition(repetitionCount);
        taskmodel.setProgress(receivedDigits);
        taskmodel.setCurrentDigit(currentDigit);
        taskmodel.setActualDigit(actualDigit);
        taskmodel.setSequenceCorrect("true");
        taskmodel.setxButtonCenter(xTarget);
        taskmodel.setyButtonCenter(yTarget);
        taskmodel.setxTouch(xTouch);
        taskmodel.setyTouch(yTouch);
        taskmodel.setTouchPressure(touchPressure);
        taskmodel.setTouchSize(touchSize);
        taskmodel.setTouchOrientation(touchOrientation);
        taskmodel.setTouchMajor(touchMajor);
        taskmodel.setTouchMinor(touchMinor);
        taskmodel.setTimestamp(System.currentTimeMillis());
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            try {
                database.createPinTaskData(taskmodel);
                database.createMotionSensorData(motionSensorUtil.getMotionSensorData());
                database.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String params) {
            progressDialog.dismiss();

            //startActivity(intent);
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(PinTaskActivity.this,
                    "Sichere Daten...", "Bitte warten.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        motionSensorUtil.registerListeners();
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        motionSensorUtil.stop();
    }

}
