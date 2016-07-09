package com.ivan.geotruck.fragments.settings_fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ivan.geotruck.R;
import com.ivan.geotruck.activities.SettingsActivity;
import com.ivan.geotruck.connection.DatabaseConnect;

/**
 * Created by Ivan on 22/05/2015.
 */
public class ChangeUsernameFragment extends Fragment implements View.OnClickListener {

    //Fragments
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Bundle args = null;
    private Bundle newArgs = null;
    private String usuario;
    private SettingsFragment settingsFragment = new SettingsFragment();

    //Componentes
    private EditText et_nuevoUsuario;
    private String newUsername;
    private Button b_confirmar;

    //Conexion con la BBDD
    private DatabaseConnect connect = new DatabaseConnect();

    //Activity padre
    private SettingsActivity parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.ly_change_username_fragment, container, false);

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
        newUsername = et_nuevoUsuario.getText().toString();
        if (!newUsername.equals("")) {
            if (connect.changeUsername(newUsername, usuario)) {
                fragmentTransaction = fragmentManager.beginTransaction();
                newArgs = new Bundle();
                newArgs.putString("Usuario", newUsername);
                settingsFragment.setArguments(newArgs);
                fragmentTransaction.replace(android.R.id.content, settingsFragment);
                parent.newUser = newUsername;
                parent.clearBackStack();
                fragmentTransaction.commit();
            }else{
                Toast.makeText(getActivity(), R.string.toast_cambioUsuarioFallido, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setUpComponents(View rootView) {
        fragmentManager = getFragmentManager();
        et_nuevoUsuario = (EditText) rootView.findViewById(R.id.et_changeUsername);
        b_confirmar = (Button) rootView.findViewById(R.id.b_confirmNewUsername);
        b_confirmar.setOnClickListener(this);
    }

}
