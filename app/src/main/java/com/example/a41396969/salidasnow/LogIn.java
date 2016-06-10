package com.example.a41396969.salidasnow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
    }

    public void Click_Prueba(View vista) {
        Intent ActividadDestino;
        ActividadDestino = new Intent(LogIn.this, ActividadPrincipal.class);
        startActivity(ActividadDestino);
    }
}
