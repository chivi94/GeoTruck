package com.ivan.geotruck.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ivan.geotruck.R;
import com.ivan.geotruck.activities_funcionality.ActivityFuncionality;
import com.ivan.geotruck.connection.DatabaseConnect;
import com.ivan.geotruck.fragments.position_fragments.EmployeePositionFragment;
import com.parse.ParseGeoPoint;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ivan on 15/05/2015.
 * Este Activity mostrará la ubicación del usuario seleccionado dentro de la lista del Fragment "BossMainFragment"
 */
public class SelectedEmployeePositionActivity extends Activity {

    //Fragments
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Bundle bundle;
    private EmployeePositionFragment employeePositionFragment;
    //Elementos
    private String usuario;
    //ActionBar
    private ActionBar actionBar;
    //ActivityFuncionality
    private ActivityFuncionality activityFuncionality;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityFuncionality = new ActivityFuncionality(this);

        activityFuncionality.setStatusBar();

        activityFuncionality.setOverFlowMenu();

        actionBar = activityFuncionality.setUpActionBar(actionBar, 3);

        String userPosition = getIntent().getStringExtra("Usuario");

        chargeUserPosition(userPosition);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void chargeUserPosition(String userPosition) {
        if (userPosition != null) {
            usuario = userPosition;
            //Llamo al fragment
            fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            bundle = new Bundle();
            bundle.putString("Usuario", usuario);
            employeePositionFragment = new EmployeePositionFragment();
            employeePositionFragment.setArguments(bundle);
            fragmentTransaction.replace(android.R.id.content, employeePositionFragment);
            fragmentTransaction.commit();
        }
    }


}
