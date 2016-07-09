package com.ivan.geotruck.fragments.boss_login_fragments;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;


import com.ivan.geotruck.R;
import com.ivan.geotruck.activities.UserActivity;
import com.ivan.geotruck.adapter.EmployeeListArrayAdapter;
import com.ivan.geotruck.asynctask.AsyncTaskController;
import com.ivan.geotruck.asynctask.EmployeesLoaderAsyncTask;
import com.ivan.geotruck.connection.DatabaseConnect;
import com.parse.ParseObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BossMainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    private ListView lvConnectedUsers;
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler;
    private EmployeeListArrayAdapter refreshedEmployeeAdapter;
    private String usuario;
    //Empleado que se usará para mostrar el detalle
    private String currentEmployee;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private EmployeeDetailFragment employeeDetailFragment;
    //En caso de que no haya empleados conectados, cuando el usuario refresca
    private ArrayAdapter<String> thereIsNoEmployees;
    private ArrayList<String> noEmployeesList = new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.ly_login_jefe, container, false);
        Bundle args = getArguments();
        if (args != null) {
            usuario = args.getString("Usuario");
            if (usuario != null) {
                setUpComponents(rootView);

                EmployeesLoaderAsyncTask employeesListViewLoader = new EmployeesLoaderAsyncTask(getActivity(), lvConnectedUsers, usuario);
                AsyncTaskController runnableAsyncTask = new AsyncTaskController(employeesListViewLoader, employeesListViewLoader.getContext(), employeesListViewLoader.getProgressDialog());
                runnableAsyncTask.start();
            }
        }


        return rootView;
    }

    private void setUpComponents(View rootView) {
        lvConnectedUsers = (ListView) rootView.findViewById(R.id.lv_usuarios_conectados);
        lvConnectedUsers.setVisibility(View.INVISIBLE);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.baseColor, R.color.textBaseColor, R.color.buttonColor);
        swipeRefreshLayout.setOnRefreshListener(this);

        lvConnectedUsers.setTextFilterEnabled(true);
        lvConnectedUsers.setOnItemClickListener(this);
    }

    private void refreshEmployeeList() {
        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshedEmployeeAdapter = new DatabaseConnect().catchEmployees(getActivity(), usuario);
                if (refreshedEmployeeAdapter != null) {
                    lvConnectedUsers.setAdapter(refreshedEmployeeAdapter);
                    checkOnItemClick(true);
                } else {
                    noEmployeesList.add(getActivity().getString(R.string.textView_noHayEmpleados));
                    thereIsNoEmployees = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, noEmployeesList);
                    lvConnectedUsers.setAdapter(thereIsNoEmployees);
                    checkOnItemClick(false);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }

    private void checkOnItemClick(boolean check) {
        AdapterView.OnItemClickListener listener = lvConnectedUsers.getOnItemClickListener();
        if (check) {
            if (listener == null) {
                lvConnectedUsers.setOnItemClickListener(BossMainFragment.this);
            }
        } else {
            if (listener != null) {
                lvConnectedUsers.setOnItemClickListener(null);
            }
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.boss_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getActivity().getString(R.string.editText_busqueda));
        searchView.setOnQueryTextListener(this);

    }

    @Override
    public void onRefresh() {
        refreshEmployeeList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LinearLayout padre = (LinearLayout) view;
        LinearLayout contenedor = (LinearLayout) padre.getChildAt(0);
        LinearLayout datosUsuario = (LinearLayout) contenedor.getChildAt(0);
        currentEmployee = ((TextView) datosUsuario.getChildAt(0)).getText().toString();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);

        final Bundle bundle = new Bundle();
        //Hago el split para solo coger el nombre del empleado, y desechar el resto.
        String[] textViewEmpleadoSeleccionado = currentEmployee.split(":");
        bundle.putString("Detalle", textViewEmpleadoSeleccionado[1]);
        bundle.putString("Nombre_Jefe", usuario);
        employeeDetailFragment = new EmployeeDetailFragment();
        employeeDetailFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.content_frame, employeeDetailFragment);
        fragmentTransaction.addToBackStack("DetailFragment");
        fragmentTransaction.commit();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ((EmployeeListArrayAdapter) lvConnectedUsers.getAdapter()).getFilter().filter(newText);
        return false;
    }
}

