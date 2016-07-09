package com.ivan.geotruck.fragments.position_fragments;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.ivan.geotruck.activities.SelectedEmployeePositionActivity;
import com.ivan.geotruck.connection.DatabaseConnect;
import com.parse.ParseGeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeePositionFragment extends Fragment {

    private String usuario;
    private Handler handler = new Handler();
    private Runnable runnable;
    private DatabaseConnect connect = new DatabaseConnect();
    //region Elementos del mapa
    private GoogleMap map;
    private MarkerOptions markerOptions;
    private Marker marker;
    private ParseGeoPoint currentPosition;
    private CameraPosition cameraPosition;
    private Geocoder gcd;
    private float minZoom = 20.0f;
    private String markerDriver;
    private String markerLocation;
    private String markerTitle;
    //endregion
    private SelectedEmployeePositionActivity parent;
    //View
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parent = (SelectedEmployeePositionActivity) getActivity();
        usuario = getArguments().get("Usuario").toString();
        if (usuario != null) {
            rootView = inflater.inflate(R.layout.ly_employee_position, container, false);
            setHasOptionsMenu(true);
            gcd = new Geocoder(parent, Locale.getDefault());
            initializeMap();
            if (map != null) {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        getLocation();
                        handler.postDelayed(runnable, 3000);

                    }
                };

                handler.post(runnable);
            } else {
                Toast.makeText(parent, R.string.toast_Aviso, Toast.LENGTH_SHORT).show();
                parent.finish();
            }
        }
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.map_menu, menu); //inflate our menu
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.vista_normal:
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.vista_hibrida:
                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.vista_satelite:
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.vista_terreno:
                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initializeMap() {
        if (map == null) {
            map = ((MapFragment) parent.getFragmentManager()
                    .findFragmentById(R.id.location_map)).getMap();
            UiSettings mapSettings = map.getUiSettings();
            mapSettings.setZoomControlsEnabled(true);
            mapSettings.setZoomGesturesEnabled(true);
            mapSettings.setCompassEnabled(true);
            markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(0, 0));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            marker = map.addMarker(markerOptions);
            marker.showInfoWindow();
        }
    }

    private void drawMarker(ParseGeoPoint currentPosition) {
        marker.setPosition(new LatLng(currentPosition.getLatitude(), currentPosition.getLongitude()));
        setMarkerTitle(currentPosition, marker);
    }

    private void getLocation() {
        //Marcamos la posicion del usuario
        currentPosition = connect.getCurrentEmployeePosition(usuario);
        if (currentPosition != null) {
            drawMarker(currentPosition);
            cameraPosition = new CameraPosition.Builder().target(new LatLng(currentPosition.getLatitude(), currentPosition.getLongitude()))
                    .zoom(map.getCameraPosition().zoom).tilt(30).build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    private void setMarkerTitle(ParseGeoPoint currentPosition, Marker marker) {
        List<Address> addresses = null;
        markerDriver = "";
        markerLocation = "";
        markerTitle = "";
        try {
            addresses = gcd.getFromLocation(currentPosition.getLatitude(), currentPosition.getLongitude(), 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                markerDriver = parent.getString(R.string.driver) + usuario;
                markerLocation = parent.getString(R.string.location) + address.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        markerTitle = markerDriver + "\n" + markerLocation;
        marker.setTitle(markerTitle);
    }


}
