package com.example.a41396969.salidasnow;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 41396969 on 12/8/2016.
 */
public class RestaurantAdapterCompleto extends BaseAdapter{

    ArrayList<Restaurant> restaurants;
    Context context;

    public RestaurantAdapterCompleto(Context context, ArrayList<Restaurant> restaurants) {
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
            view = inflater.inflate(R.layout.list_item_resto_completo, viewGroup, false);
        }

        TextView nombreTV = (TextView) view.findViewById(R.id.nombre);
        TextView direccionTV = (TextView)view.findViewById(R.id.direc);
        TextView precioTV1 = (TextView)view.findViewById(R.id.txvwLVPrecio1);
        TextView precioTV2 = (TextView)view.findViewById(R.id.txvwLVPrecio2);
        TextView precioTV3 = (TextView)view.findViewById(R.id.txvwLVPrecio3);
        TextView precioTV4 = (TextView)view.findViewById(R.id.txvwLVPrecio4);
        TextView precioTV5 = (TextView)view.findViewById(R.id.txvwLVPrecio5);
        TextView estrellasTV = (TextView)view.findViewById(R.id.txvwLVestrellas);

        Restaurant p = this.restaurants.get(position);
        nombreTV.setText(p.getNombre());
        direccionTV.setText(p.getDireccion());
        switch (p.getPrecio())
        {
            case 1:
                precioTV1.setTextColor(Color.rgb(0,0,0));
                precioTV2.setTextColor(Color.rgb(124,124,124));
                precioTV3.setTextColor(Color.rgb(124,124,124));
                precioTV4.setTextColor(Color.rgb(124,124,124));
                precioTV5.setTextColor(Color.rgb(124,124,124));
                break;
            case 2:
                precioTV1.setTextColor(Color.rgb(0,0,0));
                precioTV2.setTextColor(Color.rgb(0,0,0));
                precioTV3.setTextColor(Color.rgb(124,124,124));
                precioTV4.setTextColor(Color.rgb(124,124,124));
                precioTV5.setTextColor(Color.rgb(124,124,124));
                break;
            case 3:
                precioTV1.setTextColor(Color.rgb(0,0,0));
                precioTV2.setTextColor(Color.rgb(0,0,0));
                precioTV3.setTextColor(Color.rgb(0,0,0));
                precioTV4.setTextColor(Color.rgb(124,124,124));
                precioTV5.setTextColor(Color.rgb(124,124,124));
                break;
            case 4:
                precioTV1.setTextColor(Color.rgb(0,0,0));
                precioTV2.setTextColor(Color.rgb(0,0,0));
                precioTV3.setTextColor(Color.rgb(0,0,0));
                precioTV4.setTextColor(Color.rgb(0,0,0));
                precioTV5.setTextColor(Color.rgb(124,124,124));
                break;
            case 5:
                precioTV1.setTextColor(Color.rgb(0,0,0));
                precioTV2.setTextColor(Color.rgb(0,0,0));
                precioTV3.setTextColor(Color.rgb(0,0,0));
                precioTV4.setTextColor(Color.rgb(0,0,0));
                precioTV5.setTextColor(Color.rgb(0,0,0));
                break;
        }
        estrellasTV.setText("Estrellas: "+p.getEstrellas());
        return view;
    }
}
