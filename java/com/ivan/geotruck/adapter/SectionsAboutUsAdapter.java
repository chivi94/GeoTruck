package com.ivan.geotruck.adapter;





import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ivan.geotruck.R;
import com.ivan.geotruck.activities.AboutUsActivity;
import com.ivan.geotruck.fragments.about_us_fragments.AboutGeoTruckDeveloperFragment;
import com.ivan.geotruck.fragments.about_us_fragments.AboutGeoTruckFragment;
import com.ivan.geotruck.fragments.about_us_fragments.HowToUseGeoTruckFragment;

/**
 * Created by Ivan on 18/05/2015.
 */
public class SectionsAboutUsAdapter extends FragmentPagerAdapter {
    private AboutUsActivity aboutUsActivity;

    public SectionsAboutUsAdapter(FragmentManager fm, AboutUsActivity aboutUsActivity) {
        super(fm);
        this.aboutUsActivity = aboutUsActivity;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HowToUseGeoTruckFragment();
                break;
            case 1:
                fragment = new AboutGeoTruckFragment();
                break;
            case 2:
                fragment = new AboutGeoTruckDeveloperFragment();
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence sequence = "";
        switch (position) {
            //Como usar GeoTruck
            case 0:
                sequence = aboutUsActivity.getResources().getString(R.string.title_comoUsar);
                break;
            //Información
            case 1:
                sequence = aboutUsActivity.getResources().getString(R.string.title_informacion);
                break;
            //Acerca del desarrollador
            case 2:
                sequence = aboutUsActivity.getResources().getString(R.string.title_desarrollador);
                break;

        }
        return sequence;
    }
}
