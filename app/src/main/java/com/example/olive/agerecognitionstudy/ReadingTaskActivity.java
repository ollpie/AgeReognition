package com.example.olive.agerecognitionstudy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ScrollView;

public class ReadingTaskActivity extends AppCompatActivity {

    private static final String TASK_ID = "Reading Task";

    ScrollView scrollView;

    private DatabaseHandler database;
    private MotionSensorUtil motionSensorUtil;
    private ReadingTaskDataModel readingTaskModel;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_task);
        scrollView = findViewById(R.id.scrollView);
        userID = MainActivity.currentUserID;
        database = MainActivity.getDbHandler();
        readingTaskModel = new ReadingTaskDataModel();
        motionSensorUtil = new MotionSensorUtil(userID, TASK_ID, (SensorManager) getSystemService(SENSOR_SERVICE));


        Log.d("Height", String.valueOf(scrollView.getHeight()));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            try {
                database.createReadingTaskData(readingTaskModel);
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
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ReadingTaskActivity.this,
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
        super.onPause();
        motionSensorUtil.stop();
    }
}
