package com.example.a41396969.salidasnow;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRecomendador extends Fragment {

    CheckBox checkBox1,checkBox2,checkBox3;
    Button btnMasOpciones, btnRecomendame;
    ArrayList<Indicadores> gArrayIndicadores;

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
                    //Recomendar: traer restaurantes etc
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

        Indicadores unIndicador;
        Random generadorAzar = new Random();

        ArrayList<String> Categorias= new ArrayList<>();
        ArrayList<Indicadores> arrayListIndicadores = new ArrayList<>();

        int numeroElegido;

        ArrayList<Integer> listaNumerosAzar=new ArrayList<>();

        for (int i =0;i<=3;i++)
        {
            if (i == 0)
            {
                numeroElegido= generadorAzar.nextInt(gArrayIndicadores.size()-1);
                listaNumerosAzar.add(numeroElegido);
                arrayListIndicadores.add(gArrayIndicadores.get(numeroElegido));
                Categorias.add(arrayListIndicadores.get(0).getCategoria());
            }
            else
            {
                numeroElegido= generadorAzar.nextInt(gArrayIndicadores.size()-1);
                for (Integer num :listaNumerosAzar ) {
                    if (num == numeroElegido)
                    {
                        numeroElegido= generadorAzar.nextInt(gArrayIndicadores.size()-1);
                    }
                }
                listaNumerosAzar.add(numeroElegido);
            }
        }

        checkBox1.setText(gArrayIndicadores.get(listaNumerosAzar.get(0)).getTexto());
        checkBox2.setText(gArrayIndicadores.get(listaNumerosAzar.get(1)).getTexto());
        checkBox3.setText(gArrayIndicadores.get(listaNumerosAzar.get(2)).getTexto());
    }

}
