package com.example.a41396969.salidasnow;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


/**
 * Created by 41396969 on 20/05/2016.
 */
public class Restaurant implements Serializable {


   private String nombre, direccion, icono;
    private int precio, estrellas, numeroTelefono;

    public Restaurant(String nombre, String direccion, String icono) {
        this.direccion = direccion;
        this.nombre = nombre;
        this.icono = icono;
    }

    public Restaurant()
    {

    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(int estrellas) {
        this.estrellas = estrellas;
    }

    public int getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(int numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }
}
