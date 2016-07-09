package com.ivan.geotruck.fragments.settings_fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ivan.geotruck.R;
import com.ivan.geotruck.connection.DatabaseConnect;
import com.ivan.geotruck.password_encrypter.PasswordEncrypter;

public class CheckPasswordFragment extends Fragment implements View.OnClickListener {

    //Fragment
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ChangeUsernameFragment changeUsernameFragment;
    private ChangePasswordFragment changePasswordFragment;
    private ChangeEmailFragment changeEmailFragment;
    private Bundle args = null;
    private Bundle newArgs = null;
    private int cambio;
    private String usuario;
    //Componentes
    private Button b_confirmar;
    private EditText et_confirmPassword;
    private String password;
    private DatabaseConnect connect = new DatabaseConnect();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentManager = getFragmentManager();
        View rootView = inflater.inflate(R.layout.ly_check_pass_fragment, container, false);
        args = getArguments();
        if (args != null) {
            cambio = args.getInt("Cambio", 0);
            usuario = args.getString("Usuario");
            if (cambio != 0 && usuario != null) {
                setUpComponents(rootView);
            }
        }
        return rootView;
    }


    @Override
    public void onClick(View v) {
        fragmentTransaction = fragmentManager.beginTransaction();
        password = et_confirmPassword.getText().toString();
        if (!password.equals("")) {
            switch (cambio) {
                //Cambiar el nombre de usuario
                case 1:
                    if (connect.checkEmployeePassword(usuario, password, getActivity())) {
                        newArgs = new Bundle();
                        newArgs.putString("Usuario", usuario);
                        changeUsernameFragment = new ChangeUsernameFragment();
                        changeUsernameFragment.setArguments(newArgs);
                        fragmentTransaction.add(android.R.id.content, changeUsernameFragment);
                    } else {
                        Toast.makeText(getActivity(), R.string.toast_malPassword, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    if (connect.checkEmployeePassword(usuario, password, getActivity())) {
                        newArgs = new Bundle();
                        newArgs.putString("Usuario", usuario);
                        changePasswordFragment = new ChangePasswordFragment();
                        changePasswordFragment.setArguments(newArgs);
                        fragmentTransaction.add(android.R.id.content, changePasswordFragment);
                    } else {
                        Toast.makeText(getActivity(), R.string.toast_malPassword, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    if (connect.checkEmployeePassword(usuario, password, getActivity())) {
                        newArgs = new Bundle();
                        newArgs.putString("Usuario", usuario);
                        changeEmailFragment = new ChangeEmailFragment();
                        changeEmailFragment.setArguments(newArgs);
                        fragmentTransaction.add(android.R.id.content, changeEmailFragment);
                    } else {
                        Toast.makeText(getActivity(), R.string.toast_malPassword, Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    //Si es cualquier otro número u ocurre algo
                    break;
            }

            fragmentTransaction.addToBackStack("Change " + cambio);
            fragmentTransaction.commit();
        } else {
            Toast.makeText(getActivity(), R.string.toast_vaciaPassword, Toast.LENGTH_SHORT).show();
        }

    }

    private void setUpComponents(View rootView) {
        et_confirmPassword = (EditText) rootView.findViewById(R.id.et_confirmPassword);
        b_confirmar = (Button) rootView.findViewById(R.id.button_confirmarPassword);
        b_confirmar.setOnClickListener(this);
    }
}
