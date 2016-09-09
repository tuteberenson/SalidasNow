package com.example.a41396969.salidasnow;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import okio.ForwardingTimeout;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRecomendador extends Fragment {

    CheckBox checkBox1,checkBox2,checkBox3;
    Button btnMasOpciones, btnRecomendame;
    ArrayList<Indicadores> gArrayIndicadores,indicadoresAlAzar;
    ListView listVw;
    RestaurantAdapterCompleto adaptadorRestos;

    public FragmentRecomendador() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_recomendador, container, false);

        checkBox1 = (CheckBox)vista.findViewById(R.id.chbox1);
        checkBox2 = (CheckBox)vista.findViewById(R.id.chbox2);
        checkBox3 = (CheckBox)vista.findViewById(R.id.chbox3);

        btnMasOpciones= (Button)vista.findViewById(R.id.btnMasOpc);
        btnRecomendame = (Button)vista.findViewById(R.id.btnRecomendame);

        listVw=(ListView)vista.findViewById(R.id.listVwRecomendador);

        gArrayIndicadores=new ArrayList<>();

        String url ="http://salidasnow.hol.es/Indicadores/obtener_indicadores.php";

        Log.d("url solicitada",url);

        new AsyncTaskIndicadores().execute(url);


        btnMasOpciones.setOnClickListener(new View.OnClickListener()
        {
          @Override
          public void onClick(View v) {
                OrdenarRdm();

              checkBox1.setChecked(false);
              checkBox2.setChecked(false);
              checkBox3.setChecked(false);
          }
      });

        btnRecomendame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkBox1.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked())
                {
                    Toast.makeText(getContext(), "Seleccione al menos una opción", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ArrayList<String> listaCatsSeleccionadas=new ArrayList<>();
                    if (checkBox1.isChecked())
                    {
                        listaCatsSeleccionadas.add(indicadoresAlAzar.get(0).getCategoria());
                    }
                    else if (checkBox2.isChecked())
                    {
                        listaCatsSeleccionadas.add(indicadoresAlAzar.get(1).getCategoria());
                    }
                    else if (checkBox3.isChecked())
                    {
                        listaCatsSeleccionadas.add(indicadoresAlAzar.get(2).getCategoria());
                    }

                    for (String unaCat:listaCatsSeleccionadas)
                    {
                        String urlAEjecutar1;

                        switch (unaCat)
                        {
                            case "Precio":
                                if (indicadoresAlAzar.get(0).getValor()==2)
                                {
                                    urlAEjecutar1="http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byParametros.php?condicion=igual&valor=3&parametroW=Precio";
                                }
                                else if (indicadoresAlAzar.get(0).getValor()>2)
                                {
                                    urlAEjecutar1="http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byParametros.php?condicion=mayor&valor=3&parametroW=Precio";
                                }
                                else
                                {
                                    urlAEjecutar1="http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byParametros.php?condicion=menor&valor=3&parametroW=Precio";
                                }
                                break;
                            case "Ambientacion":
                                if (indicadoresAlAzar.get(0).getValor()==2)
                                {
                                    urlAEjecutar1="http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byParametros.php?condicion=igual&valor=3&parametroW=Precio";
                                }
                                else if (indicadoresAlAzar.get(0).getValor()>2)
                                {
                                    urlAEjecutar1="http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byParametros.php?condicion=mayor&valor=3&parametroW=Estrellas";
                                }
                                else
                                {
                                    urlAEjecutar1="http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byParametros.php?condicion=menor&valor=3&parametroW=Precio";
                                }
                                break;
                            case "Calidad":
                                if (indicadoresAlAzar.get(0).getValor()==2)
                                {
                                    urlAEjecutar1="http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byParametros.php?condicion=igual&valor=3&parametroW=Estrellas";
                                }
                                else if (indicadoresAlAzar.get(0).getValor()>2)
                                {
                                    urlAEjecutar1="http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byParametros.php?condicion=mayor&valor=3&parametroW=Estrellas";
                                }
                                else
                                {
                                    urlAEjecutar1="http://salidasnow.hol.es/Restaurantes/obtener_restaurantes_byParametros.php?condicion=menor&valor=3&parametroW=Estrellas";
                                }
                                break;
                            default:
                                Toast.makeText(getContext(), "Hubo un error", Toast.LENGTH_SHORT).show();
                                urlAEjecutar1="";
                                break;
                        }
                        Log.d("btnRecomendame","url a ejecutar: "+ urlAEjecutar1);
                        new TraerRestaurantsPorParametro().execute(urlAEjecutar1);
                    }
                }

            }
        });


        return vista;
    }

    private class AsyncTaskIndicadores extends AsyncTask<String, Void,ArrayList<Indicadores>>
    {
        private ProgressDialog dialog = new ProgressDialog(getContext());
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected ArrayList<Indicadores> doInBackground(String... params) {
            String url = params[0];

            ArrayList<Indicadores> arrayIndicadores=new ArrayList<>();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                Response response = client.newCall(request).execute();

                String strResponseBody = response.body().string();

                Log.d("response body",strResponseBody);


                arrayIndicadores = parsearResultadoIndicadores(strResponseBody);


                return arrayIndicadores;
            }catch (IOException | JSONException e)
            {
                Log.d("Error", e.getMessage());
                return arrayIndicadores;
            }
        }

        private ArrayList<Indicadores> parsearResultadoIndicadores(String JSONstr) throws JSONException {

            ArrayList<Indicadores> listaIndicadores=new ArrayList<>();

            JSONObject json = new JSONObject(JSONstr);

            JSONArray jsonIndicadores= json.getJSONArray("indicadores");

            if (jsonIndicadores.length()==0)
            {
                Toast.makeText(getContext(), "aaa", Toast.LENGTH_LONG).show();
            }

            for (int i=0; i<jsonIndicadores.length();i++) {

                JSONObject jsonResultado= jsonIndicadores.getJSONObject(i);

                String jsonTextoIndicador =jsonResultado.getString("Texto");
                String jsonCategoriaIndicador= jsonResultado.getString("Categoria");
                int jsonValorIndicador= jsonResultado.getInt("Valor");

                Indicadores unIndicador= new Indicadores();

                unIndicador.setTexto(jsonTextoIndicador);
                unIndicador.setCategoria(jsonCategoriaIndicador);
                unIndicador.setValor(jsonValorIndicador);

                listaIndicadores.add(unIndicador);

            }



            return listaIndicadores;
    }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Espere por favor");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Indicadores> resultadoIndicadores) {
            super.onPostExecute(resultadoIndicadores);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (!resultadoIndicadores.isEmpty())
            {
                 gArrayIndicadores.addAll(resultadoIndicadores);
                Log.d("IndicadorArray",gArrayIndicadores.get(0).getTexto());
                 OrdenarRdm();
            }
            else
            {
                Toast.makeText(getContext(), "No se pudieron obtener los indicadores", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void OrdenarRdm()
    {


        Random generadorAzar = new Random();

        String texto1="", texto2="", texto3="", cat1,cat2,cat3;
        int comparadoresT12,comparadoresT13,comparadoresT23, comparadoresC12,comparadoresC13, comparadoresC23;
       indicadoresAlAzar = new ArrayList<>();

        int numeroElegido;

        ArrayList<Integer> listaNumerosAzar=new ArrayList<>();

        boolean volverAGenerar= true;

       while (volverAGenerar) {

               for (int i = 0; i <= 3; i++) {

              // if (i == 0) {
                   numeroElegido = generadorAzar.nextInt(gArrayIndicadores.size() - 1);
                   listaNumerosAzar.add(numeroElegido);
                   indicadoresAlAzar.add(gArrayIndicadores.get(numeroElegido));
                   //Categorias.add(indicadoresAlAzar.get(i).getCategoria());
               /*} else {
                   numeroElegido = generadorAzar.nextInt(gArrayIndicadores.size() - 1);
                   for (Integer num : listaNumerosAzar) {
                       if (num == numeroElegido) {
                           numeroElegido = generadorAzar.nextInt(gArrayIndicadores.size() - 1);
                       }
                   }
                   listaNumerosAzar.add(numeroElegido);
               }*/
           }



           texto1= indicadoresAlAzar.get(0).getTexto();
           texto2= indicadoresAlAzar.get(1).getTexto();
           texto3= indicadoresAlAzar.get(2).getTexto();

           cat1= indicadoresAlAzar.get(0).getCategoria();
           cat2= indicadoresAlAzar.get(1).getCategoria();
           cat3= indicadoresAlAzar.get(2).getCategoria();

           comparadoresT12=texto1.compareTo(texto2);
           comparadoresT13=texto1.compareTo(texto3);
           comparadoresT23=texto2.compareTo(texto3);

           comparadoresC12=cat1.compareTo(cat2);
           comparadoresC13=cat1.compareTo(cat3);
           comparadoresC23=cat2.compareTo(cat3);

           if (comparadoresC12 != 0 && comparadoresC13 !=0 && comparadoresC23!= 0 &&
                   comparadoresT12!=0 && comparadoresT13!=0 && comparadoresT23!=0)
           {
               volverAGenerar=false;
           }
           else
           {
               listaNumerosAzar.clear();
               indicadoresAlAzar.clear();
           }


       }
        checkBox1.setText(texto1);
        checkBox2.setText(texto2);
        checkBox3.setText(texto3);
    }

    private class TraerRestaurantsPorParametro extends AsyncTask<String, Void, ArrayList<Restaurant>>
    {
        private ProgressDialog dialog = new ProgressDialog(getContext());
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }
        @Override
        protected void onPostExecute(final ArrayList<Restaurant> resultadoRestaurants) {
            super.onPostExecute(resultadoRestaurants);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if(!resultadoRestaurants.isEmpty())
            {
                adaptadorRestos = new RestaurantAdapterCompleto(getContext(), resultadoRestaurants);
                listVw.setAdapter(adaptadorRestos);

                final Intent actividad = new Intent(getContext(), ActividadMapa.class);

                listVw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Restaurant unResto = resultadoRestaurants.get(position);
                        Log.d("Test", "00");
                        //String item = ((TextView)view).getText().toString();
                        Log.d("Test", resultadoRestaurants.get(position) + "");
                        Toast.makeText(getContext(), unResto.getNombre() + "", Toast.LENGTH_LONG).show();
                        Log.d("Test", "02");


                        actividad.putExtra("Direccion",unResto.getDireccion());
                        actividad.putExtra("Restaurant", unResto);
                        startActivity(actividad);
                    }
                });
            }
            else
            {
                Toast.makeText(getContext(), "No hay restaurantes con ese criterio", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected ArrayList<Restaurant> doInBackground(String... params) {
            String url = params[0];

            Log.d("url doInB Precio",url);
            ArrayList<Restaurant> arrayRestaurantes=new ArrayList<>();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                arrayRestaurantes  = parsearResultadoRestosPrecio(response.body().string());

                return arrayRestaurantes;

            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());                          // Error de Network o al parsear JSON
                return arrayRestaurantes;
            }
        }


        ArrayList<Restaurant> parsearResultadoRestosPrecio(String JSONstr) throws JSONException {
            ArrayList<Restaurant> RestaurantArrayList = new ArrayList<>();

            JSONObject json = new JSONObject(JSONstr);                 // Convierto el String recibido a JSONObject
            //JSONObject jsonPrecio = new JSONObject("usuario");  // Array - una busqueda puede retornar varios resultados


            JSONArray jsonRestaurantes = json.getJSONArray("restaurantes");

            int condicion;
            if (jsonRestaurantes.length()>10)
            {
                condicion=10;
            }
            else
            {
                condicion=jsonRestaurantes.length();
            }

            for (int i=0;i<condicion;i++) {

                JSONObject jsonResultado = jsonRestaurantes.getJSONObject(i);

                String jsonNombre = jsonResultado.getString("Nombre");
                String jsonDireccion = jsonResultado.getString("Direccion");
                int jsonPrecio = jsonResultado.getInt("Precio");
                int jsonEstrellas = jsonResultado.getInt("Estrellas");
                int jsonNumeroTel = jsonResultado.getInt("NumeroTelefono");
                double jsonLatitud =(double)jsonResultado.getInt("Latitud");
                double jsonLongitud =(double)jsonResultado.getInt("Longitud");

                Log.d("parsearResulRes", "Nombre: " + jsonNombre + " Direccion: " + jsonDireccion);
                Restaurant re = new Restaurant();
                re.setPrecio(jsonPrecio);
                re.setNombre(jsonNombre);
                re.setNumeroTelefono(jsonNumeroTel);
                re.setLatitud(jsonLatitud);
                re.setLongitud(jsonLongitud);
                re.setEstrellas(jsonEstrellas);
                re.setDireccion(jsonDireccion);


                RestaurantArrayList.add(re);                                                 // Agrego objeto d al array list
                Log.d("RestaurantArrayList", "Precio:" +RestaurantArrayList.get(0).getPrecio());
            }
            return RestaurantArrayList;
        }
    }
}
