package com.example.a41396969.salidasnow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;





public class buscandousuariologueado extends AppCompatActivity {

    SharedPreferences prefs =  getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscandousuariologueado);



        SharedPreferences.Editor editor = prefs.edit();
       int usuario= prefs.getInt("idusuario",0);
       if( !prefs.contains("idusuario") == false)
       {

           Intent actividaddedestino;
           actividaddedestino =new Intent(buscandousuariologueado.this, LogIn.class);
           startActivity(actividaddedestino);

       }
        else {
           Usuarios datos;
           datos = new Usuarios();
           int id;
           String nombre, password, apellido, username;

           id = prefs.getInt("idusuario", 0);
           datos.set_idUsuario(id);

           nombre = prefs.getString("nombre", "");
           datos.set_Nombre(nombre);
           apellido = prefs.getString("apellido", "");
           datos.set_Nombre(apellido);
           username = prefs.getString("username", "");
           datos.set_Nombre(username);
           password = prefs.getString("password", "");
           datos.set_Nombre(password);

           Intent ActividadDestino;
           ActividadDestino = new Intent(buscandousuariologueado.this, LogIn.class);
           Bundle paquete = new Bundle();
           paquete.putSerializable("listaUsuario", datos);
           ActividadDestino.putExtras(paquete);
           startActivity(ActividadDestino);
       }


       }




    }
