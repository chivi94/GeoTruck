package com.ivan.geotruck.fragments.boss_register_fragments;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.*;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ivan.geotruck.R;
import com.ivan.geotruck.adapter.BusinessListArrayAdapter;
import com.ivan.geotruck.adapter.EmployeeListArrayAdapter;
import com.ivan.geotruck.asynctask.AsyncTaskController;
import com.ivan.geotruck.asynctask.BusinessLoaderAsyncTask;
import com.ivan.geotruck.connection.DatabaseConnect;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExistingBusinessFragment extends Fragment implements OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private ListView lvEmpresas;
    private Button confirmButton;
    private static String currentBussiness = "";
    private BusinessLoaderAsyncTask listViewLoader;

    //Para refrescar la lista
    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler;
    private BusinessListArrayAdapter businessRefreshedListArrayAdapter;

    //En caso de que no haya empresas
    //En caso de que no haya empleados conectados, cuando el usuario refresca
    private ArrayAdapter<String> thereIsNoBusiness;
    private ArrayList<String> noBusinessList = new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.ly_empresa_existente, container, false);

        setUpComponents(rootView);

        return rootView;
    }


    @Override
    public void onClick(View v) {

        if (!currentBussiness.equals("")) {
            //Con esto se puede hacer la transaccion entre Fragment y Fragment
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            BossFormFragment newBossFormFragment = new BossFormFragment();

            final Bundle bundle = new Bundle();
            bundle.putString("Empresa", currentBussiness);
            newBossFormFragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.ly_business, newBossFormFragment);
            fragmentTransaction.commit();
        } else {
            Toast.makeText(getActivity(), R.string.textView_seleccioneEmpresa, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefresh() {
        refreshBusinessList();
    }

    private void setUpComponents(View rootView) {
        lvEmpresas = (ListView) rootView.findViewById(R.id.lv_listaEmpresas);
        lvEmpresas.setVisibility(View.INVISIBLE);
        confirmButton = (Button) rootView.findViewById(R.id.b_siguiente_listaJefe);

        confirmButton.setOnClickListener(this);
        //Cargo la lista con las empresas disponibles
        //Obtenemos las empresas mediante una conexión a la BBDD
        listViewLoader = new BusinessLoaderAsyncTask(getActivity(), lvEmpresas);
        AsyncTaskController runnableAsyncTask = new AsyncTaskController(listViewLoader, listViewLoader.getContext(), listViewLoader.getProgressDialog());
        runnableAsyncTask.start();

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.baseColor, R.color.textBaseColor, R.color.buttonColor);
        swipeRefreshLayout.setOnRefreshListener(this);

        //Selector de la base de datos
        lvEmpresas.setSelector(R.drawable.listview_selector);
        //OnClick de la lista

        lvEmpresas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentBussiness = parent.getItemAtPosition(position).toString();
            }
        });
    }

    private void refreshBusinessList() {
        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                businessRefreshedListArrayAdapter = new DatabaseConnect().catchBusiness(getActivity());
                if (businessRefreshedListArrayAdapter != null) {
                    lvEmpresas.setAdapter(businessRefreshedListArrayAdapter);
                } else {
                    noBusinessList.add(getActivity().getString(R.string.textView_noHayEmpresas));
                    thereIsNoBusiness = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, noBusinessList);
                    lvEmpresas.setAdapter(thereIsNoBusiness);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }
}
