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
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PinTaskActivity extends AppCompatActivity{

    private static final int REPETITIONS = 2;
    private static final String[] PINS = {"0537", "8683"};
    private static final String TASK_NAME = "Pin Task";

    private GridLayout layout;
    private TextView pinView;
    private TextView pinDisplay;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button button0;
    private Button buttonDelete;
    private Button buttonDone;

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
    private long timestamp;

    private String userID = "";
    private char actualDigit;
    private char currentDigit;
    private boolean sequenceCorrect = true;
    private int formerSize = 0;

    DatabaseHandler database;
    private MotionSensorUtil motionSensorUtil;
    private LatinSquareUtil latinSquareUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        latinSquareUtil = MainActivity.latinSquareUtil;
        userID = MainActivity.currentUserID;
        database = MainActivity.getDbHandler();
        taskmodel = new PinTaskDataModel(userID);
        motionSensorUtil = new MotionSensorUtil(userID, TASK_NAME, (SensorManager) getSystemService(SENSOR_SERVICE));
        pinDisplay.setText("Pin: " + PINS[pinIndex]);
        currentDigit = PINS[pinIndex].charAt(0);
    }

    OnTouchListener listener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            int eventAction = event.getAction();
            Button b = (Button) view;
            xTarget = (layout.getX() + b.getX() + b.getWidth()/2);
            yTarget = (layout.getY() + b.getY() + b.getHeight()/2);
            xTouch = event.getRawX();
            yTouch = event.getRawY()-MainActivity.statusbarOffset;
            touchPressure = event.getPressure();
            touchSize = event.getSize();
            touchOrientation = event.getOrientation();
            touchMajor = event.getTouchMajor();
            touchMinor = event.getTouchMinor();
            timestamp = event.getEventTime();
            switch(eventAction) {
                case MotionEvent.ACTION_DOWN:
                    processButtonPress(b);
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
            return false;
        }
    };

     private void processButtonPress(Button b) {
        if (!(receivedDigits.length() > PINS[0].length()-1) || b.getText().equals("Delete") || b.getText().equals("Done")){
            if (!b.getText().equals("Done") && !b.getText().equals("Delete")){
                currentDigit = PINS[pinIndex].charAt(currentIndexCount);
            }
            if (b.getText().equals("Delete") && !receivedDigits.equals("")){
                receivedDigits = receivedDigits.substring(0, receivedDigits.length()-1);
                pinView.setText(receivedDigits);
                actualDigit = 'D';
                currentDigit = 'D';
                currentIndexCount--;
                sequenceCorrect = false;
            }
            if (b.getText().equals("Done")) {
                if (receivedDigits.equals(PINS[PINS.length-1]) && repetitionCount == REPETITIONS-1 && sequenceCorrect) {
                    motionSensorUtil.stop();
                    didStartIntent = true;
                    pinView.setText("FERTIG");
                    setSequenceCorrectness();
                    PinTaskActivity.AsyncTaskRunner runner = new PinTaskActivity.AsyncTaskRunner();
                    runner.execute();
                }
                if (receivedDigits.equals(PINS[pinIndex]) && !didStartIntent){
                    if (sequenceCorrect) {
                        repetitionCount++;
                    }
                    if (repetitionCount == REPETITIONS){
                        repetitionCount = 0;
                        if (pinIndex != PINS.length - 1) {
                            pinIndex++;
                        }
                    }
                    currentIndexCount = 0;
                    receivedDigits = "";
                    currentDigit = 'd';
                    actualDigit = 'd';
                    pinView.setText(receivedDigits);
                    pinDisplay.setText("Pin: " + PINS[pinIndex]);
                    setSequenceCorrectness();
                    Toast.makeText(getApplication(),
                            "Eingabe korrekt.", Toast.LENGTH_SHORT).show();
                }else{
                    currentIndexCount = 0;
                    receivedDigits = "";
                    currentDigit = 'd';
                    actualDigit = 'd';
                    pinView.setText(receivedDigits);
                    sequenceCorrect = false;
                    setSequenceCorrectness();
                    Toast.makeText(getApplication(),
                            "Eingabe falsch.", Toast.LENGTH_SHORT).show();
                }
                sequenceCorrect = true;
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

    private void setSequenceCorrectness(){
        int size = taskmodel.pin.size();
        for (int i = 0; i < size-formerSize; i++){
            if(!sequenceCorrect){
                taskmodel.setSequenceCorrect("false");
            }else {
                taskmodel.setSequenceCorrect("true");
            }
        }
        formerSize = size;
    }

    private void writeDataIntoLists(String eventType) {
        taskmodel.setParticipantId(userID);
        taskmodel.setPin(PINS[pinIndex]);
        taskmodel.setEventType(eventType);
        taskmodel.setRepetition(repetitionCount);
        taskmodel.setProgress(receivedDigits);
        taskmodel.setCurrentDigit(currentDigit);
        taskmodel.setActualDigit(actualDigit);
        taskmodel.setxButtonCenter(xTarget);
        taskmodel.setyButtonCenter(yTarget);
        taskmodel.setxTouch(xTouch);
        taskmodel.setyTouch(yTouch);
        taskmodel.setTouchPressure(touchPressure);
        taskmodel.setTouchSize(touchSize);
        taskmodel.setTouchOrientation(touchOrientation);
        taskmodel.setTouchMajor(touchMajor);
        taskmodel.setTouchMinor(touchMinor);
        taskmodel.setTimestamp(timestamp);
    }

    private void setupUI() {
        setContentView(R.layout.activity_pin_task);
        pinView = findViewById(R.id.pin_text_view);
        pinDisplay = findViewById(R.id.pin_display);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        button0 = findViewById(R.id.button0);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonDone = findViewById(R.id.buttonDone);
        button1.setOnTouchListener(listener);
        button2.setOnTouchListener(listener);
        button3.setOnTouchListener(listener);
        button4.setOnTouchListener(listener);
        button5.setOnTouchListener(listener);
        button6.setOnTouchListener(listener);
        button7.setOnTouchListener(listener);
        button8.setOnTouchListener(listener);
        button9.setOnTouchListener(listener);
        button0.setOnTouchListener(listener);
        buttonDone.setOnTouchListener(listener);
        buttonDelete.setOnTouchListener(listener);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_pin_task, null);
        layout = findViewById(R.id.gridLayout);
        database = MainActivity.getDbHandler();
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
                Log.d("Length bf db", String.valueOf(taskmodel.length()));
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
            startIntent();
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
