package com.ivan.geotruck.activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ivan.geotruck.activities_funcionality.ActivityFuncionality;
import com.ivan.geotruck.asynctask.AsyncTaskController;
import com.ivan.geotruck.asynctask.LogoutAsyncTask;
import com.ivan.geotruck.navigation_drawer.NavigationDrawerItems;
import com.ivan.geotruck.R;
import com.ivan.geotruck.navigation_drawer.NavigationAdapter;
import com.ivan.geotruck.fragments.boss_login_fragments.BossMainFragment;
import com.ivan.geotruck.fragments.employee_login_fragments.EmployeeMainFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class UserActivity extends FragmentActivity {
    //Fragments
    private Fragment userType;
    public FragmentManager fragmentManager = getFragmentManager();
    private FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    //region Elementos del NavigationDrawer
    private String[] titulos;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView navList;
    private ArrayList<NavigationDrawerItems> navItems;
    private TypedArray navIcons;
    private NavigationAdapter navAdapter;
    private TextView tvUsuarioConectado;
    //endregion
    //Usuario que se est� conectando
    private String usuario;
    private int type;
    //Action Bar
    private ActionBar actionBar;
    //tipo de usuario
    private int tipo;

    //Constante para los ajustes
    private int SETTINGS_ACTIVITY = 1;
    //Posibles datos obtenidos de la zona de ajustes
    private String newUser;

    //ActivityFuncionality
    private ActivityFuncionality activityFuncionality;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTINGS_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                //Comprobamos de nuevo los datos del usuario y si son distintos los cambiamos
                newUser = data.getStringExtra("Nuevo_usuario");
                if (newUser != null
                        && newUser != usuario) {
                    newLogin();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityFuncionality = new ActivityFuncionality(this);

        setContentView(R.layout.ly_main_user);

        activityFuncionality.setStatusBar();


        //region Componentes de navigation drawer
        setUpItems();

        setUpDrawer();

        setUpActionBar();

        //OverFlowMenu
        activityFuncionality.setOverFlowMenu();

        navList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem(position);
            }
        });
        //endregion
        //region Tipo de usuario que inicia sesi�n
        tipo = getIntent().getIntExtra("Tipo", 2);
        usuario = getIntent().getStringExtra("Usuario");

        chargeUI();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.geotruck_menu, menu); //inflate our menu

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                fragmentManager.popBackStack();
                return true;
            case R.id.ajustes:
                //start settings activity
                Intent settings = new Intent(UserActivity.this, SettingsActivity.class);
                if (newUser == null) {
                    settings.putExtra("Usuario", usuario);
                } else {
                    settings.putExtra("Usuario", newUser);
                }
                UserActivity.this.startActivityForResult(settings, SETTINGS_ACTIVITY);
                return true;
            case R.id.acercaDe:
                Intent acercaDe = new Intent(UserActivity.this, AboutUsActivity.class);
                startActivity(acercaDe);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //Que esté en la primera pantalla y el navigation drawer esté abierto
        if (drawerLayout.isDrawerOpen(navList) && fragmentManager.getBackStackEntryCount() == 1) {
            drawerLayout.closeDrawers();
        } else if (fragmentManager.getBackStackEntryCount() == 1) {
            UserActivity.this.moveTaskToBack(true);
        } else if (drawerLayout.isDrawerOpen(navList) && fragmentManager.getBackStackEntryCount() == 2) {
            drawerLayout.closeDrawers();
        } else if (fragmentManager.getBackStackEntryCount() == 2) {
            fragmentManager.popBackStack("UsuarioConectado" + type, 0);
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    //Método que carga un fragment u otro en función del tipo de usuario
    private void chargeUI() {
        switch (tipo) {
            case 0:
                userType = new BossMainFragment();
                type = tipo;
                break;
            case 1:
                userType = new EmployeeMainFragment();
                type = tipo;
                break;
            default:
                Toast.makeText(this, R.string.asyncTastk_cancelled, Toast.LENGTH_SHORT).show();
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putString("Usuario", usuario);
        userType.setArguments(bundle);
        tvUsuarioConectado.setText(usuario);
        if (userType != null) {
            fragmentTransaction.add(R.id.content_frame, userType, "UsuarioConectado" + type);
            fragmentTransaction.addToBackStack("UsuarioConectado" + type);
            fragmentTransaction.commit();
        } else {
            Toast.makeText(this, R.string.asyncTastk_cancelled, Toast.LENGTH_SHORT).show();
            finish();
        }
        //endregion
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int backCount = fragmentManager.getBackStackEntryCount();
                if (backCount == 1 &&
                        !drawerToggle.isDrawerIndicatorEnabled()) {
                    drawerToggle.setDrawerIndicatorEnabled(true);
                    actionBar.setTitle(R.string.title_activity_mainUser);
                }
            }
        });
    }

    //Método que establece el ActionBar
    private void setUpActionBar() {
        actionBar = getActionBar();
        actionBar.setTitle(R.string.title_activity_mainUser);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    //Método que establece el NavigationDrawer
    private void setUpDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.app_name) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                actionBar.setTitle(R.string.drawer_open);
            }

            /** Called when a drawer has settled in a completely closed state. */
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                actionBar.setTitle(R.string.title_activity_mainUser);
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    //Método que establece los elementos del NavigationDrawer
    private void setUpItems() {
        //Drawer Layout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Lista
        navList = (ListView) findViewById(R.id.lv_navigationDrawer);
        //Establecemos header
        navList.addHeaderView(getLayoutInflater().inflate(R.layout.navigation_drawer_header, null));
        tvUsuarioConectado = (TextView) findViewById(R.id.tv_usuarioConectado);
        //Tomamos listado  de imgs desde drawable
        navIcons = getResources().obtainTypedArray(R.array.navigation_icons);
        //Tomamos listado  de titulos desde el string-array de los recursos @string/nav_options
        titulos = getResources().getStringArray(R.array.nav_options);
        //Listado de titulos de barra de navegacion
        navItems = new ArrayList<NavigationDrawerItems>();
        //Agregamos objetos Item_objct al array
        //Inicio
        navItems.add(new NavigationDrawerItems(titulos[0], navIcons.getResourceId(0, -1)));
        //Acerca de
        navItems.add(new NavigationDrawerItems(titulos[1], navIcons.getResourceId(1, -1)));
        //Cerrar sesi�n
        navItems.add(new NavigationDrawerItems(titulos[2], navIcons.getResourceId(2, -1)));
        //Declaramos y seteamos nuestrp adaptador al cual le pasamos el array con los titulos
        navAdapter = new NavigationAdapter(this, navItems);
        navList.setAdapter(navAdapter);
    }

    //Método que depende del elemento del navigation drawer que se seleccione, se hace una cosa u otra
    private void selectedItem(int position) {
        navList.setItemChecked(position, true);
        switch (position) {
            //Inicio
            case 1:
                int backCount = fragmentManager.getBackStackEntryCount();
                if (backCount > 1) {
                    fragmentManager.popBackStack("UsuarioConectado" + type, 0);
                }
                break;
            //Acerca de
            case 2:
                Intent about = new Intent(UserActivity.this, AboutUsActivity.class);
                startActivity(about);
                break;
            //Cerrar sesi�n
            case 3:
                showDialogCloseSession();
                break;
            default:
                break;
        }
        drawerLayout.closeDrawer(navList);
    }

    //Método que muestra un dialogo para cerrar la sesion
    private void showDialogCloseSession() {
        AlertDialog.Builder dialogCloseSession = new AlertDialog.Builder(UserActivity.this);
        dialogCloseSession.setTitle(R.string.dialog_closeSessionTitle);
        dialogCloseSession.setMessage(R.string.dialog_closeSession);
        dialogCloseSession.setCancelable(true);
        dialogCloseSession.setPositiveButton(R.string.dialog_possitiveButton,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clearBackStack();
                        closeSession();
                    }
                });
        dialogCloseSession.setNegativeButton(R.string.dialog_negativeButton,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        dialogCloseSession.show();
    }

    //Método que elimina los datos guardados de la sesion del usuario
    private void closeSession() {
        LogoutAsyncTask logoutAsyncTask = new LogoutAsyncTask(this, usuario);
        AsyncTaskController runnableAsyncTask = new AsyncTaskController(logoutAsyncTask, logoutAsyncTask.getContext(), logoutAsyncTask.getProgressDialog());
        runnableAsyncTask.start();
    }

    //Método que limpia la pila de fragments
    private void clearBackStack() {
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    //Método que inicia de nuevo la sesión, si el usuario ha cambiado alguno de sus datos
    private void newLogin() {
        Intent nuevoInicio = new Intent();
        nuevoInicio.setClass(UserActivity.this, UserActivity.class);
        //El tipo de usuario que inició sesión al principio
        nuevoInicio.putExtra("Tipo", tipo);
        //El nuevo usuario introducido por el usuario actual
        nuevoInicio.putExtra("Usuario", newUser);
        finish();
        startActivity(nuevoInicio);
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return drawerToggle;
    }


}
