package com.example.olive.agerecognitionstudy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class TestOffsetActivity extends AppCompatActivity {

    private ImageView cross;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_offset);
        cross = findViewById(R.id.testCross);
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();

        switch(eventAction) {
            case MotionEvent.ACTION_DOWN:
                //writeDataIntoLists("Down", event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                //writeDataIntoLists("Move", event.getX(), event.getY());
                Log.d("XPos", String.valueOf(event.getX()));
                cross.setX(event.getRawX());
                cross.setY(event.getRawY());
                break;

            case MotionEvent.ACTION_UP:
                //writeDataIntoLists("Up", event.getX(), event.getY());
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }
}
