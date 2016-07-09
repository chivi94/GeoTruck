package com.ivan.geotruck.fragments.boss_login_fragments;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.ivan.geotruck.R;
import com.ivan.geotruck.activities.SelectedEmployeePositionActivity;
import com.ivan.geotruck.activities.UserActivity;
import com.ivan.geotruck.connection.DatabaseConnect;
import com.parse.ParseObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeeDetailFragment extends Fragment implements OnClickListener {
    //UI del fragment
    private View rootView;
    //Elementos de texto
    private String usuarioDetalle;
    private String jefe;
    private DatabaseConnect connect = new DatabaseConnect();
    //Elementos del detalle
    private TextView tv_nombreConductor;
    private TextView tv_telefono;
    private TextView tv_email;
    //Botones de llamar, enviar correo y ver ubicación del empleado.
    private Button button_agregar;
    private Button button_llamar;
    private Button button_enviarCorreo;
    private Button button_verUbicacion;
    //Activity padre
    private UserActivity parent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.ly_detalle_empleado, container, false);
        Bundle args = getArguments();
        if (args != null) {
            usuarioDetalle = args.getString("Detalle");
            jefe = args.getString("Nombre_Jefe");
        }

        setFragmentUI();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        final String[] datosTelefono = tv_telefono.getText().toString().split(":");
        final String[] datosEmail = tv_email.getText().toString().split(":");
        switch (tag) {
            //Añadir contacto
            case 1:
                showDialogSaveContact(datosEmail, datosTelefono);
                break;
            //Llamar
            case 2:
                Intent llamar = new Intent(Intent.ACTION_CALL);
                llamar.setData(Uri.parse("tel:" + datosTelefono[1]));
                getActivity().startActivity(llamar);
                break;
            //Correo
            case 3:
                Intent email = new Intent(
                        android.content.Intent.ACTION_SEND);
                email.setType("plain/text");
                email.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[]{datosEmail[1]});
                // creteChooser nos permitirá elegir con qué aplicación queremos
                // enviar el correo.
                getActivity().startActivity(Intent
                        .createChooser(email, getString(R.string.dialog_selectEmailActivity)));
                break;
            //Ubicacion
            case 4:
                Intent employeePosition = new Intent();
                employeePosition.setClass(getActivity(), SelectedEmployeePositionActivity.class);
                employeePosition.putExtra("Usuario", usuarioDetalle);
                getActivity().startActivity(employeePosition);
                break;
        }
    }

    //Método que establece los elementos de la pagina
    private void setFragmentUI() {
        tv_nombreConductor = (TextView) rootView.findViewById(R.id.tv_nombreConductor);
        tv_telefono = (TextView) rootView.findViewById(R.id.tv_telefonoConductor);
        tv_email = (TextView) rootView.findViewById(R.id.tv_emailConductor);

        ParseObject empleadoSeleccionado = connect.getEmployeeData(usuarioDetalle, jefe);

        tv_nombreConductor.setText(tv_nombreConductor.getText() + " " + usuarioDetalle);
        tv_telefono.setText(tv_telefono.getText() + " " + empleadoSeleccionado.get("Telefono").toString());
        tv_email.setText(tv_email.getText() + " " + empleadoSeleccionado.get("Email").toString());

        //Botones
        button_agregar = (Button) rootView.findViewById(R.id.button_agregar);
        button_agregar.setTag(1);
        button_agregar.setOnClickListener(this);
        button_llamar = (Button) rootView.findViewById(R.id.button_llamar);
        button_llamar.setTag(2);
        button_llamar.setOnClickListener(this);
        button_enviarCorreo = (Button) rootView.findViewById(R.id.button_email);
        button_enviarCorreo.setTag(3);
        button_enviarCorreo.setOnClickListener(this);
        button_verUbicacion = (Button) rootView.findViewById(R.id.button_ubicacion);
        button_verUbicacion.setTag(4);
        button_verUbicacion.setOnClickListener(this);
        //ActionBar
        parent = (UserActivity) getActivity();
        parent.getDrawerToggle().setDrawerIndicatorEnabled(false);
        parent.getActionBar().setDisplayHomeAsUpEnabled(true);
        parent.getActionBar().setTitle(usuarioDetalle);
    }

    //Método que muestra un diálogo para guardar el contacto en la agenda
    private void showDialogSaveContact(final String[] datosEmail, final String[] datosTelefono) {
        AlertDialog.Builder dialogCloseSession = new AlertDialog.Builder(getActivity());
        dialogCloseSession.setTitle(R.string.dialog_saveContactTitle);
        dialogCloseSession.setMessage(R.string.dialog_saveContact);
        dialogCloseSession.setCancelable(true);
        dialogCloseSession.setPositiveButton(R.string.dialog_possitiveButton,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveContact(datosEmail, datosTelefono);
                    }
                });
        dialogCloseSession.setNegativeButton(R.string.dialog_negativeButton,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        dialogCloseSession.show();
    }

    //Método que establece de que forma se guarda el contacto en el teléfono del usuario
    private void saveContact(String[] datosEmail, String[] datosTelefono) {

        Intent agregar = new Intent(ContactsContract.Intents.Insert.ACTION);
        agregar.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        agregar.putExtra(ContactsContract.Intents.Insert.EMAIL, datosEmail[1]).
                putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK).
                putExtra(ContactsContract.Intents.Insert.PHONE, datosTelefono[1]).
                putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK).
                putExtra(ContactsContract.Intents.Insert.NAME, usuarioDetalle);
        startActivity(agregar);
    }


}

