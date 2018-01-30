package com.example.olive.agerecognitionstudy;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by olive on 24.01.2018.
 */

public class LatinSquareUtil extends AppCompatActivity{

    private int[] indexes = {0, 1, 2, 3};

    private int i = 0;
    private int l = 3;
    private boolean end = false;
    DatabaseHandler database;

    public LatinSquareUtil(){
        database = MainActivity.getDbHandler();
        int checkSum = 0;
        for (int i = 0; i < database.getLatinSquareValues().length; i++){
            checkSum += database.getLatinSquareValues()[i];
        }
        if (checkSum != 0) {
            for (int i = 1; i < database.getLatinSquareValues().length; i++){
                indexes[i-1] = database.getLatinSquareValues()[i];
            }
            i = database.getLatinSquareValues()[0];
        }
        Log.d("Checksum", String.valueOf(checkSum));
    }

    public int getNext(){
        if (end){
            int[] values = new int[5];
            values[0] = i;
            values[1] = indexes[0];
            values[2] = indexes[1];
            values[3] = indexes[2];
            values[4] = indexes[3];
            database.createLatinUtil(values);
            end = false;
            return 8;
        }
        int r = indexes[i];

        if (i != l){
            i++;
        } else {
            i=0;
            int temp = indexes[3];
            indexes[3] = indexes[2];
            indexes[2] = indexes[1];
            indexes[1] = indexes[0];
            indexes[0] = temp;
            end = true;
        }
        return r;
    }
}
