package com.ivan.geotruck.fragments.boss_register_fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.ivan.geotruck.activities.RegisterFormActivity;
import com.ivan.geotruck.asynctask.AsyncTaskController;
import com.ivan.geotruck.asynctask.RegisterAsyncTask;
import com.ivan.geotruck.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class BossFormFragment extends Fragment implements OnClickListener {

    private static String business;
    //Elementos del layout de jefe
    private EditText et_empresa;
    private EditText et_nombre;
    private EditText et_telefono;
    private EditText et_password;
    private EditText et_confirmPassword;
    private EditText et_usuarioRegistro;
    private EditText et_email;
    private Button b_confirmar;
    //AsyncTask que se encargará del registro
    private RegisterAsyncTask registerAsyncTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ly_jefe, container, false);

        Bundle args = getArguments();
        if (args != null && args.containsKey("Empresa")) {
            business = args.getString("Empresa", "DHL");
            if (business != null) {
                setUpComponents(rootView);
            }
        }


        return rootView;
    }


    @Override
    public void onClick(View v) {

        Context context = (RegisterFormActivity) getActivity();
        String password = et_password.getText().toString();
        String confirmPassword = et_confirmPassword.getText().toString();

        String userName = et_usuarioRegistro.getText().toString();
        String email = et_email.getText().toString();
        String name = et_nombre.getText().toString();
        String phone = et_telefono.getText().toString();
        if (!name.equals("") && !phone.equals("") && !userName.equals("")
                && !password.equals("") && !confirmPassword.equals("")
                && !email.equals("")) {
            boolean passwordValid = isPasswordValid(password, confirmPassword);
            if (passwordValid) {
                boolean emailValid = isEmailValid(email);
                if (emailValid) {
                    boolean phoneValid = isPhonevalid(phone);
                    if (phoneValid) {
                        registerAsyncTask = new RegisterAsyncTask(getActivity(), name, phone, password, userName, email, business, 0);
                        AsyncTaskController runnableAsyncTask = new AsyncTaskController(registerAsyncTask, registerAsyncTask.getContext(), registerAsyncTask.getProgressDialog());
                        runnableAsyncTask.start();
                    } else {
                        Toast.makeText(context, getResources().getString(R.string.toast_telefonoIncorrecto), Toast.LENGTH_SHORT).show();
                        et_telefono.requestFocus();
                    }
                } else {
                    Toast.makeText(context, R.string.toast_emailIncorrecto, Toast.LENGTH_SHORT).show();
                    et_email.requestFocus();
                }
            } else {
                Toast.makeText(context, R.string.toast_passwordCoinciden, Toast.LENGTH_SHORT).show();
                et_password.requestFocus();
            }
        } else {
            Toast.makeText(context, R.string.toast_campoVacio, Toast.LENGTH_SHORT).show();
        }

    }

    private void setUpComponents(View rootView) {
        //Relaciono los elementos del ly_jefe con el xml
        et_empresa = (EditText) rootView.findViewById(R.id.et_empresa);
        et_empresa.setText(business);
        et_nombre = (EditText) rootView.findViewById(R.id.et_nombre);
        et_telefono = (EditText) rootView.findViewById(R.id.et_telefono);
        et_password = (EditText) rootView.findViewById(R.id.et_password);
        et_confirmPassword = (EditText) rootView.findViewById(R.id.et_confirmarPassword);
        et_usuarioRegistro = (EditText) rootView.findViewById(R.id.et_usuarioRegistro);
        et_email = (EditText) rootView.findViewById(R.id.et_eMail);
        b_confirmar = (Button) rootView.findViewById(R.id.b_confirmarRegistro);

        b_confirmar.setOnClickListener(this);
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
}
