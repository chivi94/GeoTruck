package com.ivan.geotruck.activities;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.ivan.geotruck.R;
import com.ivan.geotruck.activities_funcionality.ActivityFuncionality;
import com.ivan.geotruck.fragments.settings_fragments.SettingsFragment;

public class SettingsActivity extends FragmentActivity {


    private ActionBar actionBar;
    private Intent intent;
    private String usuario;

    //Fragments
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private SettingsFragment settingsFragment;

    //Datos de ajustes. Se usarán para comprobar si se ha cambiado la información del usuario
    public static String newUser;

    //ActivityFuncionality
    private ActivityFuncionality activityFuncionality;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityFuncionality = new ActivityFuncionality(this);

        activityFuncionality.setStatusBar();

        intent = getIntent();
        usuario = intent.getStringExtra("Usuario");
        setUpComponents();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (fragmentManager.getBackStackEntryCount() == 1) {
                    if (intent != null) {
                        if (newUser != null && newUser != usuario) {
                            intent.putExtra("Nuevo_usuario", newUser);
                            setResult(RESULT_OK, intent);
                        } else {
                            setResult(RESULT_CANCELED, intent);
                        }
                    }
                    finish();
                    NavUtils.navigateUpFromSameTask(this);
                } else {
                    fragmentManager.popBackStack();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() == 1) {
            finish();
            NavUtils.navigateUpFromSameTask(this);
        }else{
            fragmentManager.popBackStack();
        }
    }

    //Método que limpia la pila de fragments
    public void clearBackStack() {
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private void setUpComponents() {
        if (usuario != null) {
            // setContentView(R.layout.ly_settings_activity);
            actionBar = activityFuncionality.setUpActionBar(actionBar, 4);
            fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            settingsFragment = new SettingsFragment();
            final Bundle args = new Bundle();
            args.putString("Usuario", usuario);
            settingsFragment.setArguments(args);
            fragmentTransaction.replace(android.R.id.content, settingsFragment,"MainSettings");
            fragmentTransaction.addToBackStack("MainSettings");
            fragmentTransaction.commit();
        } else {
            finish();
        }
    }


}
