package com.example.a41396969.salidasnow;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ActividadLugaresCercanos extends AppCompatActivity {

    EditText direccion, numDireccion;
    TextView dirEncontrada, nombreRes;
    ListView listVW;
    ArrayAdapter<String> Adaptador;
    Spinner SPNListaDeDirecciones;
    ArrayList<String> arrayStrDirecciones;
    ArrayList<Direccion> direcciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_lugares_cercanos);
        listVW = (ListView)findViewById(R.id.listVw);
        direccion = (EditText)findViewById(R.id.direccion);
        arrayStrDirecciones=new ArrayList<>();
        SPNListaDeDirecciones =(Spinner)findViewById(R.id.SpnDirecciones);
        Adaptador = new ArrayAdapter<String>(ActividadLugaresCercanos.this, android.R.layout.simple_spinner_item,  arrayStrDirecciones);
        Adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        SPNListaDeDirecciones.setAdapter(Adaptador);
        Log.d("LLega", "00");



    }
    public void consultarRestaurantes(View v) {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(direccion.getWindowToken(), 0);

        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        Toast MToast;

        arrayStrDirecciones.clear();
        Adaptador.notifyDataSetChanged();

        if (!direccion.getText().toString().isEmpty()) {

            if (isNumeric(direccion.getText().toString())) {
                MToast = Toast.makeText(this, "No ingrese solo numeros en la direccion", Toast.LENGTH_SHORT);
                MToast.show();
            }else if(!verSiHayNums(direccion.getText().toString()))
            {
                Toast.makeText(ActividadLugaresCercanos.this, "Ingrese numeros en la direccion", Toast.LENGTH_SHORT).show();
            }
            else {
                url += direccion.getText().toString() ;  // Copio la direccion ingresada al final de la URL
                url += "&components=country:AR&key=AIzaSyA0T6Xd7zuyregCBfyon2axZWcgs1CUq-A";
                new GeolocalizacionTask().execute(url);  // Llamo a clase async con url
            }
        } else if(direccion.getText().toString().isEmpty()){

            MToast = Toast.makeText(this, "Complete los campos", Toast.LENGTH_SHORT);
            MToast.show();
            listVW.setAdapter(null);
        }
    }

    // Se conecta a Google API geocode(JavaScript). No utiliza la clase android.location.Geocoder ya que requiere API KEY
    // Parametros
    // String - la url que recibe doInBackground
    // Void -  Progreso (no se usa)
    // ArrayList<Direccion> - lo que devuelve doInBackground
    private class GeolocalizacionTask extends AsyncTask<String, Void, ArrayList<Direccion>> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPostExecute(ArrayList<Direccion> resultado) {
            super.onPostExecute(resultado);

            if ( Validacion(resultado.get(0).direccion))
            {
                Toast.makeText(ActividadLugaresCercanos.this, "Ingrese una direccion válida", Toast.LENGTH_SHORT).show();
            }
            else {

                if (resultado != null) {
                    arrayStrDirecciones.clear();
                    arrayStrDirecciones.addAll(ArrarDirecAstrYSpn(resultado));
                    Adaptador.notifyDataSetChanged();

                    SPNListaDeDirecciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int posicion, long arg3) {


                            Log.d("parametrosOITS", "" + arg0 + "" + arg1 + "" + posicion + "" + arg3);
                            new PlacesTask().execute(direcciones.get(posicion).coordenadas);
                            Log.d("OITS", direcciones.get(posicion).coordenadas);

                       /* String items = SPNListaDeDirecciones.getSelectedItem().toString();
                        Log.i("Selected item : ", items);*/
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }

                    });

                }
            }

        }
        @Override
        protected ArrayList<Direccion> doInBackground(String... params) {
            String url = params[0];

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();  // Llamado al Google API
              direcciones = parsearResultado(response.body().string());      // Convierto el resultado en ArrayList<Direccion>


                return direcciones;

            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());                          // Error de Network o al parsear JSON
                return null;
            }
        }




        // Convierte un JSON en un ArrayList de Direccion
        ArrayList<Direccion> parsearResultado(String JSONstr) throws JSONException {
            ArrayList<Direccion> direcciones = new ArrayList<>();
            JSONObject json = new JSONObject(JSONstr);                 // Convierto el String recibido a JSONObject
            JSONArray jsonDirecciones = json.getJSONArray("results");  // Array - una busqueda puede retornar varios resultados
            for (int i = 0; i < jsonDirecciones.length(); i++) {
                // Recorro los resultados recibidos
                JSONObject jsonResultado = jsonDirecciones.getJSONObject(i);
                String jsonAddress = jsonResultado.getString("formatted_address");  // Obtiene la direccion formateada

                JSONObject jsonGeometry = jsonResultado.getJSONObject("geometry");
                JSONObject jsonLocation = jsonGeometry.getJSONObject("location");
                String jsonLat = jsonLocation.getString("lat");                     // Obtiene latitud
                String jsonLng = jsonLocation.getString("lng");                     // Obtiene longitud
                String coord = jsonLat + "," + jsonLng;

                Direccion d = new Direccion(jsonAddress, coord);                    // Creo nueva instancia de direccion
                direcciones.add(d);                                                 // Agrego objeto d al array list
                Log.d("Direccion:", d.direccion + " " + coord);
            }
            return direcciones;
        }
    }
    private class PlacesTask extends AsyncTask<String, Void, ArrayList<Restaurant>> {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected ArrayList<Restaurant> doInBackground(String... params) {
            try {

            String url2 = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=restaurants";
                Log.d("Coordenadas-dibRest",""+params[0]);
            url2 += "&location="+ params[0];
            url2 += "&radius=75";
            url2 += "&key=AIzaSyA0T6Xd7zuyregCBfyon2axZWcgs1CUq-A";
             Log.d("url 2",url2);
            Request request2 = new Request.Builder()
                    .url(url2)
                    .build();
            Response response2 = client.newCall(request2).execute();

                ArrayList<Restaurant> restaurants = parsearResultado2(response2.body().string());      // Convierto el resultado en ArrayList<Direccion>
                return restaurants;

            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());                          // Error de Network o al parsear JSON
                return new ArrayList<Restaurant>();
            }
        }
        private ArrayList<Restaurant> parsearResultado2(String JSONstr) throws JSONException {
            ArrayList<Restaurant> restaurants = new ArrayList<>();
            JSONObject json = new JSONObject(JSONstr);                 // Convierto el String recibido a JSONObject
            JSONArray jsonDirecciones = json.getJSONArray("results");  // Array - una busqueda puede retornar varios resultados
            for (int i = 0; i < 10; i++) {
                // Recorro los resultados recibidos
                JSONObject jsonResultado = jsonDirecciones.getJSONObject(i);
                String jsonAddress = jsonResultado.getString("formatted_address");  // Obtiene la direccion formateada

                String jsonNom = jsonResultado.getString("name");                     // Obtiene latitud
                String jsonIcono = jsonResultado.getString("icon");
             /*  JSONObject jsonGeometry = jsonResultado.getJSONObject("geometry");
                JSONObject jsonLocation = jsonGeometry.getJSONObject("location");
                                    // Obtiene longitud
                String coord = jsonLat + "," + jsonLng;

                                 // Creo nueva instancia de direccion
                                                   // Agrego objeto d al array list
                Log.d("Direccion:",d.direccion + " " + coord);*/
                Restaurant d = new Restaurant(jsonNom, jsonAddress, jsonIcono);
                restaurants.add(d);
                Log.d("Restaurants:", d.direccion + " " + d.nombre);
            }
            return restaurants;
        }
        @Override
        protected void onPostExecute(final ArrayList<Restaurant> Lrestaurants) {
            super.onPostExecute(Lrestaurants);
            if (!Lrestaurants.isEmpty()) {
                /*
                requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
                listVW.setContentView(R.layout.main);*/
                 final Intent actividad = new Intent(ActividadLugaresCercanos.this, ActividadMapa.class);

                listVW.setAdapter(new RestaurantAdapter(ActividadLugaresCercanos.this, Lrestaurants));
                //  nombreRes.setText("Nombre: "+Lrestaurants.get(0).nombre);
                //  dirEncontrada.setText("Direccion: "+Lrestaurants.get(0).direccion);    // Muestro en pantalla la primera direccion recibida
                listVW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Restaurant unResto =Lrestaurants.get(position);
                        Log.d("Test", "00");
                        //String item = ((TextView)view).getText().toString();
                        Log.d("Test", Lrestaurants.get(position) + "");
                        Toast.makeText(getBaseContext(), unResto.nombre +"", Toast.LENGTH_LONG).show();
                        Log.d("Test", "02");

                        actividad.putExtra("Restaurant", unResto);
                        startActivity(actividad);
                    }
                });
            }
        }

    }

        public static boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }
    public ArrayList<String> ArrarDirecAstrYSpn(ArrayList<Direccion> adresses)
    {
        ArrayList<String> DireccionesStr =new ArrayList<>();
        for (Direccion d : adresses)
        {
            DireccionesStr.add(d.direccion+ ""+ d.coordenadas);
        }
        return DireccionesStr;
    }
    public boolean Validacion(String direccion)
    {
        int varSubstring=0;
        String palabraNueva="", caracter;
        boolean esNumero, resul=false;
        for (int i = 0; i < direccion.length(); i++)
        {

            if (varSubstring < direccion.length())
            {
                varSubstring=i+1;
            }
            caracter=direccion.substring(i,varSubstring);

            esNumero=isNumeric(caracter);

            if(caracter.compareTo(" ") != 0 && caracter.compareTo(".")!=0 && !esNumero && caracter.compareTo("-")!=0)
            {
                palabraNueva+=caracter;
            }
            if (esNumero || caracter.compareTo("-")==0)
            {
                i=direccion.length();
            }
        }
        if (palabraNueva.compareTo("Argentina")==0)
        {
            resul=true;
        }
            return resul;
    }
    public boolean verSiHayNums(String ingresado)
    {
        int varSubstring=0;
        String  caracter;
        boolean esNumero, resul=false;
        for (int i = 0; i < ingresado.length(); i++)
        {

            if (varSubstring < ingresado.length())
            {
                varSubstring=i+1;
            }
            caracter=ingresado.substring(i,varSubstring);

            esNumero=isNumeric(caracter);

         if (esNumero)
         {
             resul= true;
             i=ingresado.length();

         }

        }
        return resul;
    }

}
