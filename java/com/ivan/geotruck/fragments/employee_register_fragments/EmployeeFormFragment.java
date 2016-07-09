package com.ivan.geotruck.fragments.employee_register_fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.ivan.geotruck.R;
import com.ivan.geotruck.activities.RegisterFormActivity;
import com.ivan.geotruck.adapter.BusinessListArrayAdapter;
import com.ivan.geotruck.adapter.EmployeeListArrayAdapter;
import com.ivan.geotruck.asynctask.AsyncTaskController;
import com.ivan.geotruck.asynctask.BusinessLoaderAsyncTask;
import com.ivan.geotruck.asynctask.RegisterAsyncTask;
import com.ivan.geotruck.connection.DatabaseConnect;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeeFormFragment extends Fragment implements OnClickListener, SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {

    //Elementos del layout del trabajador
    private ListView lv_listaEmpresas;
    private EditText et_nombreTrabajador;
    private EditText et_telefonoTrabajador;
    private EditText et_passwordTrabajador;
    private EditText et_confirmPasswordTrabajador;
    private EditText et_usuarioRegistroTrabajador;
    private EditText et_emailTrabajador;
    private Button b_confirmarTrabajador;
    //String que almacenará la empresa a buscar en la BBDD, para asignar el ID de dicha empresa al empleado que se está registrando en el sistema
    private static String currentBussiness = "";
    //Contexto del activity padre del fragment
    private Context context = (RegisterFormActivity) getActivity();
    //AsyncTask que se encargará del registro
    private RegisterAsyncTask registerAsyncTask;
    //Para refrescar la lista
    private Handler handler;
    protected BusinessListArrayAdapter businessRefreshedListArrayAdapter;
    //En caso de que no haya empresas, cuando el usuario refresca
    private ArrayAdapter<String> thereIsNoBusiness;
    private ArrayList<String> noBusinessList = new ArrayList<String>();
    //Menu
    private Menu optionsMenu;
    //Search
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ly_trabajador, container, false);
        setHasOptionsMenu(true);
        setUpComponents(rootView);


        return rootView;
    }

    @Override
    public void onClick(View v) {
        //Registro del empleado. Tag 1 al botón de su layout
        String workerName = et_nombreTrabajador.getText().toString();
        String workerPhone = et_telefonoTrabajador.getText().toString();
        String workerUserName = et_usuarioRegistroTrabajador.getText().toString();
        String workerPassword = et_passwordTrabajador.getText().toString();
        String workerConfirmPassword = et_confirmPasswordTrabajador.getText().toString();
        String workerEmail = et_emailTrabajador.getText().toString();
        if (!workerName.equals("") && !workerPhone.equals("") && !workerUserName.equals("")
                && !workerPassword.equals("") && !workerConfirmPassword.equals("")
                && !workerEmail.equals("")) {
            boolean passwordValid = isPasswordValid(workerPassword, workerConfirmPassword);
            if (passwordValid) {
                boolean emailValid = isEmailValid(workerEmail);
                if (emailValid) {
                    boolean phoneValid = isPhonevalid(workerPhone);
                    if (phoneValid) {
                        registerAsyncTask = new RegisterAsyncTask(getActivity(), workerName, workerPhone, workerPassword, workerUserName, workerEmail, currentBussiness, 1);
                        AsyncTaskController runnableAsyncTask = new AsyncTaskController(registerAsyncTask, registerAsyncTask.getContext(), registerAsyncTask.getProgressDialog());
                        runnableAsyncTask.start();
                    } else {
                        Toast.makeText((RegisterFormActivity) getActivity(), getResources().getString(R.string.toast_telefonoIncorrecto), Toast.LENGTH_SHORT).show();
                        et_telefonoTrabajador.requestFocus();
                    }
                } else {
                    Toast.makeText((RegisterFormActivity) getActivity(), getResources().getString(R.string.toast_emailIncorrecto), Toast.LENGTH_SHORT).show();
                    et_emailTrabajador.requestFocus();
                }
            } else {
                Toast.makeText((RegisterFormActivity) getActivity(), getResources().getString(R.string.toast_passwordCoinciden), Toast.LENGTH_SHORT).show();
                et_passwordTrabajador.requestFocus();
            }
        } else {
            Toast.makeText((RegisterFormActivity) getActivity(), getResources().getString(R.string.toast_campoVacio), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onRefresh() {
        refreshBusinessList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.optionsMenu = menu;
        inflater.inflate(R.menu.menu_refresh, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getActivity().getString(R.string.editText_busqueda));
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                // Complete with your code
                setRefreshActionButtonState(true);
                refreshBusinessList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpComponents(View rootView) {
        //region Relaciono los elementos del ly_trabajador con el xml
        lv_listaEmpresas = (ListView) rootView.findViewById(R.id.lv_listaEmpresas);
        lv_listaEmpresas.setVisibility(View.INVISIBLE);
        et_nombreTrabajador = (EditText) rootView.findViewById(R.id.et_nombreTrabajador);
        et_telefonoTrabajador = (EditText) rootView.findViewById(R.id.et_telefonoTrabajador);
        et_passwordTrabajador = (EditText) rootView.findViewById(R.id.et_passwordTrabajador);
        et_confirmPasswordTrabajador = (EditText) rootView.findViewById(R.id.et_confirmarPasswordTrabajador);
        et_usuarioRegistroTrabajador = (EditText) rootView.findViewById(R.id.et_usuarioRegistroTrabajador);
        et_emailTrabajador = (EditText) rootView.findViewById(R.id.et_eMailTrabajador);
        b_confirmarTrabajador = (Button) rootView.findViewById(R.id.b_confirmarRegistroTrabajador);

        b_confirmarTrabajador.setTag(1);
        b_confirmarTrabajador.setOnClickListener(this);
        //endregion
        //Cargo la lista con las empresas disponibles
        //Obtenemos las empresas mediante una conexión a la BBDD
        BusinessLoaderAsyncTask listViewLoader = new BusinessLoaderAsyncTask(getActivity(), lv_listaEmpresas);
        AsyncTaskController runnableAsyncTask = new AsyncTaskController(listViewLoader, listViewLoader.getContext(), listViewLoader.getProgressDialog());
        runnableAsyncTask.start();


        //Color de seleccion de la lista
        lv_listaEmpresas.setSelector(R.drawable.listview_selector);
        //OnClick de la lista
        lv_listaEmpresas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentBussiness = parent.getItemAtPosition(position).toString();
            }
        });

        lv_listaEmpresas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    private boolean isPasswordValid(String password, String confirmedPassword) {
        boolean isValid = false;
        if (password.equals(confirmedPassword)) {
            isValid = true;
        }
        return isValid;
    }

    private boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    private boolean isPhonevalid(String phone) {
        boolean isValid = false;
        if (phone.length() >= 9 && phone.length() < 12) {
            isValid = true;
        }
        return isValid;
    }

    private void refreshBusinessList() {
        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                businessRefreshedListArrayAdapter = new DatabaseConnect().catchBusiness(getActivity());
                if (businessRefreshedListArrayAdapter != null) {
                    lv_listaEmpresas.setAdapter(businessRefreshedListArrayAdapter);
                } else {
                    noBusinessList.add(getActivity().getString(R.string.textView_noHayEmpleados));
                    thereIsNoBusiness = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, noBusinessList);
                    lv_listaEmpresas.setAdapter(thereIsNoBusiness);
                }
                setRefreshActionButtonState(false);
            }
        }, 3000);
    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (optionsMenu != null) {
            final MenuItem refreshItem = optionsMenu
                    .findItem(R.id.menu_refresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.ly_actionbar_indeterminate_progress);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ((BusinessListArrayAdapter) lv_listaEmpresas.getAdapter()).getFilter().filter(newText);
        return false;
    }
}
