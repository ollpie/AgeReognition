package com.example.olive.agerecognitionstudy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class ReadingTaskActivity extends AppCompatActivity {

    private static final String TASK_ID = "Reading Task";
    private static final String[] FONTS = {"arial.ttf", "times.ttf", "segoesc.ttf"};
    private static final int[] FONT_SIZES = {18, 14, 10};

    ScrollView scrollView;
    Button doneButton;
    TextView textView;

    private DatabaseHandler database;
    private MotionSensorUtil motionSensorUtil;
    private ReadingTaskDataModel readingTaskModel;
    private String userID;

    private float yViewPort;
    private float yViewPortBottom;
    private float xTouch = 0.0F;
    private float yTouch = 0.0F;
    private float touchPressure = 0.0F;
    private float touchSize = 0.0F;
    private float touchOrientation = 0.0F;
    private float touchMajor = 0.0F;
    private float touchMinor = 0.0F;
    private long timestamp;

    private int displayHeight;
    private int fontIndex = 0;
    private int fontSizeIndex = 0;
    private boolean end = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        textView.setTextSize(FONT_SIZES[fontSizeIndex]);
        userID = MainActivity.currentUserID;
        database = MainActivity.getDbHandler();
        readingTaskModel = new ReadingTaskDataModel(userID);
        motionSensorUtil = new MotionSensorUtil(userID, TASK_ID, (SensorManager) getSystemService(SENSOR_SERVICE));
    }

    private void setupUI(){
        setContentView(R.layout.activity_reading_task);
        scrollView = findViewById(R.id.scrollView);
        doneButton = findViewById(R.id.doneButton);
        textView = findViewById(R.id.textView);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/"+FONTS[fontIndex]);
        textView.setTypeface(typeface);
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
        setFontSize();
    }

    private void setFontSize(){
        if (fontSizeIndex < FONT_SIZES.length-1){
            fontSizeIndex++;
        }
        scrollView.scrollTo(0,0);
        textView.setTextSize(FONT_SIZES[fontSizeIndex]);
        Typeface tf = Typeface.createFromAsset(getAssets(), "font/"+FONTS[fontIndex]);
        textView.setTypeface(tf);
        if (end){
            ReadingTaskActivity.AsyncTaskRunner asyncTask = new ReadingTaskActivity.AsyncTaskRunner();
            asyncTask.execute();
        }
        if (fontSizeIndex == FONT_SIZES.length-1){
            end = true;
        }
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        yViewPort = scrollView.getScrollY();
        yViewPortBottom = yViewPort + displayHeight - MainActivity.statusbarOffset;
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
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    private void writeDataIntoLists(String eventType) {
        readingTaskModel.setParticipantId(userID);
        readingTaskModel.setEventType(eventType);
        readingTaskModel.setyViewportTop(yViewPort);
        readingTaskModel.setyViewportBottom(yViewPortBottom);
        readingTaskModel.setFont(FONTS[fontIndex]);
        readingTaskModel.setFontSize(FONT_SIZES[fontSizeIndex]);
        readingTaskModel.setxTouch(xTouch);
        readingTaskModel.setyTouch(yTouch);
        readingTaskModel.setTouchPressure(touchPressure);
        readingTaskModel.setTouchSize(touchSize);
        readingTaskModel.setTouchOrientation(touchOrientation);
        readingTaskModel.setTouchMajor(touchMajor);
        readingTaskModel.setTouchMinor(touchMinor);
        readingTaskModel.setTimestamp(timestamp);
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
