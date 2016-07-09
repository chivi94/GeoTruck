package com.ivan.geotruck.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.ivan.geotruck.R;
import com.ivan.geotruck.adapter.BusinessListArrayAdapter;
import com.ivan.geotruck.connection.DatabaseConnect;

/**
 * AyncTask que cargará de forma asíncrona las empresas existentes en la base de datos.
 */
public class BusinessLoaderAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private ListView lvEmpresas;
    private ProgressDialog progressDialog;
    private Context context;
    private DatabaseConnect connect;
    private BusinessListArrayAdapter arrayAdapter;


    public BusinessLoaderAsyncTask(Context context, ListView lvEmpresas) {
        this.context = context;
        this.lvEmpresas = lvEmpresas;
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
                progressDialog.setMessage(context.getString(R.string.progressdialog_cargandoEmpresas));
                progressDialog.show();
            }
        });
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        arrayAdapter = connect.catchBusiness(context);
        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        lvEmpresas.setAdapter(arrayAdapter);
        lvEmpresas.setVisibility(View.VISIBLE);
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public Context getContext() {
        return context;
    }
}
