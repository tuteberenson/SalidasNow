package com.example.a41396969.salidasnow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 41396969 on 20/05/2016.
 */
public class RestaurantAdapter extends BaseAdapter {

    ArrayList<Restaurant> restaurants;
    Context context;

    public RestaurantAdapter(Context context, ArrayList<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    @Override
    public int getCount() {
        return restaurants.size();
    }

    @Override
    public Object getItem(int i) {
        return restaurants.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, viewGroup, false);
        }

        TextView nombreTV = (TextView) view.findViewById(R.id.nombre);
        TextView direccionTV = (TextView) view.findViewById(R.id.direc);

        Restaurant p = this.restaurants.get(position);
        nombreTV.setText(p.getNombre());
        direccionTV.setText(p.getDireccion());
        return view;
    }

}
