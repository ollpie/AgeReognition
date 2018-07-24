package com.example.olive.agerecognitionstudy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

//

public class PinTaskActivity extends AppCompatActivity{


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

    static String pinFillerString;

    private PinTaskLogic pinTaskLogic;
    private MotionSensorUtil motionSensorUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        pinTaskLogic = new PinTaskLogic(PinTaskActivity.this);
        motionSensorUtil = new MotionSensorUtil(pinTaskLogic.userID, TASK_NAME, (SensorManager) getSystemService(SENSOR_SERVICE));
    }

    OnTouchListener listener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            pinTaskLogic.handleTouch(view, event, layout.getX(), layout.getY());
            return false;
        }
    };

    public void onEndPinTraining(View view){
        motionSensorUtil.registerListeners();
        view.setVisibility(View.GONE);
        pinTaskLogic.endTraining();
    }

    public void onUpdatePinView(String text) {
        pinView.setText(text);
    }

    public void onUpdatePinDisplay(String text) {
        pinDisplay.setText(text);
    }

    public void startAsyncTask(){
        motionSensorUtil.stop();
        PinTaskActivity.AsyncTaskRunner runner = new PinTaskActivity.AsyncTaskRunner();
        runner.execute();
    }

    public void makeToast(String message){
        Toast.makeText(getApplication(),
                message, Toast.LENGTH_SHORT).show();
    }

    private void setupUI() {
        setContentView(R.layout.activity_pin_task);
        pinView = findViewById(R.id.pin_text_view);
        pinDisplay = findViewById(R.id.pin_display);
        Resources res = PinTaskActivity.this.getResources();
        pinFillerString = res.getString(R.string.pin_counter);
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

    }

    private void startIntent(){
        int activity = pinTaskLogic.latinSquareUtil.getNext();
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onBackPressed() {

    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            try {
                pinTaskLogic.writePinTaskData(motionSensorUtil.getMotionSensorData());
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
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        motionSensorUtil.stop();
    }

}
