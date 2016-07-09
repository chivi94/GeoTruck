package com.ivan.geotruck.fragments.boss_register_fragments;


import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.ivan.geotruck.R;
import com.ivan.geotruck.activities.RegisterFormActivity;

import com.ivan.geotruck.asynctask.AsyncTaskController;
import com.ivan.geotruck.asynctask.SaveBusinessAsyncTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewBusinessFragment extends Fragment implements OnClickListener {

    private EditText editTextEmpresa;
    private SaveBusinessAsyncTask saveBusinessAsyncTask;
    private static String newBusiness;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ly_empresa_nueva, container, false);

        setUpComponents(rootView);

        return rootView;
    }


    @Override
    public void onClick(View v) {

        String empresa = editTextEmpresa.getText().toString();

        if (!empresa.equals("")) {
            newBusiness = empresa;

            saveBusinessAsyncTask = new SaveBusinessAsyncTask((RegisterFormActivity) getActivity(), empresa, getFragmentManager(), getFragmentManager().beginTransaction());
            AsyncTaskController runnableAsyncTask = new AsyncTaskController(saveBusinessAsyncTask, saveBusinessAsyncTask.getContext(), saveBusinessAsyncTask.getProgressDialog());
            runnableAsyncTask.start();
        } else {
            Toast.makeText((RegisterFormActivity) getActivity(), R.string.toast_escribaEmpresa, Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpComponents(View rootView) {
        editTextEmpresa = (EditText) rootView.findViewById(R.id.et_nombre_nueva_empresa);
        ((Button) rootView.findViewById(R.id.b_siguiente_nueva)).setOnClickListener(this);
    }
}
