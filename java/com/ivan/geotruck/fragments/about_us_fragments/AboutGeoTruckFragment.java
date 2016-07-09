package com.ivan.geotruck.fragments.about_us_fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ivan.geotruck.R;

/**
 * Created by Ivan on 18/05/2015.
 */
public class AboutGeoTruckFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ly_geotruck_info, container, false);
        return rootView;
    }
}
