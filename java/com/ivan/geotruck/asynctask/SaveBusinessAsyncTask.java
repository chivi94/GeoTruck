package com.ivan.geotruck.asynctask;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import android.widget.Toast;

import com.ivan.geotruck.R;
import com.ivan.geotruck.connection.DatabaseConnect;
import com.ivan.geotruck.fragments.boss_register_fragments.BossFormFragment;

/**
 * AsyncTask que se encargará de realizar el guardado de empresas de forma asíncrona.
 */
public class SaveBusinessAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private ProgressDialog progressDialog;
    private Context context;
    private DatabaseConnect connect;
    private String business;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private BossFormFragment bossFormFragment;


    public SaveBusinessAsyncTask(Context context, String business,
                                 FragmentManager fragmentManager, FragmentTransaction fragmentTransaction) {
        this.context = context;
        //Datos del formulario de registro
        this.business = business;
        //Datos del cambio de fragment
        this.fragmentManager = fragmentManager;
        this.fragmentTransaction = fragmentTransaction;
        bossFormFragment = new BossFormFragment();
        //Customización del diálogo de carga
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(true);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.progressdialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.dismiss();
                cancel(true);
            }
        });
        connect = new DatabaseConnect();
    }

    @Override
    protected void onPreExecute() {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage(context.getString(R.string.progressdialog_saveBusiness));
                progressDialog.show();
            }
        });
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean businessExist = connect.checkBusinessName(business);
        boolean registerSuccessful;
        if (businessExist) {
            registerSuccessful = connect.createBusiness(business);
            return registerSuccessful;
        }
        return false;
    }


    @Override
    protected void onPostExecute(Boolean registerSuccessful) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (registerSuccessful) {
            Toast.makeText(context, R.string.toast_savedBusiness, Toast.LENGTH_SHORT).show();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

            final Bundle bundle = new Bundle();
            bundle.putString("Empresa", business);
            bossFormFragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.ly_business, bossFormFragment);


            fragmentTransaction.commit();
        } else {
            Toast.makeText(context, R.string.toast_wrongBusiness, Toast.LENGTH_SHORT).show();
            cancel(true);
        }
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public Context getContext() {
        return context;
    }
}
