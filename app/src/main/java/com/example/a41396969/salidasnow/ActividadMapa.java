package com.example.a41396969.salidasnow;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActividadMapa extends AppCompatActivity  implements OnMapReadyCallback {

    GoogleMap googleMap;
    ArrayList<Direccion> coordenadasRestaurant;
    ArrayList<String> arrayDirecc;
    TextView txDireccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_mapa);

        txDireccion = (TextView)findViewById(R.id.TextVwActivityMapa);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent ActividadRecibida = getIntent();

        txDireccion.setText(ActividadRecibida.getStringExtra("Direccion"));
        arrayDirecc=new ArrayList<>();

        if (ActividadRecibida.getSerializableExtra("Restaurant")!=null) {
            Restaurant RestoSerialised = (Restaurant) ActividadRecibida.getSerializableExtra("Restaurant");
            String url = RestoSerialised.getDireccion();
            Log.d("url",url);
            new GeolocalizacionTask().execute(url);
        }
        else
        {
            arrayDirecc = ActividadRecibida.getStringArrayListExtra("Direcciones");
            {
                for (String d : arrayDirecc)
                {
                    new GeolocalizacionTask().execute(d);
                }
            }
        }
        //String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";
         // Copio la direccion ingresada al final de la URL
        //url += "&components=country:AR&key=AIzaSyA0T6Xd7zuyregCBfyon2axZWcgs1CUq-A";

        //Esto está mal hay que mandarle la lat y long del restaurant para eso hay que volver a mandar la direc del restaurant
        //Y que te devuelva las coordenadas en in para poner acá abajo--
        //Seguir tutorial de http://www.tutorialspoint.com/android/android_google_maps.htm
        //Y https://github.com/lgalant/GeolocationAPIandMap/blob/master/app/src/main/java/ar/edu/ort/geolocationapiandmap/MainActivity.java#L67

    }


    // Utiliza la clase android.location.Geocoder
    // Parametros
    // String - la direccion a buscar que recibe doInBackground
    // Void -  Progreso (no se usa)
    // List<Address> - lo que devuelve doInBackground
    private class GeolocalizacionTask extends AsyncTask<String, Void,List<Address>> {

        @Override
        protected void onPostExecute(List<Address> direcciones) {
            super.onPostExecute(direcciones);
                Log.d("Direcciones",direcciones+"");
            if (!direcciones.isEmpty()) {
                // Muestro la primera direccion recibida
                Address dirRecibida = direcciones.get(0);  // La primera direccion
                Log.d("direcRecibida",dirRecibida+"");
                //String addressStr = dirRecibida.getAddressLine(0);  // Primera linea del texto
                //dirEncontrada.setText(addressStr);

                // Muestro coordenadas
                double lat = dirRecibida.getLatitude(); //
                double lng = dirRecibida.getLongitude();
                Log.d("latitud",lat+"");
                Log.d("Longitud",lng+"");
                //String coordStr = lat + "," + lng;
                //coordenadas.setText(coordStr);  // Muestro coordenadas en pantalla

                // Ubico la direccion en el mapa
                if (googleMap != null) {
                    CameraUpdate center =
                            CameraUpdateFactory.newLatLng(new LatLng(lat, lng));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                    googleMap.moveCamera(center);
                    googleMap.animateCamera(zoom);   // Posiciono la camara en las coordenadas recibidas


                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, lng))
                            .title(dirRecibida.getAddressLine(0)));// Dibujo el marker
                }
            }
        }

        @Override
        protected List<Address> doInBackground(String... params) {
            String address = params[0];

            Log.d("LLega DIB","-01");
            Geocoder geocoder = new Geocoder(getApplicationContext());
            Log.d("LLega DIB","00");
            List<Address> addresses = null;
            try {
                Log.d("LLega DIB",address+"");
                // Utilizo la clase Geocoder para buscar la direccion. Limito a 1 resultado
                addresses = geocoder.getFromLocationName(address, 1);
                Log.d("LLega DIB","02");
            } catch (IOException e) {
                Log.d("LLega DIB","error");
                e.printStackTrace();
            }
            Log.d("LLega DIB","");
            return addresses;
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        map.getUiSettings().setZoomControlsEnabled(false);
    }

    public String sacarEspacios(String palabra) {

        int varSubstring=0;
        String palabraNueva="", caracter;
        for (int i = 0; i < palabra.length(); i++)
        {

            if (varSubstring < palabra.length())
            {
                varSubstring=i+1;
            }
            caracter=palabra.substring(i,varSubstring);

            caracter=tieneTilde(caracter);

            if(caracter.compareTo(" ") != 0 && caracter.compareTo(".")!=0 && caracter.compareTo(",")!=0)
            {
                palabraNueva+=caracter;
            }
            if (caracter.compareTo(",")==0)
            {
                i=palabra.length();
            }
        }
        palabraNueva+="CABA";
        return palabraNueva;
    }
    public String tieneTilde(String letra)
    {
        letra=letra.toLowerCase();
        String letraResultado;
        switch (letra)
        {
            case "á":
                letraResultado="a";
                break;
            case "é":
                letraResultado="e";
                break;
            case "í":
                letraResultado="í";
                break;
            case "ó":
                letraResultado="o";
                break;
            case "ú":
                letraResultado="u";
                break;
            default:
                letraResultado=letra;
                break;
        }

        return letraResultado;
    }
}
/*private class GeolocalizacionTask extends AsyncTask<String, Void, ArrayList<Direccion>> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPostExecute(ArrayList<Direccion> direccion) {
            super.onPostExecute(direccion);

            if (direccion != null) {

               /* double latitud=Double.parseDouble(coordenadas.get(0));
                double longitud=Double.parseDouble(coordenadas.get(1));
                //Log.d("Lat y lng",latitud+""+longitud);
                final LatLng TutorialsPoint = new LatLng(15,12);
                Marker TP = googleMap.addMarker(new MarkerOptions().position(TutorialsPoint).title("TutorialsPoint"));
            }
        }
        @Override
        protected ArrayList<Direccion> doInBackground(String... params) {
            String url = params[0];

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            ArrayList<Direccion> addresses = null;
            Geocoder geocoder = new Geocoder(getApplicationContext());
            try {
                Response response = client.newCall(request).execute();  // Llamado al Google API
               // coordenadasRestaurant = parsearResultado(response.body().string());      // Convierto el resultado en ArrayList<Direccion>
                addresses = geocoder.getFromLocationName(address, 10);

                return coordenadasRestaurant;

            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());                          // Error de Network o al parsear JSON
                return null;
            }
            String address = params[0];




            return addresses;
        }
        }


        // Convierte un JSON en un ArrayList de Direccion
        ArrayList<Direccion> parsearResultado(String JSONstr) throws JSONException {
            ArrayList<Direccion> direc = new ArrayList<>();
            JSONObject json = new JSONObject(JSONstr);                 // Convierto el String recibido a JSONObject
            JSONArray jsonDirecciones = json.getJSONArray("results");  // Array - una busqueda puede retornar varios resultados


            JSONObject jsonResultado = jsonDirecciones.getJSONObject(0);
            JSONArray jsonAddress = jsonResultado.getJSONArray("address_components");  // Obtiene la direccion formateada

           Log.d("jsonAdress", jsonAddress.getJSONObject(0)+"");

            String jsonType = jsonResultado.getString("types");
            Log.d("jsonType",jsonType);
           /* JSONObject jsonLocation = jsonGeometry.getJSONObject("location");
            String jsonLat = jsonLocation.getString("lat");                     // Obtiene latitud
            String jsonLng = jsonLocation.getString("lng");                     // Obtiene longitud
            String lat = jsonLat;
            String lng = jsonLng;

            //Restaurant d = new Restaurant("","",coord,"");                    // Creo nueva instancia de direccion
           /* coord.add(lat);
            coord.add(lng);
            Log.d("coordenadas:",lat+","+lng);


            return direc;
        }
    }*/