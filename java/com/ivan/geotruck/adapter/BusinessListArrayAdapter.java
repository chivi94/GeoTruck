package com.ivan.geotruck.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.ivan.geotruck.R;

import java.util.ArrayList;

/**
 * Created by Ivan on 07/04/2015.
 */
public class BusinessListArrayAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private ArrayList<String> business;
    private ArrayList<String> businessFilterList;
    private ValueBusinessFilter valueBusinessFilter;

    public BusinessListArrayAdapter(Context context, int resource, ArrayList<String> business) {

        this.context = context;
        this.business = business;
        this.businessFilterList = business;
    }

    private int lastPosition = -1;

    @Override
    public int getCount() {
        return business.size();
    }

    @Override
    public Object getItem(int position) {
        return business.get(position);
    }

    @Override
    public long getItemId(int position) {
        return business.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String enterprise = business.get(position);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View currentEnterprise = inflater.inflate(R.layout.ly_bussiness_adapter, null);

        TextView businessName = (TextView) currentEnterprise.findViewById(R.id.tv_adapterEmpresa);
        businessName.setTag(4);
        businessName.setText(enterprise);

        Animation animation =
                AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        currentEnterprise.startAnimation(animation);
        lastPosition = position;
        return currentEnterprise;
    }

    @Override
    public Filter getFilter() {
        if (valueBusinessFilter == null) {
            valueBusinessFilter = new ValueBusinessFilter();
        }
        return valueBusinessFilter;
    }

    private class ValueBusinessFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {

                ArrayList<String> filterList = new ArrayList<String>();
                for (int i = 0; i < businessFilterList.size(); i++) {
                    if ((businessFilterList.get(i).toString().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        filterList.add(businessFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;


            } else {
                results.count = businessFilterList.size();
                results.values = businessFilterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            business = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }
    }
}
