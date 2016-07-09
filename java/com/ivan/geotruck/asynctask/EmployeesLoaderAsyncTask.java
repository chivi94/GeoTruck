package com.ivan.geotruck.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ivan.geotruck.R;
import com.ivan.geotruck.adapter.EmployeeListArrayAdapter;
import com.ivan.geotruck.connection.DatabaseConnect;

import java.util.ArrayList;

/**
 * AsyncTask que se encargará de cargar la lista de empleados conectados a la aplicación, de forma asíncrona.
 */
public class EmployeesLoaderAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private ListView lvEmpleados;
    private ProgressDialog progressDialog;
    private Context context;
    private DatabaseConnect connect;
    private EmployeeListArrayAdapter arrayAdapter;
    //En el caso de que no haya empleados conectados
    private ArrayAdapter<String> thereIsNoEmployees;
    private ArrayList<String> noEmployeesList;
    private String usuario;

    public EmployeesLoaderAsyncTask(Context context, ListView lvEmpleados, String usuario) {
        this.context = context;
        this.lvEmpleados = lvEmpleados;
        this.usuario = usuario;
        noEmployeesList = new ArrayList<String>();
        noEmployeesList.add(((Activity) context).getString(R.string.textView_noHayEmpleados));
        thereIsNoEmployees = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, noEmployeesList);
        //Customización del diálogo de carga
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(true);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.progressdialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancel(true);
                progressDialog.dismiss();
            }
        });
        connect = new DatabaseConnect();
    }

    @Override
    protected void onPreExecute() {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage(context.getString(R.string.progressdialog_cargandoEmpleados));
                progressDialog.show();
            }
        });
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        arrayAdapter = connect.catchEmployees(context, usuario);
        if (arrayAdapter != null) {
            return true;
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean thereIsEmployees) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (thereIsEmployees) {
            lvEmpleados.setAdapter(arrayAdapter);
            lvEmpleados.setVisibility(View.VISIBLE);
        } else {
            lvEmpleados.setAdapter(thereIsNoEmployees);
            lvEmpleados.setVisibility(View.VISIBLE);
            lvEmpleados.setOnItemClickListener(null);
        }
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public Context getContext() {
        return context;
    }
}