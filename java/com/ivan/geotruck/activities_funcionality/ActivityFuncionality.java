package com.ivan.geotruck.activities_funcionality;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import com.ivan.geotruck.R;

import java.lang.reflect.Field;

/**
 * Clase usada para configurar la funcionalidad básica de la aplicación.
 */
public class ActivityFuncionality {

    private Context context;

    public ActivityFuncionality(Context context) {
        this.context = context;
    }

    /**
     * Método que comprueba si la versión del dispositivo es Lollipop o superior, cambio el color de la barra de estado del terminal.
     */
    public void setStatusBar() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Activity activity = (Activity) this.context;
            //Status bar
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(activity.getResources().getColor(R.color.statusBarColor));
        }
    }

    /**
     * Método que establece el menú accesible desde el ActionBar.
     */
    public void setOverFlowMenu() {
        try {
            ViewConfiguration configuration = ViewConfiguration.get(this.context);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(configuration, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Método que hace que el ActionBar del Activity desde el que es llamado se muestre.
     *
     * @param actionBar Recibe un objeto de tipo ActionBar (android.app.ActionBar) para setearlo.
     * @param context   Contexto del Activity desde el que se llama al método.
     */
    public ActionBar setUpActionBar(ActionBar actionBar, int activityType) {
        actionBar = ((Activity) this.context).getActionBar();
        //region Switch para mostrar o no el botón de volver atrás en el actionbar
        switch (activityType) {
            //AboutUs,RegisterForm,SelectedEmployeePosition,Settings
            case 0:
            case 2:
            case 3:
            case 4:
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
                break;
            default:
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setHomeButtonEnabled(false);
                break;
        }
        //endregion
        //region Switch para cambiar el título mostrado en el actionbar
        switch (activityType) {
            //AboutUs
            case 0:
                actionBar.setTitle(R.string.title_activity_about_us);
                break;
            //Main,SplashScreen
            case 1:
            case 5:
                actionBar.setTitle(R.string.app_name);
                break;
            //RegisterForm
            case 2:
                actionBar.setTitle(R.string.title_activity_registro);
                break;
            //SelectedEmployeePosition
            case 3:
                actionBar.setTitle(R.string.title_activity_current_employee_position);
                break;
            //Settings
            case 4:
                actionBar.setTitle(R.string.activity_settings);
                break;
            //User--> No se pasará por aquí porque este activity necesita una configuración específica por el navigation drawer.
            case 6:
                actionBar.setTitle(R.string.title_activity_mainUser);
                break;
            default:
                actionBar.setTitle(R.string.app_name);
                break;
        }
        //endregion

        return actionBar;
    }


}
