package com.example.a41396969.salidasnow;

import android.app.ProgressDialog;
import android.content.DialogInterface;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class FragmentLugaresCercanos extends Fragment {

    EditText direccion, numDireccion;
    TextView dirEncontrada, nombreRes;
    ListView listVW;
    ArrayAdapter<String> Adaptador;
    Spinner SPNListaDeDirecciones;
    ArrayList<String> arrayStrDirecciones;
    ArrayList<String> direcRestaurants;
    ArrayList<Direccion> direcciones;
    Button btnMostrarEnMapa,BotonConsultar;


    public FragmentLugaresCercanos() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayStrDirecciones=new ArrayList<>();
        direcRestaurants=new ArrayList<>();

        Log.d("LLega", "00");


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View vista =inflater.inflate(R.layout.actividad_lugares_cercanos, container, false);

        Context context=getActivity();
        listVW = (ListView) vista.findViewById(R.id.listVw);
        direccion = (EditText) vista.findViewById(R.id.direccion);
        arrayStrDirecciones=new ArrayList<>();
        SPNListaDeDirecciones =(Spinner) vista.findViewById(R.id.SpnDirecciones);
        Adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,  arrayStrDirecciones);
        Adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        btnMostrarEnMapa=(Button) vista.findViewById(R.id.btn_MostrarEnMapa);


       BotonConsultar=(Button)vista.findViewById(R.id.angry_btn);
        BotonConsultar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(direccion.getWindowToken(), 0);

                if (v.getId() == R.id.angry_btn) {
                    String url = "https://maps.googleapis.com/maps/api/geocode/json?address="; //url  de API direcciones
                    Toast MToast;

                    arrayStrDirecciones.clear();
                    Adaptador.notifyDataSetChanged();

                    if (!direccion.getText().toString().isEmpty()) {

                        if (isNumeric(direccion.getText().toString())) {
                            MToast = Toast.makeText(getContext(), "No ingrese solo numeros en la direccion", Toast.LENGTH_SHORT);
                            MToast.show();
                        } else if (!verSiHayNums(direccion.getText().toString())) {
                            Toast.makeText(getContext(), "Ingrese numeros en la direccion", Toast.LENGTH_SHORT).show();
                        } else {
                            url += direccion.getText().toString();  // Copio la direccion ingresada al final de la URL
                            url += "&components=country:AR&key=AIzaSyA0T6Xd7zuyregCBfyon2axZWcgs1CUq-A";
                            new GeolocalizacionTask().execute(url);  // Llamo a clase async con url
                            direccion.setEnabled(false);
                        }
                    } else if (direccion.getText().toString().isEmpty()) {

                        MToast = Toast.makeText(getContext(), "Complete los campos", Toast.LENGTH_SHORT);
                        MToast.show();
                        listVW.setAdapter(null);
                    }
                }

            }
        });
        btnMostrarEnMapa=(Button)vista.findViewById(R.id.btn_MostrarEnMapa);
        btnMostrarEnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_MostrarEnMapa) {
                    ArrayList<String> direccsAmapa = direcRestaurants;
                    Intent actividad = new Intent(getContext(), ActividadMapa.class);

                    direccion.setEnabled(true);

                    actividad.putExtra("Direccion", direccion.getText().toString());
                    actividad.putStringArrayListExtra("Direcciones", direccsAmapa);
                    startActivity(actividad);

                }
            }
        });



        direcRestaurants=new ArrayList<>();
        SPNListaDeDirecciones.setAdapter(Adaptador);
        Log.d("LLega", "00");


        return vista;
               // super.onCreateView(inflater, container, savedInstanceState);
    }



    // Se conecta a Google API geocode(JavaScript). No utiliza la clase android.location.Geocoder ya que requiere API KEY
    // Parametros
    // String - la url que recibe doInBackground
    // Void -  Progreso (no se usa)
    // ArrayList<Direccion> - lo que devuelve doInBackground
    private class GeolocalizacionTask extends AsyncTask<String, Void, ArrayList<Direccion>> {
        private OkHttpClient client = new OkHttpClient();
        private ProgressDialog dialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Direccion> resultado) {
            super.onPostExecute(resultado);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if ( Validacion(resultado.get(0).direccion))
            {
                Toast.makeText(getContext(), "Ingrese una direccion válida", Toast.LENGTH_SHORT).show();
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
                            btnMostrarEnMapa.setVisibility(View.VISIBLE);
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
                Log.d("Coordenadas-dibRest", "" + params[0]);
                url2 += "&location=" + params[0];
                url2 += "&radius=75";
                url2 += "&key=AIzaSyA0T6Xd7zuyregCBfyon2axZWcgs1CUq-A";
                Log.d("url 2", url2);
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
                Log.d("Restaurants:", d.getDireccion() + " " + d.getNombre());
            }
            return restaurants;
        }

        @Override
        protected void onPostExecute(final ArrayList<Restaurant> Lrestaurants) {
            super.onPostExecute(Lrestaurants);
            if (!Lrestaurants.isEmpty()) {

                for (Restaurant r:Lrestaurants) {
                    direcRestaurants.add(r.getDireccion());
                }
                /*
                requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
                listVW.setContentView(R.layout.main);*/
                final Intent actividad = new Intent(getContext(), ActividadMapa.class);

                listVW.setAdapter(new RestaurantAdapter(getContext(), Lrestaurants));
                //  nombreRes.setText("Nombre: "+Lrestaurants.get(0).nombre);
                //  dirEncontrada.setText("Direccion: "+Lrestaurants.get(0).direccion);    // Muestro en pantalla la primera direccion recibida
                listVW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Restaurant unResto = Lrestaurants.get(position);
                        Log.d("Test", "00");
                        //String item = ((TextView)view).getText().toString();
                        Log.d("Test", Lrestaurants.get(position) + "");
                        Toast.makeText(getContext(), unResto.getNombre() + "", Toast.LENGTH_LONG).show();
                        Log.d("Test", "02");
                        direccion.setEnabled(false);

                        actividad.putExtra("Direccion",direccion.getText().toString());
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
