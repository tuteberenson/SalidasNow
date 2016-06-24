package com.example.a41396969.salidasnow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class sing_up extends AppCompatActivity {

    EditText txtUsuario, txtMail, txtContra, txtContra2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        txtUsuario =(EditText)findViewById(R.id.txtnomUsuario);
        txtMail =(EditText)findViewById(R.id.txtMail);
        txtContra =(EditText)findViewById(R.id.txtContra);
        txtContra2 =(EditText)findViewById(R.id.txtContra2);
    }
    public void Click_singUP (View Vista)
    {
        if (txtUsuario.length()<6)
        {
            Toast.makeText(sing_up.this, "El nombre de Usuario es muy corto", Toast.LENGTH_SHORT).show();
        }
        String letra = "@";
        int posicion;
        posicion = txtMail.getText().toString().indexOf(letra);
        if (posicion != 0)
        {
            
        }
    }
}
