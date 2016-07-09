package com.ivan.geotruck.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ivan.geotruck.R;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Establecerá un Timeout para el AsyncTask de tipo <Void,Void,Boolean> que reciba como parámetro.
 */
public class AsyncTaskController extends Thread implements Runnable {

    private AsyncTask<Void, Void, Boolean> task;
    private Context context;
    private ProgressDialog progressDialog;

    public AsyncTaskController(AsyncTask<Void, Void, Boolean> task, Context context, ProgressDialog progressDialog) {
        this.task = task;
        this.context = context;
        this.progressDialog = progressDialog;
    }

    @Override
    public void run() {
        try {
            task.execute();
            task.get(6, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            this.taskTakeTooLong(true);
        } catch (ExecutionException e) {
            this.taskTakeTooLong(true);
        } catch (TimeoutException e) {
            this.taskTakeTooLong(true);
        } catch (CancellationException e) {
            taskCancelled();
        }
    }

    public void taskCancelled() {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
        task.cancel(true);
    }

    public void taskTakeTooLong(final boolean changeMessage) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog.isShowing()) {
                    if (changeMessage) {
                        progressDialog.setMessage(context.getString(R.string.progressdialog_message));
                    }
                }
            }
        });
        try {
            task.get(6, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            this.cancelCheckTimeThread(true);
        } catch (ExecutionException e) {
            this.cancelCheckTimeThread(true);
        } catch (TimeoutException e) {
            this.cancelCheckTimeThread(true);
        } catch (CancellationException e) {
            taskCancelled();
        }
    }

    public void cancelCheckTimeThread(final boolean showMessageError) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (showMessageError) {
                    Toast.makeText(context, R.string.toast_tiempoAgotado, Toast.LENGTH_SHORT).show();
                }
            }
        });
        task.cancel(true);
    }
}
