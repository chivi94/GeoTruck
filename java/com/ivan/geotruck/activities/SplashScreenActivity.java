package com.ivan.geotruck.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.ivan.geotruck.R;
import com.ivan.geotruck.activities_funcionality.ActivityFuncionality;
import com.ivan.geotruck.shared_preferences.SharedPreferencesConstants;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends Activity {

    //Duración del SplashScreen
    private static final long SPLASH_SCREEN_DELAY = 3000;
    //ActivityFuncionality
    private ActivityFuncionality activityFuncionality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityFuncionality = new ActivityFuncionality(this);

        activityFuncionality.setStatusBar();

        //Orientación
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Ocultación de la barra superior
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.ly_splash_screen);

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Intent mainActivity = new Intent();
                    mainActivity.setClass(SplashScreenActivity.this, MainActivity.class);
                    startActivity(mainActivity);
                    finish();
                }
            };

            //Carga de la aplicación
            Timer timer = new Timer();
            timer.schedule(task, SPLASH_SCREEN_DELAY);
    }
}
