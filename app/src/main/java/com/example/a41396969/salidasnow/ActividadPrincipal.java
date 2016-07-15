package com.example.a41396969.salidasnow;

//import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ActividadPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txVwNombre,txVwInfo;
    ImageView imgVwIcono;
    Restaurant restaurant;
    ArrayList<Usuarios> UsuarioActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle paquete;
        paquete=new Bundle();

        UsuarioActual=new ArrayList<>();
        paquete = getIntent().getExtras();
        UsuarioActual = (ArrayList<Usuarios>)paquete.get("listaUsuario");



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Usuarios usu=new Usuarios();
        txVwNombre=(TextView)navigationView.getHeaderView(0).findViewById(R.id.textViewSideBar1);
        txVwInfo=(TextView)navigationView.getHeaderView(0).findViewById(R.id.textViewSideBar2);
        imgVwIcono=(ImageView)navigationView.getHeaderView(0).findViewById(R.id.imageViewSideBar);


        Picasso.with(getApplicationContext())
                .load("http://salidasnow.hol.es/images/"+UsuarioActual.get(0).get_NombreImg())
                .fit()
                .into(imgVwIcono);
        txVwNombre.setText(UsuarioActual.get(0).get_Nombre()+""+UsuarioActual.get(0).get_Apellido());
        txVwInfo.setText("Username: " + UsuarioActual.get(0).get_Username());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actividad_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        ImageView imgView=(ImageView)findViewById(R.id.imgSDN);
        imgView.setVisibility(View.INVISIBLE);
        int id = item.getItemId();
        Fragment fragment;
        if (id == R.id.nav_camera) {
            // Handle the camera action
            android.app.FragmentTransaction FT = getFragmentManager().beginTransaction();
             fragment = new FragmentLugaresCercanos();
            FT.replace(R.id.layoutPrincipal,fragment)
                    .commit();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setUnRestaurant(Restaurant r)
    {
        restaurant =r;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

}
