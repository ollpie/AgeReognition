package com.example.olive.agerecognitionstudy;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

public class PinTaskActivity extends AppCompatActivity{

    private static final int REPETITIONS = 2;
    private static final String[] PINS = {"0537", "8683"};
    private static final String TABLE_PIN_TASK = "pin_task";
    private static final String TASK_NAME = "Pin Task";

    private GridLayout layout;
    private TextView pinView;

    private int repetitionCount = 0;
    private String receivedDigits = "";
    private int pinIndex = 0;
    private boolean didStartIntent = false;
    private GenericTaskDataModel taskmodel;

    private float xTarget;
    private float yTarget;

    private String currentButtonPress = "";
    private String userID = "";

    DatabaseHandler database;
    private MotionSensorUtil motionSensorUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_task);
        pinView = findViewById(R.id.pin_text_view);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_pin_task, null);
        layout = findViewById(R.id.gridLayout);
        database = MainActivity.getDbHandler();
        taskmodel = new GenericTaskDataModel();
        userID = MainActivity.currentUserID;
        motionSensorUtil = new MotionSensorUtil(userID, TASK_NAME, (SensorManager) getSystemService(SENSOR_SERVICE));
        showAlertDialog();
    }

    public void buttonPressed(View view) {
        Button b = (Button) view;
        //currentButtonPress = (String) b.getText();
        xTarget = (layout.getX() + b.getX() + b.getWidth()/2);
        yTarget = (layout.getY() + b.getY() + b.getHeight()/2);
        //touchPoint.y -= b.getHeight();
        if (b.getText().equals("Delete") && !receivedDigits.equals("")){
            receivedDigits = receivedDigits.substring(0, receivedDigits.length()-1);
            pinView.setText(receivedDigits);
        }
        if (b.getText().equals("Done")) {
            if (receivedDigits.equals(PINS[PINS.length-1]) && repetitionCount == REPETITIONS-1) {
                didStartIntent = true;
                pinView.setText("FERTIG");
                /*database.createGenericTaskData(taskmodel);
                database.createMotionSensorData(motionSensorUtil.getMotionSensorData());
                database.close();*/
                Intent intent = new Intent(this, UnlockActivityTask.class);
                startActivity(intent);
            }
            if (receivedDigits.equals(PINS[pinIndex]) && !didStartIntent){
                repetitionCount++;
                if (repetitionCount == REPETITIONS){
                    repetitionCount = 0;
                    if (pinIndex != PINS.length - 1) {
                        pinIndex++;
                    }
                }
                receivedDigits = "";
                pinView.setText(receivedDigits);
                showAlertDialog();
            }
        }else{
            if(!b.getText().equals("Delete")){
                receivedDigits += b.getText();
                pinView.setText(receivedDigits);
            }
        }
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();

        switch(eventAction) {
            case MotionEvent.ACTION_DOWN:
                writeDataIntoLists("Down", event.getX(), event.getY()-MainActivity.statusbarOffset);
                break;

            case MotionEvent.ACTION_MOVE:
                //writeDataIntoLists("Move", event.getX(), event.getY()-MainActivity.statusbarOffset);
                break;

            case MotionEvent.ACTION_UP:
                writeDataIntoLists("Up", event.getX(), event.getY()-MainActivity.statusbarOffset);
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    private void writeDataIntoLists(String eventType, Float xTouch, Float yTouch) {
        taskmodel.setParticipantId(userID);
        taskmodel.setTargetId(0);
        taskmodel.setTimestamp(System.currentTimeMillis());
        taskmodel.setEventType(eventType);
        taskmodel.setXTarget(xTarget);
        taskmodel.setYTarget(yTarget);
        taskmodel.setXTouch(xTouch);
        taskmodel.setYTouch(yTouch);
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new Builder(this);

        // set title
        alertDialogBuilder.setTitle("Die einzugebene Pin lautet:");

        // set dialog message
        alertDialogBuilder
                .setMessage(PINS[pinIndex])
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //motionSensorUtil.registerListeners();
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        //motionSensorUtil.stop();
    }

}
