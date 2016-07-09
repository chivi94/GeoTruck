package com.ivan.geotruck.activities;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;


import com.ivan.geotruck.R;
import com.ivan.geotruck.activities_funcionality.ActivityFuncionality;
import com.ivan.geotruck.adapter.SectionsMainPagerAdapter;
import com.ivan.geotruck.pager_transformers.DepthPageTransformer;

import java.lang.reflect.Field;


public class MainActivity extends FragmentActivity {

    //Pager adapter.
    private SectionsMainPagerAdapter sectionsMainPagerAdapter;
    //ViewPager--> Se usará para mostrar los elementos
    private ViewPager mainViewPager;
    //PagerTabStrip
    private PagerTabStrip pagerTabStrip;
    //Indice actual
    private static int currentIndex = 0;
    //ActionBar
    private ActionBar actionBar;
    //Elementos para la conectividad
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;
    //ActivityFuncionality
    private ActivityFuncionality activityFuncionality;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityFuncionality = new ActivityFuncionality(this);

        activityFuncionality.setStatusBar();
        actionBar = activityFuncionality.setUpActionBar(actionBar, 1);

        manageWifiNetwork();

        if ((networkInfo != null && networkInfo.isConnectedOrConnecting()) || (wifiInfo != null && wifiManager.isWifiEnabled())) {
            setUpComponents();
        } else {
            showNetworkDialog(wifiManager);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //0 = Zona de logeo(LoginFragment)
        if (mainViewPager != null) {
            mainViewPager.setCurrentItem(currentIndex);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu); //inflate our menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.acercaMainDe:
                //start about activity
                Intent about = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(about);
                break;
        }
        return true;
    }


    //Método que inicia los componentes necesarios para poder comprobar la conexión a internet del dispositivo del usuario.
    private void manageWifiNetwork() {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
    }

    //Método que carga el MainActivity en caso de que el usuario disponga de conexión a Internet.
    private void setUpComponents() {
        setContentView(R.layout.ly_main_activity);
        activityFuncionality.setOverFlowMenu();

        //Creamos el PagerAdapter
        sectionsMainPagerAdapter = new SectionsMainPagerAdapter(getSupportFragmentManager(), this);

        //Enlazamos la instancia del ViewPager con la declarada en el XML
        mainViewPager = (ViewPager) findViewById(R.id.pager);
        mainViewPager.setAdapter(sectionsMainPagerAdapter);
        mainViewPager.setPageTransformer(true, new DepthPageTransformer());
        //Enlazamos el PagerTabStrip con el XML
        pagerTabStrip = (PagerTabStrip) findViewById(R.id.titulos);
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.baseColor));
        mainViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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


    private void showNetworkDialog(final WifiManager wifiManager) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                MainActivity.this);
        builder.setView(getLayoutInflater().inflate(R.layout.ly_custom_dialog_network, null));
        builder.setPositiveButton(getResources().getString(R.string.dialog_possitiveButton),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        wifiManager.setWifiEnabled(true);
                        setUpComponents();
                    }
                });
        builder.setNegativeButton(getResources().getString(R.string.dialog_negativeButton),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel();
                        finish();
                    }
                });
        builder.show();
    }

}
