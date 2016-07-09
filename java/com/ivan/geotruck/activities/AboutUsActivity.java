package com.ivan.geotruck.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import com.ivan.geotruck.R;
import com.ivan.geotruck.activities_funcionality.ActivityFuncionality;
import com.ivan.geotruck.adapter.SectionsAboutUsAdapter;
import com.ivan.geotruck.adapter.SectionsMainPagerAdapter;
import com.ivan.geotruck.pager_transformers.ZoomOutPageTransformer;

import java.lang.reflect.Field;

public class AboutUsActivity extends FragmentActivity {

    //Pager adapter.
    private SectionsAboutUsAdapter sectionsAboutUsAdapter;
    //ViewPager--> Se usará para mostrar los elementos
    private ViewPager aboutUsViewPager;
    //PagerTabStrip
    private PagerTabStrip pagerTabStrip;
    //Indice actual
    private static int currentIndex = 0;
    //ActionBar
    private ActionBar actionBar;
    //ActivityFuncionality
    private ActivityFuncionality activityFuncionality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_about_us);
        activityFuncionality = new ActivityFuncionality(this);
        activityFuncionality.setStatusBar();
        //ActionBar
        actionBar = activityFuncionality.setUpActionBar(actionBar, 0);
        //Componentes
        setUpComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //0 = Zona de logeo(LoginFragment)
        if (aboutUsViewPager != null) {
            aboutUsViewPager.setCurrentItem(currentIndex);
        }
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


    private void setUpComponents() {

        //Creamos el PagerAdapter
        sectionsAboutUsAdapter = new SectionsAboutUsAdapter(getSupportFragmentManager(), this);

        //Enlazamos la instancia del ViewPager con la declarada en el XML
        aboutUsViewPager = (ViewPager) findViewById(R.id.aboutPager);
        aboutUsViewPager.setAdapter(sectionsAboutUsAdapter);
        aboutUsViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        //Enlazamos el PagerTabStrip con el XML
        pagerTabStrip = (PagerTabStrip) findViewById(R.id.aboutTitulos);
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.baseColor));
        aboutUsViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                //Do something when page is selected
                currentIndex = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
