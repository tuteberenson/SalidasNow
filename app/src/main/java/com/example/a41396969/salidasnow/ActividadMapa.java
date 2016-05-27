package com.example.a41396969.salidasnow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActividadMapa extends AppCompatActivity  implements OnMapReadyCallback {

    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_mapa);


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent ActividadRecibida = getIntent();

        Restaurant RestoSerialised = (Restaurant)ActividadRecibida.getSerializableExtra("Restaurant");
        //Esto está mal hay que mandarle la lat y long del restaurant para eso hay que volver a mandar la direc del restaurant
        //Y que te devuelva las coordenadas en in para poner acá abajo--
        //Seguir tutorial de http://www.tutorialspoint.com/android/android_google_maps.htm
        //Y https://github.com/lgalant/GeolocationAPIandMap/blob/master/app/src/main/java/ar/edu/ort/geolocationapiandmap/MainActivity.java#L67
        final LatLng TutorialsPoint = new LatLng(21 , 57);
        Marker TP = googleMap.addMarker(new MarkerOptions().position(TutorialsPoint).title("TutorialsPoint"));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        map.getUiSettings().setZoomControlsEnabled(false);
    }
}
