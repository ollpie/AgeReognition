package com.example.olive.agerecognitionstudy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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

    private static final String KEY_ID = "id";

    // PARTICIPANTS Table - column names
    private static final String KEY_PARTICIPANT_ID = "participant_id";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";

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

    // UNLOCK_PATTERN_TASK - column names
    private static final String  KEY_UNLOCK_PATTERN = "unlock_pattern";
    private static final String  KEY_REPETITION = "repetition";
    private static final String  KEY_PROGRESS = "progress";
    private static final String  KEY_SEQUENCE_CORRECTNESS = "sequence_correctness";
    private static final String  KEY_X_BUTTON_CENTER = "x_button_center";
    private static final String  KEY_Y_BUTTON_CENTER = "y_button_center";

    // READING_TASK - column names
    private static final String KEY_X_VIEWPORT = "x_viewport";
    private static final String KEY_Y_VIEWPORT = "y_viewport";
    private static final String KEY_FONT = "font";
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

    private static final String TASK_ID = "task_id";

    // PIN_TASK Table - column names

    // SLIDE_PIN_TASK

    // READING_TASK

    // Table Create Statements
    // Participants table create statement
    private static final String CREATE_TABLE_PARTICIPANTS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PARTICIPANTS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PARTICIPANT_ID + " TEXT," + KEY_AGE
            + " INTEGER," + KEY_GENDER + " TEXT)";

    //FOREIGN KEY (" + KEY_PARTICIPANT_ID + ") REFERENCES " + TABLE_PARTICIPANTS + "(" + KEY_PARTICIPANT_ID + "),

    // Generic task table create statement
    private static final String CREATE_TABLE_GENERIC_TASK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_GENERIC_TASK + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PARTICIPANT_ID + " TEXT,"
            + KEY_TARGET_ID + " INTEGER," + KEY_EVENT_TYPE + " TEXT," + KEY_X_TARGET + " REAL," + KEY_Y_TARGET + " REAL,"
            + KEY_X_TOUCH + " REAL," + KEY_Y_TOUCH + " REAL," + KEY_TOUCH_PRESSURE + " REAL,"
            + KEY_TOUCH_SIZE + " REAL," + KEY_ORIENTATION + " REAL," + KEY_TOUCH_MAJOR + " REAL,"
            + KEY_TOUCH_MINOR + " REAL," + KEY_TIMESTAMP + " REAL, FOREIGN KEY (" + KEY_PARTICIPANT_ID + ") REFERENCES " + TABLE_PARTICIPANTS + "(" + KEY_PARTICIPANT_ID + "))";

    private static final String CREATE_TABLE_PIN_TASK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PIN_TASK + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PARTICIPANT_ID + " TEXT,"
            + KEY_PIN + " TEXT," + KEY_EVENT_TYPE + " TEXT," + KEY_REPETITION + " Text," + KEY_PROGRESS + " TEXT,"
            + KEY_CURRENT_DIGIT + " TEXT," + KEY_ACTUAL_DIGIT + " TEXT,"
            + KEY_X_BUTTON_CENTER + " REAL," + KEY_Y_BUTTON_CENTER + " REAL,"
            + KEY_X_TOUCH + " REAL," + KEY_Y_TOUCH + " REAL," + KEY_TOUCH_PRESSURE + " REAL,"
            + KEY_TOUCH_SIZE + " REAL," + KEY_ORIENTATION + " REAL," + KEY_TOUCH_MAJOR + " REAL,"
            + KEY_TOUCH_MINOR + " REAL," + KEY_TIMESTAMP + " REAL, FOREIGN KEY (" + KEY_PARTICIPANT_ID + ") REFERENCES " + TABLE_PARTICIPANTS + "(" + KEY_PARTICIPANT_ID + "))";

    private static final String CREATE_TABLE_UNLOCK_PATTERN_TASK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_UNLOCK_PATTERN_TASK + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PARTICIPANT_ID + " TEXT,"
            + KEY_UNLOCK_PATTERN + " TEXT," + KEY_EVENT_TYPE + " TEXT," + KEY_REPETITION + " TEXT,"
            + KEY_PROGRESS + " TEXT," + KEY_SEQUENCE_CORRECTNESS + " TEXT," + KEY_X_BUTTON_CENTER
            + " REAL," + KEY_Y_BUTTON_CENTER + " REAL," + KEY_X_TOUCH + " REAL," + KEY_Y_TOUCH + " REAL,"
            + KEY_TOUCH_PRESSURE + " REAL," + KEY_TOUCH_SIZE + " REAL," + KEY_ORIENTATION + " REAL,"
            + KEY_TOUCH_MAJOR + " REAL," + KEY_TOUCH_MINOR + " REAL," + KEY_TIMESTAMP + " REAL, FOREIGN KEY (" + KEY_PARTICIPANT_ID + ") REFERENCES " + TABLE_PARTICIPANTS + "(" + KEY_PARTICIPANT_ID + "))";

    private static final String CREATE_TABLE_READING_TASK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_READING_TASK + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PARTICIPANT_ID + " TEXT,"
            + KEY_EVENT_TYPE + " TEXT," + KEY_X_TOUCH + " REAL," + KEY_Y_TOUCH + " REAL,"
            + KEY_X_VIEWPORT + " REAL," + KEY_Y_VIEWPORT + " REAL,"
            + KEY_FONT + " TEXT," + KEY_FONT_SIZE + " INTEGER," + KEY_TOUCH_PRESSURE + " REAL,"
            + KEY_TOUCH_SIZE + " REAL," + KEY_ORIENTATION + " REAL," + KEY_TOUCH_MAJOR + " REAL,"
            + KEY_TOUCH_MINOR + " REAL," + KEY_TIMESTAMP + " REAL, FOREIGN KEY (" + KEY_PARTICIPANT_ID + ") REFERENCES " + TABLE_PARTICIPANTS + "(" + KEY_PARTICIPANT_ID + "))";

    private static final String CREATE_TABLE_MOTION_SENSOR = "CREATE TABLE IF NOT EXISTS " + TABLE_MOTION_SENSOR +
            "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PARTICIPANT_ID + " TEXT,"
            + TASK_ID + " TEXT," + KEY_TIMESTAMP + " REAL,"
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

        // create new tables
        onCreate(db);
    }

    // ------------------------ "participants" table methods ----------------//

    /**
     * Creating Participants
     */
    public long createParticipant(Participant participant) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PARTICIPANT_ID, participant.getId());
        values.put(KEY_AGE, participant.getAge());
        values.put(KEY_GENDER, participant.getGender());

        // insert row
        long participant_id = db.insert(TABLE_PARTICIPANTS, null, values);
        Log.d("Create Participant", "participant created");
        return participant_id;

    }

    /*
 * getting all participants
 * */
    public List<Participant> getAllParticipants() {
        List<Participant> participants = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PARTICIPANTS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Participant pt = new Participant();
                pt.setId(c.getString((c.getColumnIndex(KEY_PARTICIPANT_ID))));
                pt.setAge((c.getInt(c.getColumnIndex(KEY_AGE))));
                pt.setGender(c.getString(c.getColumnIndex(KEY_GENDER)));

                // adding to participant list
                participants.add(pt);
            } while (c.moveToNext());
        }

        return participants;
    }

    // deleting all Participants
    public void deleteAllTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GENERIC_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PIN_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNLOCK_PATTERN_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_READING_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOTION_SENSOR);
    }

    public void createGenericTaskData(GenericTaskDataModel taskModel, String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < taskModel.length(); i++) {
            ContentValues values = new ContentValues();

            values.put(KEY_PARTICIPANT_ID, taskModel.getUserId().get(i));
            values.put(KEY_TARGET_ID, taskModel.getTargetId().get(i));
            values.put(KEY_TIMESTAMP, taskModel.getTimestamp().get(i));
            values.put(KEY_EVENT_TYPE, taskModel.getEventType().get(i));

            values.put(KEY_X_TARGET, taskModel.getXTarget().get(i));
            values.put(KEY_Y_TARGET, taskModel.getYTarget().get(i));
            values.put(KEY_X_TOUCH, taskModel.getXTouch().get(i));
            values.put(KEY_Y_TOUCH, taskModel.getYTouch().get(i));

            values.put(KEY_X_ACC, taskModel.getXAcc().get(i));
            values.put(KEY_Y_ACC, taskModel.getYAcc().get(i));
            values.put(KEY_Z_ACC, taskModel.getZAcc().get(i));

            values.put(KEY_X_GRAVITY, taskModel.getXGravity().get(i));
            values.put(KEY_Y_GRAVITY, taskModel.getYGravity().get(i));
            values.put(KEY_Z_GRAVITY, taskModel.getZGravity().get(i));

            values.put(KEY_X_GYRO, taskModel.getXGyroscope().get(i));
            values.put(KEY_Y_GYRO, taskModel.getYGyroscope().get(i));
            values.put(KEY_Z_GYRO, taskModel.getZGyroscope().get(i));

            values.put(KEY_X_ROTATION, taskModel.getXRotation().get(i));
            values.put(KEY_Y_ROTATION, taskModel.getYRotation().get(i));
            values.put(KEY_Z_ROTATION, taskModel.getZRotation().get(i));

            db.insert(table, null, values);
        }
    }

    public GenericTaskDataModel getAllGenericTaskData(String table) {
        GenericTaskDataModel model = new GenericTaskDataModel();
        String selectQuery = "SELECT * FROM " + table;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                model.setUserId(c.getString((c.getColumnIndex(KEY_PARTICIPANT_ID))));
                model.setTargetId(c.getInt((c.getColumnIndex(KEY_TARGET_ID))));
                model.setTimestamp(c.getLong((c.getColumnIndex(KEY_TIMESTAMP))));
                model.setEventType(c.getString((c.getColumnIndex(KEY_EVENT_TYPE))));
                model.setXTarget(c.getFloat((c.getColumnIndex(KEY_X_TARGET))));
                model.setYTarget(c.getFloat((c.getColumnIndex(KEY_Y_TARGET))));
                model.setXTouch(c.getFloat((c.getColumnIndex(KEY_X_TOUCH))));
                model.setYTouch(c.getFloat((c.getColumnIndex(KEY_Y_TOUCH))));
                model.setXAcc(c.getFloat((c.getColumnIndex(KEY_X_ACC))));
                model.setYAcc(c.getFloat((c.getColumnIndex(KEY_Y_ACC))));
                model.setZAcc(c.getFloat((c.getColumnIndex(KEY_Z_ACC))));
                model.setXGravity(c.getFloat((c.getColumnIndex(KEY_X_GRAVITY))));
                model.setYGravity(c.getFloat((c.getColumnIndex(KEY_Y_GRAVITY))));
                model.setZGravity(c.getFloat((c.getColumnIndex(KEY_Z_GRAVITY))));
                model.setXGyroscope(c.getFloat((c.getColumnIndex(KEY_X_GYRO))));
                model.setYGyroscope(c.getFloat((c.getColumnIndex(KEY_Y_GYRO))));
                model.setZGyroscope(c.getFloat((c.getColumnIndex(KEY_Z_GYRO))));
                model.setXRotation(c.getFloat((c.getColumnIndex(KEY_X_ROTATION))));
                model.setYRotation(c.getFloat((c.getColumnIndex(KEY_Y_ROTATION))));
                model.setZRotation(c.getFloat((c.getColumnIndex(KEY_Z_ROTATION))));
            } while (c.moveToNext());
        }

        return model;
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
