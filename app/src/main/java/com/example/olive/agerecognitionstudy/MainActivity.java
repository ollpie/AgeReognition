package com.example.olive.agerecognitionstudy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static DatabaseHandler db;
    private EditText ageEntry;
    private EditText manualIdEntry;
    private RadioButton male;
    private RadioButton female;
    private Button taskButton;
    private DialogUtil dialogUtil;
    public static String currentUserID;
    public static int statusbarOffset;
    public static LatinSquareUtil latinSquareUtil;
    private static Context context;

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
        context = getApplicationContext();
        db = new DatabaseHandler(getApplicationContext());
        db.createDB();

        if (latinSquareUtil == null){
            latinSquareUtil = new LatinSquareUtil();
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void startSession(View view) {
        if (isEmpty(ageEntry) || isEmpty(manualIdEntry) || (!male.isChecked() && !female.isChecked())) {
            dialogUtil.showAlertDialog();
        }else{
            Participant p = new Participant(Integer.parseInt(manualIdEntry.getText().toString()), Integer.parseInt(ageEntry.getText().toString()), checkGender());
            currentUserID = p.getId();
            db.createParticipant(p);
            db.closeDB();
            ageEntry.setText("");
            manualIdEntry.setText("");
            startIntent();
        }
    }

    public void onDBButtonClicked(View view){
        dialogUtil.showDBDialog();
    }

    public static void onLogData() {
        db.exportDB(context);
        Toast.makeText(context,
                "Daten wurden exportiert", Toast.LENGTH_SHORT).show();
    }

    public static void onClearData() {
        db.deleteAllTables();
        Toast.makeText(context,
                "Daten wurden gelöscht", Toast.LENGTH_SHORT).show();
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);
        ageEntry = findViewById(R.id.editTextAge);
        male = findViewById(R.id.radio_btn_male);
        female = findViewById(R.id.radio_btn_female);
        manualIdEntry = findViewById(R.id.editTextID);
        taskButton = findViewById(R.id.taskButton);
        dialogUtil = new DialogUtil(MainActivity.this);
        taskButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUtil.showTaskDialog(db.getLastInsertedManualId());
            }
        });

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

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    public static void onTaskWasChosen(int index){
        latinSquareUtil.setIndex(index);
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

    public static DatabaseHandler getDbHandler() {
        return db;
    }
}
