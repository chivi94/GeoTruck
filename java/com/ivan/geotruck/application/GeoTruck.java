package com.ivan.geotruck.application;

import android.app.Application;
import android.content.res.Configuration;
import com.ivan.geotruck.connection.DatabaseConnect;

import java.util.Locale;

public class GeoTruck extends Application {

    public static String sDefSystemLanguage;
    private Locale locale;
    private Configuration config;

    @Override
    public void onCreate() {
        super.onCreate();

        //Especifico la conexión a la base de datos en el contexto de toda la aplicación
        DatabaseConnect.connectToDatabase(this);
        //Lenguaje del sistema
        sDefSystemLanguage = Locale.getDefault().getLanguage();
        //Si es español, cargo la aplicación en español
        if (sDefSystemLanguage.equals("es")) {
            locale = new Locale(sDefSystemLanguage);
            config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            //Si es cualquier otro idioma, la cargo en inglés
        } else {
            locale = new Locale("en");
            config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        sDefSystemLanguage = newConfig.locale.getLanguage();
    }




}