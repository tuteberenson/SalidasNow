package com.example.a41396969.salidasnow;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBuscarRestaurantes extends Fragment {

    Spinner spncalidad, spnprecio, spneleccion;
    EditText txtnombre;
    String nombrebuscado;
    ArrayList<String> arrayspnprecio;
    ArrayList<String> arrayspncalidad;
    ArrayList<String> arrayspneleccion;
    ArrayAdapter<String> adapterprecio;
    ArrayAdapter<String> adaptercalidad;
    ArrayAdapter<String> adaptereleccion;
    Button btnBuscar;
    ListView listVw;


    public FragmentBuscarRestaurantes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View vista= inflater.inflate(R.layout.fragment_buscar_restaurantes, container, false);

        btnBuscar = (Button)vista.findViewById(R.id.btnBuscarRestaurantes);

        listVw=(ListView)vista.findViewById(R.id.listVwBuscoRestaurantes);

        spncalidad = (Spinner)vista.findViewById(R.id.SpnCalidad);
        spnprecio = (Spinner)vista.findViewById(R.id.SpnPrecio);
        spneleccion = (Spinner)vista.findViewById(R.id.Spneleccion);
        txtnombre = (EditText)vista.findViewById(R.id.nombbuscar);
      //  nombrebuscado = txtnombre.getText().toString().trim();
        arrayspncalidad = new ArrayList<String>();
        arrayspnprecio = new ArrayList<String>();
        arrayspneleccion = new ArrayList<String>();

        arrayspnprecio.add("Muy barato");
        arrayspnprecio.add("Barato");
        arrayspnprecio.add("Medio");
        arrayspnprecio.add("Caro");
        arrayspnprecio.add("Muy caro");

        arrayspncalidad.add("1 Estrella");
        arrayspncalidad.add("2 Estrellas");
        arrayspncalidad.add("3 Estrellas");
        arrayspncalidad.add("4 Estrellas");
        arrayspncalidad.add("5 Estrellas");

        arrayspneleccion.add("Buscar por nombre");
        arrayspneleccion.add("Buscar por calidad");
        arrayspneleccion.add("Buscar por precio");

        adaptercalidad = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, arrayspncalidad);
        adapterprecio = new ArrayAdapter<String>(getContext(),  R.layout.spinner_item, arrayspnprecio);
        adaptereleccion = new ArrayAdapter<String>(getContext(),  R.layout.spinner_item, arrayspneleccion);
        spncalidad.setAdapter(adaptercalidad);
        spnprecio.setAdapter(adapterprecio);
        spneleccion.setAdapter(adaptereleccion);
        spnprecio.setVisibility(View.INVISIBLE);
        spncalidad.setVisibility(View.INVISIBLE);
        txtnombre.setVisibility(View.INVISIBLE);

        spneleccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item is selected (in the Spinner)
             */
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An spinnerItem was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                // int posicion = spneleccion.getSelectedItemPosition();
                switch (pos) {
                    case (0):
                        txtnombre.setVisibility(View.VISIBLE);
                        spnprecio.setVisibility(View.INVISIBLE);
                        spncalidad.setVisibility(View.INVISIBLE);

                        break;
                    case (1):
                        spncalidad.setVisibility(View.VISIBLE);
                        spnprecio.setVisibility(View.INVISIBLE);
                        txtnombre.setVisibility(View.INVISIBLE);
                        break;
                    case (2):
                        spnprecio.setVisibility(View.VISIBLE);
                        spncalidad.setVisibility(View.INVISIBLE);
                        txtnombre.setVisibility(View.INVISIBLE);
                        break;
                }

            }
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing, just another required interface callback
            }

        }); // (optional)

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String Estrellas, Precio, NombreRestaurant;

                Estrellas= spncalidad.getSelectedItem().toString();
                Precio = spnprecio.getSelectedItem().toString();
                NombreRestaurant= txtnombre.getText().toString().trim();

/*                if (nombrebuscado.compareTo("")==0 && spneleccion.getSelectedItemPosition() == 0)
                {
                    Toast.makeText(getContext(), "Ingrese un nombre de restaurant", Toast.LENGTH_SHORT).show();
                }
                else{*/

                    if (spncalidad.getVisibility()== View.VISIBLE)
                    {

                        int cantEstrellas;
                        switch (Estrellas)
                        {
                            case "1 Estrella":
                                cantEstrellas=1;
                                break;
                            case "2 Estrellas":
                                cantEstrellas=2;
                                break;
                            case "3 Estrellas":
                                cantEstrellas=3;
                                break;
                            case "4 Estrellas":
                                cantEstrellas=4;
                                break;
                            case "5 Estrellas":
                                cantEstrellas=5;
                                break;
                            default:
                                cantEstrellas=0;
                                break;

                        }
                        String url = "http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byEstrellas.php?estrellas="+ cantEstrellas;
                        Log.d("url byEstrellas", url);
                        new TraerRestaurantPorEstrellas().execute(url);
                    }
                    else if (spnprecio.getVisibility()== View.VISIBLE)
                    {
                        int cantPrecio;
                        switch (Precio)
                        {
                            case "Muy barato":
                                cantPrecio = 1;
                                break;
                            case "Barato":
                                cantPrecio = 2;
                                break;
                            case "Medio":
                                cantPrecio = 3;
                                break;
                            case "Caro":
                                cantPrecio = 4;
                                break;
                            case "Muy caro":
                                cantPrecio = 5;
                                break;
                            default:
                                cantPrecio = 0;
                                break;
                        }
                        String url1 = "http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byPrecio.php?precio="+ cantPrecio;
                        Log.d("url byPrecio", url1);
                        new TraerRestaurantPorPrecio().execute(url1);
                    }
                    else if (txtnombre.getVisibility()== View.VISIBLE)
                    {

                    }
                }


            //}
        });

        return vista;
    }

    private class TraerRestaurantPorEstrellas extends AsyncTask<String,Void,ArrayList<Restaurant>>
    {

        private ProgressDialog dialog = new ProgressDialog(getContext());
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }
        @Override
        protected void onPostExecute(ArrayList<Restaurant> resultado) {
            super.onPostExecute(resultado);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if(!resultado.isEmpty())
            {
                listVw.setAdapter(new RestaurantAdapter(getContext(), resultado));

            }
        }
        @Override
        protected ArrayList<Restaurant> doInBackground(String... params) {
            String url = params[0];

            ArrayList<Restaurant> arrayRestaurantes=new ArrayList<>();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                arrayRestaurantes  = parsearResultadoRestos(response.body().string());

                return arrayRestaurantes;

            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());                          // Error de Network o al parsear JSON
                return arrayRestaurantes;
            }
        }

    }
    private class TraerRestaurantPorPrecio extends AsyncTask<String,Void,ArrayList<Restaurant>>
    {

        private ProgressDialog dialog = new ProgressDialog(getContext());
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }
        @Override
        protected void onPostExecute(ArrayList<Restaurant> resultado) {
            super.onPostExecute(resultado);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if(!resultado.isEmpty())
            {
                listVw.setAdapter(new RestaurantAdapter(getContext(), resultado));

            }
        }
        @Override
        protected ArrayList<Restaurant> doInBackground(String... params) {
            String url = params[0];

            ArrayList<Restaurant> arrayRestaurantes=new ArrayList<>();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                arrayRestaurantes  = parsearResultadoRestos(response.body().string());

                return arrayRestaurantes;

            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());                          // Error de Network o al parsear JSON
                return arrayRestaurantes;
            }
        }

    }

    ArrayList<Restaurant> parsearResultadoRestos(String JSONstr) throws JSONException {
        ArrayList<Restaurant> RestaurantArrayList = new ArrayList<>();

        JSONObject json = new JSONObject(JSONstr);                 // Convierto el String recibido a JSONObject
        //JSONObject jsonPrecio = new JSONObject("usuario");  // Array - una busqueda puede retornar varios resultados


        JSONObject jsonResultado = json.getJSONObject("restaurantes");
        String jsonNombre = jsonResultado.getString("Nombre");
        String jsonDireccion =jsonResultado.getString("Direccion");
        int jsonPrecio = jsonResultado.getInt("Precio");
        int jsonEstrellas =jsonResultado.getInt("Estrellas");
        int jsonNumeroTel = jsonResultado.getInt("NumeroTelefono");



        Log.d("parsearResulRes", "Nombre: " + jsonNombre + " Direccion: " + jsonDireccion);
        Restaurant re = new Restaurant();
        re.setPrecio(jsonPrecio);
        re.setNombre(jsonNombre);
        re.setNumeroTelefono(jsonNumeroTel);

        re.setEstrellas(jsonEstrellas);
        re.setDireccion(jsonDireccion);



        RestaurantArrayList.add(re);                                                 // Agrego objeto d al array list

        return RestaurantArrayList;
    }

}
