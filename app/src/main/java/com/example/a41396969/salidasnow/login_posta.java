package com.example.a41396969.salidasnow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class login_posta extends AppCompatActivity {
    EditText Usuario;
    EditText Contraseña;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_posta);
        Usuario= (EditText)findViewById(R.id.usuario);
        Contraseña= (EditText)findViewById(R.id.contraseña);
    }
    public void Click_Login (View Vista){
    if (Usuario.length() == 0 || Contraseña.length() == 0)
    {
        Toast.makeText(login_posta.this, "Ingrese Usuario o Contraseña", Toast.LENGTH_SHORT).show();
    }
        else
    {
        Intent ActividadDestino;
        ActividadDestino=new Intent(login_posta.this, ActividadPrincipal.class);
        startActivity(ActividadDestino);
    }

    }
}
