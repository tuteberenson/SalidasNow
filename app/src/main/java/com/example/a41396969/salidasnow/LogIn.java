package com.example.a41396969.salidasnow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Parcelable;
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
import java.lang.reflect.Array;
import java.util.ArrayList;

public class LogIn extends AppCompatActivity {


    EditText edTxUsername, edTxPassword;
    String passwordIngresada, usernameIngresado;
    boolean UsuarioCorrecto, passInvalida;
    ArrayList<Usuarios> listaUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        edTxPassword=(EditText)findViewById(R.id.passwordLogIn);
        edTxUsername=(EditText)findViewById(R.id.usuarioLogIn);
        listaUsuario=new ArrayList<>();
    }

    public void Click_generalLogIn(View vista) {

        Resources recursos;
        recursos = getResources();
        passwordIngresada = edTxPassword.getText().toString().trim();
        usernameIngresado = edTxUsername.getText().toString().trim();

        if (usernameIngresado.compareTo("") == 0) {

            Toast.makeText(LogIn.this, "Ingrese usuario", Toast.LENGTH_SHORT).show();
        } else if (passwordIngresada.compareTo("") == 0)
        {
            Toast.makeText(LogIn.this, "Ingrese contraseña", Toast.LENGTH_SHORT).show();
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
        private ProgressDialog dialog = new ProgressDialog(LogIn.this);
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }
        @Override
        protected void onPostExecute(ArrayList<Usuarios> resultado) {
            super.onPostExecute(resultado);
             UsuarioCorrecto =false;
            passInvalida=false;

            dialog.dismiss();
            int comparadorUser, comparadorPass;

            if (resultado != null) {

                for (Usuarios unUsuario :resultado) {

                    comparadorUser= unUsuario.get_Username().compareTo(usernameIngresado);
                    comparadorPass=unUsuario.get_Password().compareTo(passwordIngresada);

                    if (comparadorUser== 0 && comparadorPass ==0)
                    {
                        UsuarioCorrecto =true;
                        String url = "http://salidasnow.hol.es/obtener_usuario_byId.php?idusuario="+unUsuario.get_idUsuario();
                        new TraerUsuarioPorIdAsyncTask().execute(url);

                        Log.d("Usuario","true");

                    }
                    else if (comparadorUser== 0 && comparadorPass !=0)
                    {
                        passInvalida=true;
                    }

                }
                if (passInvalida) {
                    Toast.makeText(getBaseContext(), "La password es incorrecta", Toast.LENGTH_SHORT).show();
                }
                else if(!UsuarioCorrecto)
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
                String jsonNombre = jsonResultado.getString("Nombre");
                String jsonApellido =jsonResultado.getString("Apellido");
                int jsonIdUsuario = jsonResultado.getInt("idUsuario");
                String jsonNombreImg =jsonResultado.getString("NombreImg");


                Log.d("parsearResultado1","Usuario: "+ jsonUsuario+" Contraseña: "+jsonPassword);
                Usuarios u = new Usuarios();
                u.set_Nombre(jsonNombre);
                u.set_Apellido(jsonApellido);
                u.set_idUsuario(jsonIdUsuario);
                u.set_NombreImg(jsonNombreImg);
                u.set_Username(jsonUsuario);
                u.set_Password(jsonPassword);

              /*  u.get_Username();
                u.get_Password();
                u.get_Apellido();
                u.get_Nombre();
                u.get_NombreImg();
                u.get_idUsuario();*/
                usuariosArrayList.add(u);                                                 // Agrego objeto d al array list
            }
            return usuariosArrayList;
        }
    }
    private class TraerUsuarioPorIdAsyncTask extends AsyncTask<String,Void,ArrayList<Usuarios>>
    {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPostExecute(ArrayList<Usuarios> resultado) {
            super.onPostExecute(resultado);

            if (UsuarioCorrecto) {
                Intent ActividadDestino;
                ActividadDestino = new Intent(LogIn.this, ActividadPrincipal.class);
                Log.d("ListaUsuario", listaUsuario.get(0).toString());
                Bundle paquete = new Bundle();
                paquete.putSerializable("listaUsuario", listaUsuario);
                ActividadDestino.putExtras(paquete);
                startActivity(ActividadDestino);
                edTxUsername.setText("");
                edTxPassword.setText("");
            }
/*
            if (resultado != null) {

                Usuarios u=new Usuarios();
                u.set_idUsuario((resultado.get(0).get_idUsuario()));//HAcerrr esto para los de abajo tambien yla lista la voy amanda r a lam otra activityy
                u.set_Username(resultado.get(0).get_Username());
                u.set_Password(resultado.get(0).get_Password());
                u.set_Apellido(resultado.get(0).get_Apellido());
                u.set_Nombre(resultado.get(0).get_Nombre());
                u.set_NombreImg(resultado.get(0).get_NombreImg());
                listaUsuario.add(u);
            }*/

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
                arrayUsuarios  = parsearResultado2(response.body().string());

                return arrayUsuarios;

            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());                          // Error de Network o al parsear JSON
                return arrayUsuarios;
            }
        }


        ArrayList<Usuarios> parsearResultado2(String JSONstr) throws JSONException {
            ArrayList<Usuarios> usuariosArrayList = new ArrayList<>();

            JSONObject json = new JSONObject(JSONstr);                 // Convierto el String recibido a JSONObject
            //JSONObject jsonUsuario = new JSONObject("usuario");  // Array - una busqueda puede retornar varios resultados


                JSONObject jsonResultado = json.getJSONObject("usuario");
                String jsonUsuario = jsonResultado.getString("Username");
                String jsonPassword =jsonResultado.getString("Password");
                String jsonNombre = jsonResultado.getString("Nombre");
                String jsonApellido =jsonResultado.getString("Apellido");
                int jsonIdUsuario = jsonResultado.getInt("idUsuario");
                String jsonNombreImg =jsonResultado.getString("NombreImg");


                Log.d("parsearResultado2", "Usuario: " + jsonUsuario + " Contraseña: " + jsonPassword);
                Usuarios u = new Usuarios();
                u.set_Nombre(jsonNombre);
                u.set_Apellido(jsonApellido);
                u.set_idUsuario(jsonIdUsuario);
                u.set_NombreImg(jsonNombreImg);
                u.set_Username(jsonUsuario);
                u.set_Password(jsonPassword);

               /* u.get_Username();
                u.get_Password();
                u.get_Apellido();
                u.get_Nombre();
                u.get_NombreImg();
                u.get_idUsuario();*/

                usuariosArrayList.add(u);                                                 // Agrego objeto d al array list
                listaUsuario.add(u);
            return usuariosArrayList;
        }
    }
}

