package com.ivan.geotruck.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ivan.geotruck.R;
import com.ivan.geotruck.connection.DatabaseConnect;

/**
 * AsyncTask que se encargará de realizar el registro de usuario de forma asíncrona. Mientras realiza esta tarea, notificará al usuario mediante un ProgressDialog.
 */
public class RegisterAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private ProgressDialog progressDialog;
    private Context context;
    private DatabaseConnect connect;
    private String name;
    private String phone;
    private String password;
    private String userName;
    private String email;
    private String business;
    private int type;

    public RegisterAsyncTask(Context context, String name, String phone, String password, String userName, String email, String business, int type) {
        this.context = context;
        //Datos del formulario de registro
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.userName = userName;
        this.email = email;
        this.business = business;
        this.type = type;
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
                progressDialog.setMessage(context.getString(R.string.progressdialog_registro));
                progressDialog.show();
            }
        });
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean userExist = connect.checkUserName(userName);
        boolean emailExist;
        boolean registerSuccessful;
        if (userExist) {
            emailExist = connect.checkUserEmail(email);
            if (emailExist) {
                registerSuccessful = connect.createEmployee(name, phone, userName, password, email, business, type, context);
                return registerSuccessful;
            }
        }
        return false;
    }


    @Override
    protected void onPostExecute(Boolean registerSuccessful) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (registerSuccessful) {
            Toast.makeText(context, context.getString(R.string.toast_registroTerminado), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getString(R.string.toast_registroFallido), Toast.LENGTH_SHORT).show();
        }
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public Context getContext() {
        return context;
    }
}
