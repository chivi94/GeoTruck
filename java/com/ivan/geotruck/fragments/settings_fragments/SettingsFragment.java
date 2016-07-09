package com.ivan.geotruck.fragments.settings_fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ivan.geotruck.R;
import com.ivan.geotruck.connection.DatabaseConnect;
import com.ivan.geotruck.password_encrypter.PasswordEncrypter;
import com.parse.ParseObject;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Ivan on 22/05/2015.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {

    private DatabaseConnect connect = new DatabaseConnect();
    private ParseObject currentEmployee = null;
    private String currentBusiness = null;

    private String usuario;
    //Componentes
    //Username
    private TextView tv_cambioUsername;
    private TextView tv_currentUsername;
    //Password
    private TextView tv_cambioPassword;
    private TextView tv_currentPassword;
    //Email
    private TextView tv_cambioEmail;
    private TextView tv_currentEmail;
    //Empresa
    private TextView tv_business;
    //Registrado
    private TextView tv_registered;
    //Fragments
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private final Bundle args = new Bundle();
    private CheckPasswordFragment checkPasswordFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.ly_settings_activity, container, false);
        Bundle args = getArguments();
        if (args != null) {
            usuario = args.getString("Usuario");
            if (usuario != null) {
                setUpComponents(rootView);
            }
        }

        return rootView;
    }


    //Método que asigna los componentes con su XML correspondiente
    private void setUpComponents(View rootView) {
        currentEmployee = connect.catchEmployeeInfo(usuario);
        if (currentEmployee != null) {
            //Gestión de fragments
            fragmentManager = getFragmentManager();
            //Username
            tv_cambioUsername = (TextView) rootView.findViewById(R.id.tv_cambiarUsuario);
            tv_cambioUsername.setTag(1);
            tv_cambioUsername.setOnClickListener(this);

            tv_currentUsername = (TextView) rootView.findViewById(R.id.tv_usuarioActual);
            tv_currentUsername.setText(tv_currentUsername.getText() + " " + usuario);
            //Password
            tv_cambioPassword = (TextView) rootView.findViewById(R.id.tv_cambiarPassword);
            tv_cambioPassword.setTag(2);
            tv_cambioPassword.setOnClickListener(this);

            tv_currentPassword = (TextView) rootView.findViewById(R.id.tv_passwordActual);
            //Para mostrar la contraseña actual, la desencriptamos primero
            tv_currentPassword.setText(tv_currentPassword.getText() + " " + PasswordEncrypter.DecryptText(currentEmployee.get("Password").toString()));
            //E-mail
            tv_cambioEmail = (TextView) rootView.findViewById(R.id.tv_cambiarEmail);
            tv_cambioEmail.setTag(3);
            tv_cambioEmail.setOnClickListener(this);

            tv_currentEmail = (TextView) rootView.findViewById(R.id.tv_emailActual);
            tv_currentEmail.setText(tv_currentEmail.getText() + " " + currentEmployee.get("Email"));
            //Business
            tv_business = (TextView) rootView.findViewById(R.id.tv_empresaUsuario);
            currentBusiness = connect.catchEmployeeBusiness((ParseObject) currentEmployee.get("Empresa"));
            tv_business.setText(tv_business.getText() + " " + currentBusiness);
            //Registered
            tv_registered = (TextView) rootView.findViewById(R.id.tv_registradoDesdeUsuario);
            Date fechaRegistro = currentEmployee.getCreatedAt();
            tv_registered.setText(tv_registered.getText() + " " + DateFormat.getInstance().format(fechaRegistro));

            //Creación de los fragments
            checkPasswordFragment = new CheckPasswordFragment();

        }
    }

    @Override
    public void onClick(View v) {

        int tag = (int) v.getTag();
        switch (tag) {
            //Cambio de username
            case 1:
                args.putInt("Cambio", 1);
                break;
            //Cambio de password
            case 2:
                args.putInt("Cambio", 2);
                break;
            //Cambio de email
            case 3:
                args.putInt("Cambio", 3);
                break;
        }
        args.putString("Usuario", usuario);
        fragmentTransaction = fragmentManager.beginTransaction();
        checkPasswordFragment.setArguments(args);
        fragmentTransaction.add(android.R.id.content, checkPasswordFragment);
        fragmentTransaction.addToBackStack("CheckPassword");
        fragmentTransaction.commit();
    }

}
