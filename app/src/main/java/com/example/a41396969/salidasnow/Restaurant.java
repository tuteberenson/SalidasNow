package com.example.a41396969.salidasnow;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


/**
 * Created by 41396969 on 20/05/2016.
 */
public class Restaurant implements Serializable {


    String nombre;
    String direccion;
    String icono;

    public Restaurant(String nombre, String direccion, String icono) {
        this.direccion = direccion;
        this.nombre = nombre;
        this.icono = icono;
    }

}
