package com.example.a41396969.salidasnow;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class LogIn extends AppCompatActivity {

    EditText edTxUsername, edTxPassword;
    String passwordIngresada, usernameIngresado;
    boolean UsuarioCorrecto, passInvalida;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        edTxPassword=(EditText)findViewById(R.id.passwordLogIn);
        edTxUsername=(EditText)findViewById(R.id.usuarioLogIn);

    }

    public void Click_generalLogIn(View vista) {

        Resources recursos;
        recursos = getResources();
        passwordIngresada = edTxPassword.getText().toString().trim();
        usernameIngresado = edTxUsername.getText().toString().trim();

        if (usernameIngresado.compareTo("") == 0) {

            edTxUsername.setHintTextColor(recursos.getColor(R.color.Rojo));
            edTxUsername.setHint("Ingrese usuario");
        } else if (passwordIngresada.compareTo("") == 0)
        {
            edTxPassword.setHintTextColor(recursos.getColor(R.color.Rojo));
            edTxPassword.setHint("Ingrese contraseña");
        } else
        {
            String url = "http://salidasnow.hol.es/obtener_usuarios.php";
            new TraerUsuariosAsyncTask().execute(url);
        }
        InputMethodManager imm = (InputMethodManager) LogIn.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edTxUsername.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(edTxPassword.getWindowToken(), 0);
    }
    private class TraerUsuariosAsyncTask extends AsyncTask<String,Void,ArrayList<Usuarios>>
    {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPostExecute(ArrayList<Usuarios> resultado) {
            super.onPostExecute(resultado);
             UsuarioCorrecto =false;
            passInvalida=false;


            int comparadorUser, comparadorPass;

            if (resultado != null) {

                for (Usuarios unUsuario :resultado) {

                    comparadorUser= unUsuario.get_Username().compareTo(usernameIngresado);
                    comparadorPass=unUsuario.get_Password().compareTo(passwordIngresada);

                    if (comparadorUser== 0 && comparadorPass ==0)
                    {
                        unUsuario.set_Username(usernameIngresado);
                        unUsuario.set_Password(passwordIngresada);
                        UsuarioCorrecto =true;
                        Log.d("Usuario","true");
                    }
                    else if (comparadorUser== 0 && comparadorPass !=0)
                    {
                        passInvalida=true;
                    }

                }
                if (passInvalida)
                {
                    Toast.makeText(getBaseContext(), "La password es incorrecta", Toast.LENGTH_SHORT).show();
                }
                else if (UsuarioCorrecto)
                {

                    Intent ActividadDestino;
                    ActividadDestino = new Intent(LogIn.this, ActividadPrincipal.class);
                    ActividadDestino.putExtra("Username", usernameIngresado);
                    startActivity(ActividadDestino);
                    edTxUsername.setText("");
                    edTxPassword.setText("");
                }
                else
                {
                    Toast.makeText(getBaseContext(), "El usuario no existe", Toast.LENGTH_SHORT).show();
                }
            }

        }
        @Override
        protected ArrayList<Usuarios> doInBackground(String... params) {
            String url = params[0];

            ArrayList<Usuarios> arrayUsuarios=new ArrayList<>();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                arrayUsuarios  = parsearResultado(response.body().string());

                return arrayUsuarios;

            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());                          // Error de Network o al parsear JSON
                return null;
            }
        }


        ArrayList<Usuarios> parsearResultado(String JSONstr) throws JSONException {
            ArrayList<Usuarios> usuariosArrayList = new ArrayList<>();
            JSONObject json = new JSONObject(JSONstr);                 // Convierto el String recibido a JSONObject
            JSONArray jsonUsuarios = json.getJSONArray("usuarios");  // Array - una busqueda puede retornar varios resultados
            for (int i = 0; i < jsonUsuarios.length(); i++) {
                // Recorro los resultados recibidos
                JSONObject jsonResultado = jsonUsuarios.getJSONObject(i);
                String jsonUsuario = jsonResultado.getString("Username");
                String jsonPassword =jsonResultado.getString("Password");

                Log.d("parsearResultado","Usuario: "+ jsonUsuario+"Contraseña: "+jsonPassword);
                Usuarios u = new Usuarios();
                u.set_Username(jsonUsuario);
                u.set_Password(jsonPassword);
                u.get_Username();
                u.get_Password();
                usuariosArrayList.add(u);                                                 // Agrego objeto d al array list

            }
            return usuariosArrayList;
        }
    }
    }

