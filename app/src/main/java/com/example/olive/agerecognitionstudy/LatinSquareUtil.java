package com.example.olive.agerecognitionstudy;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by olive on 24.01.2018.
 */

public class LatinSquareUtil extends AppCompatActivity{

    private int[] indexes = {0, 1, 2, 3};

    int i = 0;
    int l = 3;

    public LatinSquareUtil(){

    }

    public int get(){
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
        }
        return r;
    }
}
