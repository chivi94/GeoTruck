package com.ivan.geotruck.fragments.settings_fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ivan.geotruck.R;
import com.ivan.geotruck.activities.RegisterFormActivity;
import com.ivan.geotruck.activities.SettingsActivity;
import com.ivan.geotruck.connection.DatabaseConnect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ivan on 25/05/2015.
 */
public class ChangeEmailFragment extends Fragment implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Bundle args = null;
    private Bundle newArgs = null;
    private String usuario;
    private SettingsFragment settingsFragment = new SettingsFragment();


    //Componentes
    private EditText et_email;
    private String nuevoEmail;
    private Button b_confirmar;

    //Conexion con la BBDD
    private DatabaseConnect connect = new DatabaseConnect();

    //Activity padre
    private SettingsActivity parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.ly_change_email_fragment, container, false);
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
        nuevoEmail = et_email.getText().toString();
        if (!nuevoEmail.equals("")) {
            boolean isEmailValid = isEmailValid(nuevoEmail);
            if (isEmailValid) {
                if (connect.changeUserEmail(usuario, nuevoEmail)) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    newArgs = new Bundle();
                    newArgs.putString("Usuario", usuario);
                    settingsFragment.setArguments(newArgs);
                    fragmentTransaction.replace(android.R.id.content, settingsFragment);
                    parent.clearBackStack();
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(getActivity(), R.string.toast_cambioEmailFallido, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText((SettingsActivity) getActivity(), getResources().getString(R.string.toast_emailIncorrecto), Toast.LENGTH_SHORT).show();
                et_email.requestFocus();
            }
        }
    }

    private void setUpComponents(View rootView) {
        fragmentManager = getFragmentManager();
        et_email = (EditText) rootView.findViewById(R.id.et_nuevoMail);
        b_confirmar = (Button) rootView.findViewById(R.id.b_confirmarNuevoMail);
        b_confirmar.setOnClickListener(this);
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
}
