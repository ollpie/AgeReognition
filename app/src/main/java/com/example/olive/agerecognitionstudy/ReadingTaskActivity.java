package com.example.olive.agerecognitionstudy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ScrollView;

public class ReadingTaskActivity extends AppCompatActivity {

    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_task);
        scrollView = findViewById(R.id.scrollView);

        Log.d("Height", String.valueOf(scrollView.getHeight()));
    }
}
