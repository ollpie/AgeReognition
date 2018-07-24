package com.example.olive.agerecognitionstudy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class ReadingTaskActivity extends AppCompatActivity {

    private static final String TASK_ID = "Reading Task";
    private static final int[] FIRST_TEXTS = {R.string.ruth, R.string.steinmeier1, R.string.maus_part1};
    private static final int[] SECOND_TEXTS = {R.string.ruth2, R.string.steinmeier2, R.string.maus_part2};
    private static final int[] FIRST_IMAGE = {R.drawable.natur, R.drawable.steinmeier, R.drawable.smdm};
    private static final int[] SECOND_IMAGE = {0,0,R.drawable.smdm_moderatoren};

    ScrollView scrollView;
    Button doneButton;
    TextView textView1;
    TextView textView2;
    ImageView image1;
    ImageView image2;

    private MotionSensorUtil motionSensorUtil;
    private int displayHeight;
    private int clickCounter = 0;

    private ReadingTaskLogic readingTaskLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        readingTaskLogic = new ReadingTaskLogic();
        motionSensorUtil = new MotionSensorUtil(readingTaskLogic.userID, TASK_ID, (SensorManager) getSystemService(SENSOR_SERVICE));
    }

    private void setupUI(){
        setContentView(R.layout.activity_reading_task);
        scrollView = findViewById(R.id.scrollView);
        doneButton = findViewById(R.id.doneButton);
        image1 = findViewById(R.id.readingImage1);
        image2 = findViewById(R.id.readingImage2);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        displayHeight = size.y;
    }

    public void buttonClicked (View view){
        if (readingTaskLogic.trainigsMode){
            motionSensorUtil.registerListeners();
        }
        readingTaskLogic.trainigsMode = false;
        nextText();
    }

    private void nextText(){
        if (clickCounter == FIRST_TEXTS.length){
            ReadingTaskActivity.AsyncTaskRunner asyncTask = new ReadingTaskActivity.AsyncTaskRunner();
            asyncTask.execute();
        }else{
            if(!readingTaskLogic.trainigsMode){
                readingTaskLogic.textCount++;
            }
            scrollView.scrollTo(0,0);
            textView1.setText(getResources().getString(FIRST_TEXTS[clickCounter]));
            textView2.setText(getResources().getString(SECOND_TEXTS[clickCounter]));
            image1.setImageResource(FIRST_IMAGE[clickCounter]);
            image2.setImageResource(SECOND_IMAGE[clickCounter]);
            clickCounter++;
        }
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        readingTaskLogic.handleTouch(event, scrollView, displayHeight);
        return super.dispatchTouchEvent(event);
    }

    private void startIntent(){
        int activity = readingTaskLogic.latinSquareUtil.getNext();
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

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            try {
                readingTaskLogic.writeReadingData(motionSensorUtil.getMotionSensorData());
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
            progressDialog = ProgressDialog.show(ReadingTaskActivity.this,
                    "Sichere Daten...", "Bitte warten.");
        }
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
}
