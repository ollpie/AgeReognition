package com.example.olive.agerecognitionstudy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void btnPressed(View view) {
        Log.d("Hello", "There");
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        int eventaction=event.getAction();

        switch(eventaction) {
            case MotionEvent.ACTION_DOWN:
                Log.d("Hey", "TouchDown");
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }
}
