package com.ivan.geotruck.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.LinearGradient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ivan.geotruck.R;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by Ivan on 05/05/2015.
 */
public class EmployeeListArrayAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private ArrayList<ParseObject> employees;
    private ArrayList<ParseObject> employeeFilterList;
    private ValueEmployeeFilter valueEmployeeFilter;


    public EmployeeListArrayAdapter(Context context, int resource, ArrayList<ParseObject> employees) {
        this.context = context;
        this.employees = employees;
        this.employeeFilterList = employees;
    }


    private int lastPosition = -1;

    @Override
    public int getCount() {
        return employees.size();

    }

    @Override
    public Object getItem(int position) {

        return employees.get(position);

    }

    @Override
    public long getItemId(int position) {
        return employees.indexOf(getItem(position));

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View currentEmployee = inflater.inflate(R.layout.ly_employee_adapter, null);
        TextView employeeName = (TextView) currentEmployee.findViewById(R.id.tv_nombre_usuario);
        TextView employeePhone = (TextView) currentEmployee.findViewById(R.id.tv_telefono_usuario);
        if (employees != null) {
            ParseObject employee = employees.get(position);

            employeeName.setTag(4);
            employeeName.setText("Nombre:" + employee.get("Nombre").toString());

            employeePhone.setText(employee.get("Telefono").toString());

            Animation animation =
                    AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            currentEmployee.startAnimation(animation);
            //Si no hay coincidencias de búsqueda
        }

        lastPosition = position;


        return currentEmployee;
    }

    @Override
    public Filter getFilter() {
        if (valueEmployeeFilter == null) {
            valueEmployeeFilter = new ValueEmployeeFilter();
        }
        return valueEmployeeFilter;
    }

    private class ValueEmployeeFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {

                ArrayList<ParseObject> filterList = new ArrayList<ParseObject>();
                for (int i = 0; i < employeeFilterList.size(); i++) {
                    if ((employeeFilterList.get(i).get("Nombre").toString().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        ParseObject employee = new ParseObject("Empleado");
                        employee.put("Nombre", employeeFilterList.get(i).get("Nombre").toString());
                        employee.put("Telefono", employeeFilterList.get(i).get("Telefono").toString());
                        filterList.add(employee);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;


            } else {
                results.count = employeeFilterList.size();
                results.values = employeeFilterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            employees = (ArrayList<ParseObject>) results.values;
            notifyDataSetChanged();
        }
    }


}