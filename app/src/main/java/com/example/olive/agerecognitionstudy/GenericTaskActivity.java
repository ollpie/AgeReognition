package com.example.olive.agerecognitionstudy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GenericTaskActivity extends AppCompatActivity {

    private static final String TASK_ID = "Generic Task";

    private ImageView target;
    private TextView counterDisplay;
    private MotionSensorUtil motionSensorUtil;
    private GenericTapTaskLogic tapDataLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        tapDataLogic = new GenericTapTaskLogic(target, getWindowManager().getDefaultDisplay(), GenericTaskActivity.this);
        motionSensorUtil = new MotionSensorUtil(tapDataLogic.userID, TASK_ID, (SensorManager) getSystemService(SENSOR_SERVICE));
    }

    public void onEndGenericTraining(View view){
        tapDataLogic.endTraining();
        view.setVisibility(View.GONE);
        motionSensorUtil.registerListeners();
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        tapDataLogic.handleTouch(event, target);
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onResume() {
           super.onResume();
    }

    @Override
       protected void onPause() {
       super.onPause();
       motionSensorUtil.stop();
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
        tapDataLogic.windowFocusChanged();
    }


    private void setupUI () {
        setContentView(R.layout.activity_generic_task);
        target = findViewById(R.id.targetView);
        counterDisplay = findViewById(R.id.generic_counter_display);
    }

    public void setCounterDisplay(String text){
        counterDisplay.setText(text);
    }

    private void startIntent(){
        int activity = tapDataLogic.latinSquareUtil.getNext();
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
    public void onBackPressed() {

    }

    public void startAsyncTask(){
        motionSensorUtil.stop();
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            try {
                tapDataLogic.writeGenericTapData(motionSensorUtil.getMotionSensorData());
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
