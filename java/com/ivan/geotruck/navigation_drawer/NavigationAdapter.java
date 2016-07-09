package com.ivan.geotruck.navigation_drawer;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivan.geotruck.navigation_drawer.NavigationDrawerItems;
import com.ivan.geotruck.R;

import java.util.ArrayList;

/**
 * Adapter usado en la lista de un objeto Navigation Drawer.
 */
public class NavigationAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<NavigationDrawerItems> navigationDrawerItemsArrayList;

    public NavigationAdapter(Activity activity, ArrayList<NavigationDrawerItems> navigationDrawerItemsArrayList) {
        this.activity = activity;
        this.navigationDrawerItemsArrayList = navigationDrawerItemsArrayList;
    }

    @Override
    public int getCount() {
        return navigationDrawerItemsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return navigationDrawerItemsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public static class Fila {
        TextView titulo_item;
        ImageView icono;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Fila view;
        if (convertView == null) {
            view = new Fila();
            //Creo objeto item y lo obtengo del array
            NavigationDrawerItems itm = navigationDrawerItemsArrayList.get(position);
            convertView = activity.getLayoutInflater().inflate(R.layout.navigation_drawer_item, null);
            //Titulo
            view.titulo_item = (TextView) convertView.findViewById(R.id.title_item);
            //Seteo en el campo titulo el nombre correspondiente obtenido del objeto
            view.titulo_item.setText(itm.getTitulo());
            //Icono
            view.icono = (ImageView) convertView.findViewById(R.id.icon);
            //Seteo el icono
            view.icono.setImageResource(itm.getIcono());

            convertView.setTag(view);
        } else {
            view = (Fila) convertView.getTag();
        }
        return convertView;

    }
}
