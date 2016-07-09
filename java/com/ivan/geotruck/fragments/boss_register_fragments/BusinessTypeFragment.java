package com.ivan.geotruck.fragments.boss_register_fragments;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ivan.geotruck.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessTypeFragment extends Fragment implements OnClickListener {

    private RadioGroup tipoEmpresa;
    private Button siguiente;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ly_tipo_empresa, container, false);

        setUpComponents(rootView);


        return rootView;
    }

    private void setUpComponents(View rootView) {
        siguiente = (Button) rootView.findViewById(R.id.b_siguiente_jefe);
        siguiente.setOnClickListener(this);

        tipoEmpresa = (RadioGroup) rootView.findViewById(R.id.rg_tipos_empresas);
    }


    @Override
    public void onClick(View v) {
        //Indice del elemento seleccionado
        int id = tipoEmpresa.getCheckedRadioButtonId();

        RadioButton radioButton = (RadioButton) tipoEmpresa.findViewById(id);
        int index = tipoEmpresa.indexOfChild(radioButton);


        //Con esto se puede hacer la transaccion entre Fragment y Fragment

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

        //Le paso el índice seleccionado.
        switch (index) {
            //Para empresa nueva
            case 0:
                NewBusinessFragment newBusinessFragment = new NewBusinessFragment();
                fragmentTransaction.replace(R.id.ly_business, newBusinessFragment);
                break;
            //Para empresa existente
            case 1:
                ExistingBusinessFragment existingBusinessFragment = new ExistingBusinessFragment();
                fragmentTransaction.replace(R.id.ly_business, existingBusinessFragment);
                break;
            default:
                Toast.makeText(getActivity(), R.string.toast_seleccione, Toast.LENGTH_SHORT).show();
                break;
        }
        fragmentTransaction.commit();
    }
}
