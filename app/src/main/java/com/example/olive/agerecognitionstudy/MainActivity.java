package com.example.olive.agerecognitionstudy;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static DatabaseHandler db;
    private EditText ageEntry;
    private RadioButton male;
    private RadioButton female;
    public static String currentUserID;
    public static int statusbarOffset;
    public static LatinSquareUtil latinSquareUtil;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        verifyStoragePermissions(this);
        db = new DatabaseHandler(getApplicationContext());
        if (latinSquareUtil == null){
            latinSquareUtil = new LatinSquareUtil();
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void startSession(View view) {
        if (ageEntry.getText().equals("") || (!male.isChecked() && !female.isChecked())) {
            showAlertDialog();
        }else{
            Participant p = new Participant(Integer.parseInt(ageEntry.getText().toString()), checkGender());
            currentUserID = p.getId();
            db.createParticipant(p);
            db.closeDB();
            ageEntry.setText("");
            startIntent();
        }
    }

    public void logData(View view) {
        /*MainActivity.AsyncTaskRunner runner = new MainActivity.AsyncTaskRunner();
        runner.execute();*/
        db.exportDB(getApplicationContext());
    }

    public void clearData(View view) {
        db.deleteAllTables();
        Toast.makeText(getApplication(),
                "Tables cleared", Toast.LENGTH_SHORT).show();
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);
        ageEntry = findViewById(R.id.editTextAge);
        male = findViewById(R.id.radio_btn_male);
        female = findViewById(R.id.radio_btn_female);

        // calculate statusbar offset
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusbarOffset = getResources().getDimensionPixelSize(resourceId);
        }
    }

    private String checkGender(){
        if (male.isChecked()) {
            return "Männlich";
        } else {
            return "Weiblich";
        }
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
                break;
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new Builder(this);

        // set title
        alertDialogBuilder.setTitle("Obacht!");

        // set dialog message
        alertDialogBuilder
                .setMessage("Bitte Alter und Geschlecht auswählen.")
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

    public static DatabaseHandler getDbHandler() {
        return db;
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            try {
                LoggingModule loggingModule = new LoggingModule(db.getAllParticipants(),
                        db.getAllGenericTaskData(),
                        db.getAllPinTaskData(),
                        db.getAllUnlockTaskData(),
                        db.getAllReadingTaskData(),
                        db.getMotionSensorData());
                loggingModule.generateParticipantExcelFile();
                loggingModule.generateGenericTaskExcelFile();
                loggingModule.generatePinTaskExcelFile();
                loggingModule.generateUnlockTaskExcelFile();
                loggingModule.generateReadingTaskExcelFile();
                loggingModule.generateMotionSensorExcelFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String params) {
            progressDialog.dismiss();
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Exportiere Daten...", "Bitte warten.");
        }
    }
}
