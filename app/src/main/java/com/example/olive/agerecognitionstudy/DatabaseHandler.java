package com.example.olive.agerecognitionstudy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by olive on 04.01.2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "participantsManager";

    // Table Names
    private static final String TABLE_PARTICIPANTS = "participants";
    private static final String TABLE_GENERIC_TASK = "generic_task";
    private static final String TABLE_PIN_TASK = "pin_task";
    private static final String TABLE_UNLOCK_PATTERN_TASK = "unlock_pattern_task";
    private static final String TABLE_READING_TASK = "reading_task";
    private static final String TABLE_MOTION_SENSOR = "motion_sensor";
    private static final String TABLE_LATIN_UTIL = "latin_util";

    private static final String KEY_ID = "key_id";

    // PARTICIPANTS Table - column names
    private static final String KEY_PARTICIPANT_ID = "participant_id";
    private static final String KEY_MANUAL_ID = "manual_id";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";

    // LATIN_UTIL - column names
    private static final String KEY_INDEX = "latin_index";
    private static final String KEY_ARRAY_FIRST = "array_first";
    private static final String KEY_ARRAY_SECOND = "array_second";
    private static final String KEY_ARRAY_THIRD = "array_third";
    private static final String KEY_ARRAY_FOURTH = "array_fourth";

    // GENERIC_TASK Table - column names
    private static final String KEY_TARGET_ID = "target_id";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_EVENT_TYPE = "event_type";

    private static final String KEY_X_TARGET = "x_target";
    private static final String KEY_Y_TARGET = "y_target";

    private static final String KEY_X_TOUCH = "x_touch";
    private static final String KEY_Y_TOUCH = "y_touch";

    private static final String KEY_TOUCH_PRESSURE = "pressure";
    private static final String KEY_TOUCH_SIZE = "size";
    private static final String KEY_ORIENTATION = "orientation";
    private static final String KEY_TOUCH_MAJOR = "major";
    private static final String KEY_TOUCH_MINOR = "minor";

    // PIN_TASK - colun names
    private static final String KEY_PIN = "pin";
    private static final String KEY_CURRENT_DIGIT = "current_digit";
    private static final String KEY_ACTUAL_DIGIT = "actual_digit";
    private static final String KEY_ACTUAL_REPETITION = "actual_repetition";

    // UNLOCK_PATTERN_TASK - column names
    private static final String  KEY_UNLOCK_PATTERN = "unlock_pattern";
    private static final String  KEY_LOGIC_REPETITION = "logic_repetition";
    private static final String  KEY_PROGRESS = "progress";
    private static final String  KEY_SEQUENCE_CORRECTNESS = "sequence_correctness";
    private static final String  KEY_X_BUTTON_CENTER = "x_button_center";
    private static final String  KEY_Y_BUTTON_CENTER = "y_button_center";

    // READING_TASK - column names
    private static final String KEY_X_VIEWPORT = "x_viewport";
    private static final String KEY_Y_VIEWPORT = "y_viewport";
    private static final String KEY_FONT = "text";
    private static final String KEY_FONT_SIZE = "font_size";

    // MOTION_SENSOR_TABLE - column names
    private static final String KEY_X_ACC = "x_acc";
    private static final String KEY_Y_ACC = "y_acc";
    private static final String KEY_Z_ACC = "z_acc";

    private static final String KEY_X_GRAVITY = "x_gravity";
    private static final String KEY_Y_GRAVITY = "y_gravity";
    private static final String KEY_Z_GRAVITY = "z_gravity";

    private static final String KEY_X_GYRO = "x_gyro";
    private static final String KEY_Y_GYRO = "y_gyro";
    private static final String KEY_Z_GYRO = "z_gyro";

    private static final String KEY_X_ROTATION = "x_rotation";
    private static final String KEY_Y_ROTATION = "y_rotation";
    private static final String KEY_Z_ROTATION = "z_rotation";

    private static final String KEY_TASK_ID = "task_id";

    // PIN_TASK Table - column names

    // SLIDE_PIN_TASK

    // READING_TASK

    // Table Create Statements
    // Participants table create statement
    private static final String CREATE_TABLE_PARTICIPANTS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PARTICIPANTS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PARTICIPANT_ID + " TEXT,"
            + KEY_MANUAL_ID + " INTEGER," + KEY_AGE + " INTEGER," + KEY_GENDER + " TEXT)";

    private static final String CREATE_TABLE_LATIN_UTIL = "CREATE TABLE IF NOT EXISTS "
            + TABLE_LATIN_UTIL + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_INDEX + " INTEGER," + KEY_ARRAY_FIRST
            + " INTEGER," + KEY_ARRAY_SECOND + " INTEGER," + KEY_ARRAY_THIRD  + " INTEGER,"
            + KEY_ARRAY_FOURTH + " INTEGER)";

    // Generic task table create statement
    private static final String CREATE_TABLE_GENERIC_TASK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_GENERIC_TASK + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PARTICIPANT_ID + " TEXT," + KEY_TARGET_ID + " INTEGER,"
            + KEY_EVENT_TYPE + " TEXT," + KEY_X_TARGET + " REAL," + KEY_Y_TARGET + " REAL,"
            + KEY_X_TOUCH + " REAL," + KEY_Y_TOUCH + " REAL," + KEY_TOUCH_PRESSURE + " REAL,"
            + KEY_TOUCH_SIZE + " REAL," + KEY_ORIENTATION + " REAL," + KEY_TOUCH_MAJOR + " REAL,"
            + KEY_TOUCH_MINOR + " REAL," + KEY_TIMESTAMP
            + " REAL, FOREIGN KEY (" + KEY_PARTICIPANT_ID + ") REFERENCES " + TABLE_PARTICIPANTS + "(" + KEY_PARTICIPANT_ID + "))";

    private static final String CREATE_TABLE_PIN_TASK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PIN_TASK + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PARTICIPANT_ID + " TEXT,"
            + KEY_PIN + " TEXT," + KEY_EVENT_TYPE + " TEXT," + KEY_ACTUAL_REPETITION + " TEXT," + KEY_LOGIC_REPETITION + " TEXT," + KEY_PROGRESS + " TEXT,"
            + KEY_CURRENT_DIGIT + " TEXT," + KEY_ACTUAL_DIGIT + " TEXT," + KEY_SEQUENCE_CORRECTNESS + " TEXT,"
            + KEY_X_BUTTON_CENTER + " REAL," + KEY_Y_BUTTON_CENTER + " REAL,"
            + KEY_X_TOUCH + " REAL," + KEY_Y_TOUCH + " REAL," + KEY_TOUCH_PRESSURE + " REAL,"
            + KEY_TOUCH_SIZE + " REAL," + KEY_ORIENTATION + " REAL," + KEY_TOUCH_MAJOR + " REAL,"
            + KEY_TOUCH_MINOR + " REAL," + KEY_TIMESTAMP + " REAL, FOREIGN KEY (" + KEY_PARTICIPANT_ID + ") REFERENCES " + TABLE_PARTICIPANTS + "(" + KEY_PARTICIPANT_ID + "))";

    private static final String CREATE_TABLE_UNLOCK_PATTERN_TASK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_UNLOCK_PATTERN_TASK + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PARTICIPANT_ID + " TEXT,"
            + KEY_UNLOCK_PATTERN + " TEXT," + KEY_EVENT_TYPE + " TEXT," + KEY_ACTUAL_REPETITION + " TEXT," + KEY_LOGIC_REPETITION + " TEXT,"
            + KEY_PROGRESS + " TEXT," + KEY_SEQUENCE_CORRECTNESS + " TEXT," + KEY_X_BUTTON_CENTER
            + " REAL," + KEY_Y_BUTTON_CENTER + " REAL," + KEY_X_TOUCH + " REAL," + KEY_Y_TOUCH + " REAL,"
            + KEY_TOUCH_PRESSURE + " REAL," + KEY_TOUCH_SIZE + " REAL," + KEY_ORIENTATION + " REAL,"
            + KEY_TOUCH_MAJOR + " REAL," + KEY_TOUCH_MINOR + " REAL," + KEY_TIMESTAMP + " REAL, FOREIGN KEY (" + KEY_PARTICIPANT_ID + ") REFERENCES " + TABLE_PARTICIPANTS + "(" + KEY_PARTICIPANT_ID + "))";

    private static final String CREATE_TABLE_READING_TASK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_READING_TASK + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PARTICIPANT_ID + " TEXT,"
            + KEY_EVENT_TYPE + " TEXT," + KEY_X_TOUCH + " REAL," + KEY_Y_TOUCH + " REAL,"
            + KEY_X_VIEWPORT + " REAL," + KEY_Y_VIEWPORT + " REAL,"
            + KEY_FONT + " INTEGER," + KEY_FONT_SIZE + " INTEGER," + KEY_TOUCH_PRESSURE + " REAL,"
            + KEY_TOUCH_SIZE + " REAL," + KEY_ORIENTATION + " REAL," + KEY_TOUCH_MAJOR + " REAL,"
            + KEY_TOUCH_MINOR + " REAL," + KEY_TIMESTAMP + " REAL, FOREIGN KEY (" + KEY_PARTICIPANT_ID + ") REFERENCES " + TABLE_PARTICIPANTS + "(" + KEY_PARTICIPANT_ID + "))";

    private static final String CREATE_TABLE_MOTION_SENSOR = "CREATE TABLE IF NOT EXISTS " + TABLE_MOTION_SENSOR +
            "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PARTICIPANT_ID + " TEXT,"
            + KEY_TASK_ID + " TEXT," + KEY_TIMESTAMP + " REAL,"
            + KEY_X_ACC + " REAL," + KEY_Y_ACC + " REAL," + KEY_Z_ACC + " REAL,"
            + KEY_X_GRAVITY + " REAL," + KEY_Y_GRAVITY + " REAL," + KEY_Z_GRAVITY + " REAL,"
            + KEY_X_GYRO + " REAL," + KEY_Y_GYRO + " REAL," + KEY_Z_GYRO + " REAL,"
            + KEY_X_ROTATION + " REAL," + KEY_Y_ROTATION + " REAL," + KEY_Z_ROTATION + " REAL, FOREIGN KEY (" + KEY_PARTICIPANT_ID + ") REFERENCES " + TABLE_PARTICIPANTS + "(" + KEY_PARTICIPANT_ID + "))";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_PARTICIPANTS);
        db.execSQL(CREATE_TABLE_GENERIC_TASK);
        db.execSQL(CREATE_TABLE_PIN_TASK);
        db.execSQL(CREATE_TABLE_UNLOCK_PATTERN_TASK);
        db.execSQL(CREATE_TABLE_READING_TASK);
        db.execSQL(CREATE_TABLE_MOTION_SENSOR);
        db.execSQL(CREATE_TABLE_LATIN_UTIL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GENERIC_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PIN_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNLOCK_PATTERN_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_READING_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOTION_SENSOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LATIN_UTIL);

        // create new tables
        onCreate(db);
    }

    /**
     * Export database to external storage, so it can be accessed from
     / copied
     * to a computer.
     *
     * @return
     */
    public boolean exportDB(Context context) {
        //
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
        File sd = Environment.getExternalStorageDirectory();
        // File sd =
        //
        context.getFilesDir();//this.context.getExternalFilesDir(null);
        // File sd =
        //
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + "com.example.olive.agerecognitionstudy" + "/databases/"
                + DATABASE_NAME;
        String backupDBPath = DATABASE_NAME+".db";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);

        Log.d("RESTORE", backupDB.toString());
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            MediaScannerConnection.scanFile(context, new
                    String[]{backupDB.getAbsolutePath()}, null, null);
            return true;
        } catch (IOException e) {
            e.printStackTrace();

        }
        return false;
    }

    // ------------------------ table methods ----------------//

    /**
     * Creating Participants
     */
    public long createParticipant(Participant participant) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PARTICIPANT_ID, participant.getId());
        values.put(KEY_MANUAL_ID, participant.getManualID());
        values.put(KEY_AGE, participant.getAge());
        values.put(KEY_GENDER, participant.getGender());

        long participant_id = db.insert(TABLE_PARTICIPANTS, null, values);
        return participant_id;

    }

    public void createLatinUtil(int[] latinSquareValues) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_INDEX, latinSquareValues[0]);
        values.put(KEY_ARRAY_FIRST, latinSquareValues[1]);
        values.put(KEY_ARRAY_SECOND, latinSquareValues[2]);
        values.put(KEY_ARRAY_THIRD, latinSquareValues[3]);
        values.put(KEY_ARRAY_FOURTH, latinSquareValues[4]);

        db.insert(TABLE_LATIN_UTIL, null, values);
    }

    public void createGenericTaskData(GenericTaskDataModel taskModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try{
            for (int i = 0; i < taskModel.length(); i++) {
                ContentValues values = new ContentValues();
                values.put(KEY_PARTICIPANT_ID, taskModel.getParticipantId());
                values.put(KEY_TARGET_ID, taskModel.getTargetId().get(i));
                values.put(KEY_EVENT_TYPE, taskModel.getEventType().get(i));
                values.put(KEY_X_TARGET, taskModel.getXTarget().get(i));
                values.put(KEY_Y_TARGET, taskModel.getYTarget().get(i));
                values.put(KEY_X_TOUCH, taskModel.getXTouch().get(i));
                values.put(KEY_Y_TOUCH, taskModel.getYTouch().get(i));
                values.put(KEY_TOUCH_PRESSURE, taskModel.getTouchPressure().get(i));
                values.put(KEY_TOUCH_SIZE, taskModel.getTouchSize().get(i));
                values.put(KEY_ORIENTATION, taskModel.getTouchOrientation().get(i));
                values.put(KEY_TOUCH_MAJOR, taskModel.getTouchMajor().get(i));
                values.put(KEY_TOUCH_MINOR, taskModel.getTouchMinor().get(i));
                values.put(KEY_TIMESTAMP, taskModel.getTimestamp().get(i));
                db.insert(TABLE_GENERIC_TASK, null, values);
            }
            db.setTransactionSuccessful();
        }catch(Exception e){
            Log.e("Error in Transaction",e.toString());
        }finally{
            db.endTransaction();
        }
    }

    public void createPinTaskData(PinTaskDataModel taskModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("Pin Model Length", String.valueOf(taskModel.length()));
        db.beginTransaction();
        try{
            for (int i = 0; i < taskModel.length(); i++) {
                ContentValues values = new ContentValues();
                values.put(KEY_PARTICIPANT_ID, taskModel.getParticipantId());
                values.put(KEY_PIN, taskModel.getPin().get(i));
                values.put(KEY_EVENT_TYPE, taskModel.getEventType().get(i));
                values.put(KEY_LOGIC_REPETITION, taskModel.getLogicRepetition().get(i));
                values.put(KEY_ACTUAL_REPETITION, taskModel.getActualRepetition().get(i));
                values.put(KEY_PROGRESS, taskModel.getProgress().get(i));
                values.put(KEY_CURRENT_DIGIT, taskModel.getCurrentDigit().get(i).toString());
                values.put(KEY_ACTUAL_DIGIT, taskModel.getActualDigit().get(i).toString());
                values.put(KEY_SEQUENCE_CORRECTNESS, taskModel.getSequenceCorrect().get(i));
                values.put(KEY_X_BUTTON_CENTER, taskModel.getxButtonCenter().get(i));
                values.put(KEY_Y_BUTTON_CENTER, taskModel.getyButtonCenter().get(i));
                values.put(KEY_X_TOUCH, taskModel.getxTouch().get(i));
                values.put(KEY_Y_TOUCH, taskModel.getyTouch().get(i));
                values.put(KEY_TOUCH_PRESSURE, taskModel.getTouchPressure().get(i));
                values.put(KEY_TOUCH_SIZE, taskModel.getTouchSize().get(i));
                values.put(KEY_ORIENTATION, taskModel.getTouchOrientation().get(i));
                values.put(KEY_TOUCH_MAJOR, taskModel.getTouchMajor().get(i));
                values.put(KEY_TOUCH_MINOR, taskModel.getTouchMinor().get(i));
                values.put(KEY_TIMESTAMP, taskModel.getTimestamp().get(i));
                db.insert(TABLE_PIN_TASK, null, values);
            }
            db.setTransactionSuccessful();
        }catch(Exception e){
            Log.e("Error in Transaction",e.toString());
        }finally{
            db.endTransaction();
        }
    }

    public void createUnlockTaskData(UnlockTaskDataModel taskModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try{
            for (int i = 0; i < taskModel.length(); i++) {
                ContentValues values = new ContentValues();

                values.put(KEY_PARTICIPANT_ID, taskModel.getParticipantId());
                values.put(KEY_UNLOCK_PATTERN, taskModel.getPattern().get(i));
                values.put(KEY_EVENT_TYPE, taskModel.getEventType().get(i));
                values.put(KEY_LOGIC_REPETITION, taskModel.getLogicRepetition().get(i));
                values.put(KEY_ACTUAL_REPETITION, taskModel.getActualRepetition().get(i));
                values.put(KEY_PROGRESS, taskModel.getProgress().get(i));
                values.put(KEY_SEQUENCE_CORRECTNESS, taskModel.getSequenceCorrect().get(i));
                values.put(KEY_X_BUTTON_CENTER, taskModel.getxButtonCenter().get(i));
                values.put(KEY_Y_BUTTON_CENTER, taskModel.getyButtonCenter().get(i));
                values.put(KEY_X_TOUCH, taskModel.getxTouch().get(i));
                values.put(KEY_Y_TOUCH, taskModel.getyTouch().get(i));
                values.put(KEY_TOUCH_PRESSURE, taskModel.getTouchPressure().get(i));
                values.put(KEY_TOUCH_SIZE, taskModel.getTouchSize().get(i));
                values.put(KEY_ORIENTATION, taskModel.getTouchOrientation().get(i));
                values.put(KEY_TOUCH_MAJOR, taskModel.getTouchMajor().get(i));
                values.put(KEY_TOUCH_MINOR, taskModel.getTouchMinor().get(i));
                values.put(KEY_TIMESTAMP, taskModel.getTimestamp().get(i));

                db.insert(TABLE_UNLOCK_PATTERN_TASK, null, values);
            }
            db.setTransactionSuccessful();
        }catch(Exception e){
            Log.e("Error in Transaction",e.toString());
        }finally{
            db.endTransaction();
        }
    }

    public void createReadingTaskData(ReadingTaskDataModel taskModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try{
            for (int i = 0; i < taskModel.length(); i++) {
                ContentValues values = new ContentValues();

                values.put(KEY_PARTICIPANT_ID, taskModel.getParticipantId());
                values.put(KEY_EVENT_TYPE, taskModel.getEventType().get(i));
                values.put(KEY_X_TOUCH, taskModel.getxTouch().get(i));
                values.put(KEY_Y_TOUCH, taskModel.getyTouch().get(i));
                values.put(KEY_X_VIEWPORT, taskModel.getyViewport().get(i));
                values.put(KEY_Y_VIEWPORT, taskModel.getyViewportBottom().get(i));
                values.put(KEY_FONT, taskModel.getText().get(i));
                values.put(KEY_FONT_SIZE, taskModel.getFontSize().get(i));
                values.put(KEY_TOUCH_PRESSURE, taskModel.getTouchPressure().get(i));
                values.put(KEY_TOUCH_SIZE, taskModel.getTouchSize().get(i));
                values.put(KEY_ORIENTATION, taskModel.getTouchOrientation().get(i));
                values.put(KEY_TOUCH_MAJOR, taskModel.getTouchMajor().get(i));
                values.put(KEY_TOUCH_MINOR, taskModel.getTouchMinor().get(i));
                values.put(KEY_TIMESTAMP, taskModel.getTimestamp().get(i));

                db.insert(TABLE_READING_TASK, null, values);
            }
            db.setTransactionSuccessful();
        }catch(Exception e){
            Log.e("Error in Transaction",e.toString());
        }finally{
            db.endTransaction();
        }
    }

    public void createMotionSensorData(MotionSensorDataModel dataModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try{
            for (int i = 0; i < dataModel.length(); i++) {
                ContentValues values = new ContentValues();
                values.put(KEY_PARTICIPANT_ID, dataModel.getParticipantID());
                values.put(KEY_TASK_ID, dataModel.getTaskID());
                values.put(KEY_TIMESTAMP, dataModel.getTimestamp().get(i));
                values.put(KEY_X_ACC, dataModel.getXAcc().get(i));
                values.put(KEY_Y_ACC, dataModel.getYAcc().get(i));
                values.put(KEY_Z_ACC, dataModel.getZAcc().get(i));
                values.put(KEY_X_GRAVITY, dataModel.getXGravity().get(i));
                values.put(KEY_Y_GRAVITY, dataModel.getYGravity().get(i));
                values.put(KEY_Z_GRAVITY, dataModel.getZGravity().get(i));
                values.put(KEY_X_GYRO, dataModel.getXGyroscope().get(i));
                values.put(KEY_Y_GYRO, dataModel.getYGyroscope().get(i));
                values.put(KEY_Z_GYRO, dataModel.getZGyroscope().get(i));
                values.put(KEY_X_ROTATION, dataModel.getXRotation().get(i));
                values.put(KEY_Y_ROTATION, dataModel.getYRotation().get(i));
                values.put(KEY_Z_ROTATION, dataModel.getZRotation().get(i));

                db.insert(TABLE_MOTION_SENSOR, null, values);
            }
            db.setTransactionSuccessful();
        }catch(Exception e){
            Log.e("Error in Transaction",e.toString());
        }finally{
            db.endTransaction();
        }
    }

    public int[] getLatinSquareValues() {
        int[] values = new int[5];
        String selectQuery = "SELECT * FROM " + TABLE_LATIN_UTIL;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        db.beginTransaction();
        try{
            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    values[0] = c.getInt(c.getColumnIndex(KEY_INDEX));
                    values[1] = c.getInt(c.getColumnIndex(KEY_ARRAY_FIRST));
                    values[2] = c.getInt(c.getColumnIndex(KEY_ARRAY_SECOND));
                    values[3] = c.getInt(c.getColumnIndex(KEY_ARRAY_THIRD));
                    values[4] = c.getInt(c.getColumnIndex(KEY_ARRAY_FOURTH));
                } while (c.moveToNext());
            }
            db.setTransactionSuccessful();
        }catch(Exception e){
            Log.e("Error in Transaction",e.toString());
        }finally{
            db.endTransaction();
        }
        return values;
    }

    public String getLastInsertedManualId(){
        int id = 0;
        String selectQuery = "SELECT * FROM "+ TABLE_PARTICIPANTS +" ORDER BY "+ KEY_ID +" DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()){
            do {
               id = c.getInt(c.getColumnIndex(KEY_MANUAL_ID));
            } while (c.moveToNext());
        }
        return String.valueOf(id);
    }

    // deleting all Tables
    public void deleteAllTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GENERIC_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PIN_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNLOCK_PATTERN_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_READING_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOTION_SENSOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LATIN_UTIL);
        onCreate(db);
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    public void deleteLatinUtilData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_LATIN_UTIL);
    }
}
