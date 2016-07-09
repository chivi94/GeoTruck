package com.ivan.geotruck.fragments.main_fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.ivan.geotruck.R;
import com.ivan.geotruck.activities.RegisterFormActivity;
import com.ivan.geotruck.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements OnClickListener {

    private LinearLayout ly_easter;
    private int cont = 1;
    private ImageView imageView;
    private RotateAnimation anim;

    private RadioGroup tiposDeUsuario;
    private Button botonSiguiente;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ly_registro
                , container, false);

        setUpComponents(rootView);

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        switch (tag) {
            //Si desencadena el easter egg
            case 1:
                doSomethingCool();
                break;
            //Si da al boton
            case 2:
                showRegistrationForm();
                break;
        }

    }

    /**
     * Método que inicializa los elementos de la interfaz gráfica.
     */
    private void setUpComponents(View rootView) {
        ly_easter = (LinearLayout) rootView.findViewById(R.id.ly_titulo);
        ly_easter.setTag(1);
        ly_easter.setOnClickListener(this);

        tiposDeUsuario = (RadioGroup) rootView.findViewById(R.id.rg_tipos_usuario);

        botonSiguiente = (Button) rootView.findViewById(R.id.b_siguiente);
        botonSiguiente.setTag(2);
        botonSiguiente.setOnClickListener(this);


    }

    /**
     * Método que inicia el formulario de registro de usuarios en función de la opción seleccionada.
     */
    private void showRegistrationForm() {
        //Indice del elemento seleccionado
        int id = tiposDeUsuario.getCheckedRadioButtonId();

        RadioButton radioButton = (RadioButton) tiposDeUsuario.findViewById(id);
        int index = tiposDeUsuario.indexOfChild(radioButton);

        Intent tipoFormulario = new Intent(((MainActivity) getActivity()).getApplicationContext(), RegisterFormActivity.class);
        tipoFormulario
                .putExtra("Tipo", index);
        ((MainActivity) getActivity()).startActivity(tipoFormulario);
    }

    /**
     * Método que incrementa el contador, y que comprueba su valor. Si es divisible por 10, muestra una animación.
     */
    private void doSomethingCool() {
        //Si es divisible entre 21...
        if (cont % 21 == 0) {
            if (imageView == null) {
                imageView = new ImageView(getActivity());
                imageView.setImageResource(R.drawable.ic_launcher);
            }
            ly_easter.addView(imageView, 0);

            // Step1 : create the  RotateAnimation object
            anim = new RotateAnimation(0f, 350f, 15f, 15f);
            // Step 2:  Set the Animation properties
            anim.setInterpolator(new LinearInterpolator());
            //anim.setRepeatCount(Animation.INFINITE);
            anim.setDuration(700);

            // Step 3: Start animating the image
            imageView.startAnimation(anim);

            // Later. if you want to  stop the animation
            // cont = 0;
        }
        //Si la animación ha sido creadam, se ha inicializado y ha terminado...
        if (anim != null && anim.isInitialized() && anim.hasEnded()) {
            ly_easter.removeView(imageView);
        }
        //Si el contador es divisible entre 35...
        if (cont % 35 == 0) {
            Toast.makeText(getActivity(), R.string.toast_dialogoSalvaje, Toast.LENGTH_SHORT).show();
        }
        cont++;
    }
}
