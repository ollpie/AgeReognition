package com.example.olive.agerecognitionstudy;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;

/**
 * Created by olive on 05.02.2018.
 */

public class DialogUtil {
    Context context;

    public DialogUtil(Context context){
        this.context = context;
    }

    public void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new Builder(context);

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

    public void showDBDialog(){
        AlertDialog.Builder builder = new Builder(context);
        builder.setTitle("Datenbank Aktionen");
        builder.setPositiveButton("Exportieren", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                MainActivity.onLogData();
            }
        });
        builder.setNegativeButton("Löschen", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                MainActivity.onClearData();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void showTaskDialog(String manualId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        Resources res = context.getResources();
        String title = res.getString(R.string.pick_activity) + " " + manualId;
        builder.setTitle(title)
                .setItems(R.array.activities, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.onTaskWasChosen(which);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
