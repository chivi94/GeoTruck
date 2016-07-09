package com.ivan.geotruck.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ivan.geotruck.R;
import com.ivan.geotruck.activities.MainActivity;
import com.ivan.geotruck.fragments.main_fragments.LoginFragment;
import com.ivan.geotruck.fragments.main_fragments.RegisterFragment;

public class SectionsMainPagerAdapter extends FragmentPagerAdapter {

    private MainActivity mainActivity;

    public SectionsMainPagerAdapter(FragmentManager fm, MainActivity mainActivity) {
        super(fm);
        this.mainActivity = mainActivity;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new LoginFragment();
                break;
            case 1:
                fragment = new RegisterFragment();
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence sequence="";
        switch (position) {
            case 0:
                sequence= mainActivity.getResources().getString(R.string.title_login);
            break;
            case 1:
                sequence = mainActivity.getResources().getString(R.string.title_registro);
            break;

        }
        return sequence;
    }
}
