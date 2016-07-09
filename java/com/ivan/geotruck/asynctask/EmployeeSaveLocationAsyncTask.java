package com.ivan.geotruck.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.EditText;
import android.widget.Toast;

import com.ivan.geotruck.R;
import com.ivan.geotruck.connection.DatabaseConnect;
import com.ivan.geotruck.fragments.boss_register_fragments.BossFormFragment;
import com.ivan.geotruck.gps_management.GPSTracker;

/**
 * AsyncTask que se encargará de guardar la localización del usuario actual de forma asíncrona.
 */
public class EmployeeSaveLocationAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private Context context;
    private DatabaseConnect connect;
    private double lat;
    private double longitude;
    private String currentEmployee;

    public EmployeeSaveLocationAsyncTask(Context context, double lat, double longitude, String currentEmployee) {
        this.context = context;
        this.lat = lat;
        this.longitude = longitude;
        this.currentEmployee = currentEmployee;
        connect = new DatabaseConnect();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return connect.saveEmployeePosition(lat, longitude, currentEmployee);
    }

    public Context getContext() {
        return context;
    }
}