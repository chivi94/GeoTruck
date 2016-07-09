package com.ivan.geotruck.fragments.settings_fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.ivan.geotruck.R;
import com.ivan.geotruck.activities.SettingsActivity;
import com.ivan.geotruck.connection.DatabaseConnect;

/**
 * Created by Ivan on 25/05/2015.
 */
public class ChangePasswordFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    //Fragments
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Bundle args = null;
    private Bundle newArgs = null;
    private String usuario;
    private SettingsFragment settingsFragment = new SettingsFragment();

    //Componentes
    private EditText et_password;
    private EditText et_confirmPassword;
    private String password;
    private String confirmPassword;
    private Button b_confirmar;
    private CheckBox check_mostrarPassword;

    //Conexion con la BBDD
    private DatabaseConnect connect = new DatabaseConnect();

    //Activity padre
    private SettingsActivity parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.ly_change_password_fragment, container, false);
        args = getArguments();
        if (args != null) {
            usuario = args.getString("Usuario");
            if (usuario != null) {
                parent = ((SettingsActivity) getActivity());
                setUpComponents(rootView);
            }
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        password = et_password.getText().toString();
        confirmPassword = et_confirmPassword.getText().toString();
        if (!password.equals("") &&
                !confirmPassword.equals("")) {
            if (password.equals(confirmPassword)) {
                if (connect.changeUserPassword(usuario, password)) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    newArgs = new Bundle();
                    newArgs.putString("Usuario", usuario);
                    settingsFragment.setArguments(newArgs);
                    fragmentTransaction.replace(android.R.id.content, settingsFragment);
                    parent.clearBackStack();
                    fragmentTransaction.commit();
                }
            }

        }
    }

    private void setUpComponents(View rootView) {
        fragmentManager = getFragmentManager();
        et_password = (EditText) rootView.findViewById(R.id.et_nuevaPassword);
        et_confirmPassword = (EditText) rootView.findViewById(R.id.et_confirmarNuevaPassword);
        b_confirmar = (Button) rootView.findViewById(R.id.b_confirmarNuevaPassword);
        b_confirmar.setOnClickListener(this);
        check_mostrarPassword = (CheckBox) rootView.findViewById(R.id.check_mostrar);
        check_mostrarPassword.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            et_confirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            et_confirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }
}
