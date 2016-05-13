package com.example.a41396969.salidasnow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ActividadInicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_inicio);
    }

    public void Click_Prueba(View vista)
    {
        Intent ActividadDestino;
        ActividadDestino = new Intent(ActividadInicio.this, ActividadLugaresCercanos.class);
        startActivity(ActividadDestino);
    }
}
