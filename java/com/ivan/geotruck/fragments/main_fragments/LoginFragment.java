package com.ivan.geotruck.fragments.main_fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.ivan.geotruck.shared_preferences.SharedPreferencesConstants;
import com.ivan.geotruck.R;
import com.ivan.geotruck.asynctask.AsyncTaskController;
import com.ivan.geotruck.asynctask.LoginAsyncTask;


public class LoginFragment extends Fragment implements OnClickListener, OnCheckedChangeListener {

    private EditText userName;
    private EditText password;
    private Button login;
    private CheckBox holdSession;
    private CheckBox showPassword;
    private LoginAsyncTask logMe;
    //SharedPreferences para mantener la sesión abierta
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ly_login, container, false);

        //Preferences
        preferences = getActivity().getApplicationContext().getSharedPreferences(SharedPreferencesConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

        setUpComponents(rootView);

        checkLogin();

        return rootView;
    }


    @Override
    public void onClick(View v) {
        String user = userName.getText().toString();
        String pass = password.getText().toString();
        if (holdSession.isChecked()) {
            if (!SharedPreferencesConstants.autoLogin) {
                newLogin(user, pass);
            } else {
                catchOldLogin();
            }
        } else {
            if (!user.equals("") && !pass.equals("")) {
                normalLogin();
            } else {
                Toast.makeText(getActivity(), R.string.toast_usuarioOPassword, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    /**
     * Método que inicializa los elementos de la interfaz gráfica.
     */
    private void setUpComponents(View rootView) {
        userName = (EditText) rootView.findViewById(R.id.et_usuario);
        password = (EditText) rootView.findViewById(R.id.et_contrasenia);
        login = (Button) rootView.findViewById(R.id.b_entrar);
        holdSession = (CheckBox) rootView.findViewById(R.id.check_mantener_sesion);
        showPassword = (CheckBox) rootView.findViewById(R.id.check_mostrar_password);


        showPassword.setOnCheckedChangeListener(this);

        login.setOnClickListener(this);
    }

    /**
     * Método que inicia una sesión nueva en la aplicación, y guarda los datos del usuario que está iniciando sesión.
     *
     * @param user Nombre de usuario que está iniciando sesión.
     * @param pass Contraseña de usuario que está iniciando sesión.
     */
    private void newLogin(String user, String pass) {
        if (!user.equals("") && !pass.equals("")) {
            //Editamos las preferences
            editor.putString(SharedPreferencesConstants.USERNAME, user);
            editor.putString(SharedPreferencesConstants.PASSWORD, pass);
            editor.putBoolean(SharedPreferencesConstants.HOLD_SESSION, true);
            editor.commit();

            logMe = new LoginAsyncTask(getActivity(), user, pass);
            AsyncTaskController runnableAsyncTask = new AsyncTaskController(logMe, logMe.getContext(), logMe.getProgressDialog());
            runnableAsyncTask.start();
        } else {
            Toast.makeText(getActivity(), R.string.toast_usuarioOPassword, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método que inicia sesión en el sistema si hay datos guardados en el terminal.
     */
    private void catchOldLogin() {
        String username = preferences.getString(SharedPreferencesConstants.USERNAME, null);
        String user_password = preferences.getString(SharedPreferencesConstants.PASSWORD, null);
        userName.setText(username);
        password.setText(user_password);
        logMe = new LoginAsyncTask(getActivity(), username, user_password);
        AsyncTaskController runnableAsyncTask = new AsyncTaskController(logMe, logMe.getContext(), logMe.getProgressDialog());
        runnableAsyncTask.start();
    }

    /**
     * Método que inicia sesión en el sistema sin guardar ningún tipo de credencial.
     */
    private void normalLogin() {
        logMe = new LoginAsyncTask(getActivity(), userName.getText().toString(), password.getText().toString());
        AsyncTaskController runnableAsyncTask = new AsyncTaskController(logMe, logMe.getContext(), logMe.getProgressDialog());
        runnableAsyncTask.start();
    }

    /**
     * Método que comprueba si hay datos guardados de una sesión antigua. En caso afirmativo, inicia sesión automáticamente.
     */
    private void checkLogin() {
        String username = preferences.getString(SharedPreferencesConstants.USERNAME, null);
        String user_password = preferences.getString(SharedPreferencesConstants.PASSWORD, null);
        boolean isHoldSession = preferences.getBoolean(SharedPreferencesConstants.HOLD_SESSION, false);

        if (username != null && user_password != null && isHoldSession) {
            SharedPreferencesConstants.autoLogin = true;
            holdSession.setChecked(SharedPreferencesConstants.autoLogin);
            //Hace click en el boton de forma automatica
            login.performClick();
        }
    }


}


