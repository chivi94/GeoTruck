package com.ivan.geotruck.activities;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ivan.geotruck.R;
import com.ivan.geotruck.activities_funcionality.ActivityFuncionality;
import com.ivan.geotruck.fragments.boss_register_fragments.BusinessTypeFragment;
import com.ivan.geotruck.fragments.employee_register_fragments.EmployeeFormFragment;

public class RegisterFormActivity extends FragmentActivity {

    //Fragments
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private EmployeeFormFragment employeeFormFragment;
    private BusinessTypeFragment businessTypeFragment;
    //ActionBar
    private ActionBar actionBar;
    //ActivityFuncionality
    private ActivityFuncionality activityFuncionality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityFuncionality = new ActivityFuncionality(this);

        activityFuncionality.setStatusBar();

        actionBar = activityFuncionality.setUpActionBar(actionBar, 2);

        fragmentManager = getFragmentManager();

        int tipo = getIntent().getIntExtra("Tipo", 2);
        chargeRegisterForm(tipo);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void chargeRegisterForm(int tipo) {
        switch (tipo) {
            //Para el jefe
            case 0:
                fragmentTransaction = fragmentManager.beginTransaction();
                businessTypeFragment = new BusinessTypeFragment();
                fragmentTransaction.replace(android.R.id.content, businessTypeFragment);
                fragmentTransaction.commit();
                break;
            //Para el empleado
            case 1:
                fragmentTransaction = fragmentManager.beginTransaction();
                employeeFormFragment = new EmployeeFormFragment();
                fragmentTransaction.replace(android.R.id.content, employeeFormFragment);
                fragmentTransaction.commit();
                break;
            //En cualquier otro caso
            default:
                Toast.makeText(this, getResources().getString(R.string.toast_seleccione), Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }


}
