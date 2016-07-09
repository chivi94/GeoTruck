package com.ivan.geotruck.fragments.employee_login_fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ivan.geotruck.R;
import com.ivan.geotruck.asynctask.EmployeeSaveLocationAsyncTask;
import com.ivan.geotruck.gps_management.GPSTracker;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeeMainFragment extends Fragment implements OnClickListener {

    private EditText et_calle;
    private EditText et_piso;
    private EditText et_localidad;
    private Button button_comenzar;
    private View rootView;

    private GPSTracker gpsTracker;

    private String usuario;

    private Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getCurrentLocation();
            handler.postDelayed(runnable, 3000);

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.ly_login_empleado, container, false);
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showAlertDialog(rootView);
        } else {
            setUpComponents(rootView);
        }
        gpsTracker = new GPSTracker(getActivity());
        handler.post(runnable);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        switch (tag) {
            //Comenzar la ruta, si ha especificado destino
            case 1:
                String calle = et_calle.getText().toString();
                String piso = et_piso.getText().toString();
                String localidad = et_localidad.getText().toString();
                if (!localidad.equals("")) {
                    String destino = "";
                    if (piso.equals("")) {
                        destino = calle + "," + localidad;
                    } else if (calle.equals("")) {
                        destino = localidad;
                    } else {
                        destino = calle + "," + piso + "," + localidad;
                    }
                    Uri navigationUri = Uri.parse("google.navigation:q=" + destino);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(getActivity(), R.string.toast_rutaVacia, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void setUpComponents(View rootView) {

        et_calle = (EditText) rootView.findViewById(R.id.editText_calle);
        et_piso = (EditText) rootView.findViewById(R.id.editText_piso);
        et_localidad = (EditText) rootView.findViewById(R.id.editText_localidad);
        button_comenzar = (Button) rootView.findViewById(R.id.button_comenzar);
        button_comenzar = (Button) rootView.findViewById(R.id.button_comenzar);
        button_comenzar.setOnClickListener(this);
        button_comenzar.setTag(1);

        Bundle args = getArguments();
        if (args != null) {
            usuario = args.getString("Usuario");
        }
    }

    private void showAlertDialog(final View rootView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity());
        builder.setView(getActivity().getLayoutInflater().inflate(R.layout.ly_custom_dialog_gps, null));
        builder.setPositiveButton(getResources().getString(R.string.dialog_possitiveButton),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        setUpComponents(rootView);
                    }
                });
        builder.setNegativeButton(getResources().getString(R.string.dialog_negativeButton),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel();
                        getActivity().finish();
                    }
                });
        builder.show();
    }


    private void getCurrentLocation() {
        if (gpsTracker.canGetLocation()) {
            Double latitude = gpsTracker.getLatitude();
            Double longitude = gpsTracker.getLongitude();
            if (latitude != null && longitude != null) {
                EmployeeSaveLocationAsyncTask employeeSaveLocationAsyncTask = new EmployeeSaveLocationAsyncTask(getActivity(), latitude, longitude, usuario);
                employeeSaveLocationAsyncTask.execute();
            }
        }
    }

}
