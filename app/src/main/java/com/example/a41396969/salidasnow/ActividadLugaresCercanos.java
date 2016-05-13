package com.example.a41396969.salidasnow;

import android.app.DownloadManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
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

    EditText direccion;
    TextView dirEncontrada, nombreRes;
    ListView listVW;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_lugares_cercanos);

        listVW = (ListView) findViewById(R.id.listVw);
        direccion = (EditText) findViewById(R.id.direccion);
        /*dirEncontrada = (TextView) findViewById(R.id.dirEncontrada);
        nombreRes = (TextView) findViewById(R.id.Nombre);*/


    }

    public void consultarRestaurantes(View v) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        if (!direccion.getText().toString().isEmpty()) {
            url += direccion.getText().toString();   // Copio la direccion ingresada al final de la URL
            new GeolocalizacionTask().execute(url);  // Llamo a clase async con url
        }
    }

    // Se conecta a Google API geocode(JavaScript). No utiliza la clase android.location.Geocoder ya que requiere API KEY
    // Parametros
    // String - la url que recibe doInBackground
    // Void -  Progreso (no se usa)
    // ArrayList<Direccion> - lo que devuelve doInBackground
    private class GeolocalizacionTask extends AsyncTask<String, Void, ArrayList<Restaurant>> {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPostExecute(final ArrayList<Restaurant> Lrestaurants) {
            super.onPostExecute(Lrestaurants);
            if (!Lrestaurants.isEmpty()) {
                /*
                requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
                listVW.setContentView(R.layout.main);*/

                listVW.setAdapter(new RestaurantAdapter(ActividadLugaresCercanos.this, Lrestaurants));
                //  nombreRes.setText("Nombre: "+Lrestaurants.get(0).nombre);
                //  dirEncontrada.setText("Direccion: "+Lrestaurants.get(0).direccion);    // Muestro en pantalla la primera direccion recibida
                listVW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("Test", "00");
                       //String item = ((TextView)view).getText().toString();
                        Log.d("Test",Lrestaurants.get(position)+"");
                        Toast.makeText(getBaseContext(), Lrestaurants.get(position).nombre +"", Toast.LENGTH_LONG).show();
                        Log.d("Test", "02");
                    }
                });
            }
        }

        @Override
        protected ArrayList<Restaurant> doInBackground(String... params) {
            String url = params[0];

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();  // Llamado al Google API
                ArrayList<Direccion> direcciones = parsearResultado(response.body().string());      // Convierto el resultado en ArrayList<Direccion>
                String coordenadas = direcciones.get(0).coordenadas;
                String url2 = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=restaurants";
                url2 += "&location=" + coordenadas;
                url2 += "&radius=200";
                url2 += "&key=AIzaSyA0T6Xd7zuyregCBfyon2axZWcgs1CUq-A";

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
            for (int i = 0; i < jsonDirecciones.length(); i++) {
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
                Log.d("Direccion:", d.direccion + " " + d.nombre);
            }
            return restaurants;
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

    public class Direccion {
        String direccion;
        String coordenadas;

        public Direccion(String direccion, String coordenadas) {
            this.direccion = direccion;
            this.coordenadas = coordenadas;
        }
    }

    public class Restaurant {
        String nombre;
        String direccion;
        String icono;

        public Restaurant(String nombre, String direccion, String icono) {
            this.direccion = direccion;
            this.nombre = nombre;
            this.icono = icono;
        }
    }

    public class RestaurantAdapter extends BaseAdapter {

        ArrayList<Restaurant> restaurants;
        Context context;

        public RestaurantAdapter(Context context, ArrayList<Restaurant> restaurants) {
            this.context = context;
            this.restaurants = restaurants;
        }

        @Override
        public int getCount() {
            return restaurants.size();
        }

        @Override
        public Object getItem(int i) {
            return restaurants.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item, viewGroup, false);
            }

            TextView nombreTV = (TextView) view.findViewById(R.id.nombre);
            TextView direccionTV = (TextView) view.findViewById(R.id.direc);

            Restaurant p = this.restaurants.get(position);
            nombreTV.setText(p.nombre);
            direccionTV.setText(p.direccion);
            return view;
        }
    }

}
