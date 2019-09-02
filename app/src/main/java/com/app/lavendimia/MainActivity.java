package com.app.lavendimia;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.format.Time;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.app.lavendimia.Utilidades.Servicios;
import com.app.lavendimia.Utilidades.Utilidades;
import com.app.lavendimia.configuracion.apiConfiguracion;
import com.app.lavendimia.configuracion.apiConfiguraciones;
import com.app.lavendimia.configuracion.mainConfiguracion;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, fragment_ventas.OnFragmentInteractionListener,fragment_configuracion.OnFragmentInteractionListener, fragment_articulos.OnFragmentInteractionListener,fragment_clientes.OnFragmentInteractionListener,
                    fragment_principal.OnFragmentInteractionListener, fragment_editadcliente.OnFragmentInteractionListener, fragment_editadarticulo.OnFragmentInteractionListener, fragment_nuevaVenta.OnFragmentInteractionListener{

    private Retrofit retro;
    private Servicios service;
    private Call<apiConfiguraciones> requestConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        obtieneConfiguracion obtieneConfiguracion = new obtieneConfiguracion();
        obtieneConfiguracion.execute();
        showHome();
    }

    private void showHome() {
        fra = new fragment_principal();
        executeManageR();
    }

    private static final int INTERVALO = 250; //2 segundos para salir
    private long tiempoPrimerClick;

    private boolean avisoSalir= false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(avisoSalir == false) {
            Toast.makeText(this, getResources().getString(R.string.precionaparasalir), Toast.LENGTH_SHORT).show();
            avisoSalir = true;
        }
        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
            finish();
        }

        tiempoPrimerClick = System.currentTimeMillis();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(fra == null){
                showHome();
            } else if(Utilidades.puedeRegresar == false) {
                drawer.openDrawer(GravityCompat.START);
            }
            else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_fecha);
        Time hoy = new Time(Time.getCurrentTimezone());
        hoy.setToNow();
        int dia = hoy.monthDay;
        int mes = hoy.month;
        int año = hoy.year;
        menuItem.setTitle(getResources().getString(R.string.action_fecha)+ " "+ String.valueOf(dia) + "/"
        + String.valueOf(mes+1) + "/" + String.valueOf(año));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_fecha) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    static Fragment fra = null;
    FragmentManager manager = null;
    boolean FragmentSeleccionado = false;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ventas) {
            // Handle the camera action
            fra = new fragment_ventas();
            FragmentSeleccionado = true;
            Utilidades.puedeRegresar = false;
        } else if (id == R.id.nav_clientes) {
            fra = new fragment_clientes();
            FragmentSeleccionado = true;
            Utilidades.puedeRegresar = false;
        } else if (id == R.id.nav_articulos) {
            fra = new fragment_articulos();
            FragmentSeleccionado = true;
            Utilidades.puedeRegresar = false;
        } else if (id == R.id.nav_configuracion) {
            fra = new fragment_configuracion();
            FragmentSeleccionado = true;
            Utilidades.puedeRegresar = false;
        }

        if(FragmentSeleccionado){
            executeManageR();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void executeManageR() {
        manager = getSupportFragmentManager();
        manager.beginTransaction().addToBackStack(null).replace(R.id.idu_contenedor,fra).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    //ASSYNCRONOS
    class obtieneConfiguracion extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            requestConfig.enqueue(new Callback<apiConfiguraciones>() {
                @Override
                public void onResponse(Call<apiConfiguraciones> call, Response<apiConfiguraciones> response) {
                    if (!response.isSuccessful()) {
                        //    Toast.makeText(MainActivity.this, "" + response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        apiConfiguraciones config = response.body();
                        //Toast.makeText(getActivity(), ""+animes.Animes, Toast.LENGTH_SHORT).show();
                        if (config.estado== 1) {
                            List<mainConfiguracion> listConfig = new ArrayList<>();
                            for(apiConfiguracion c : config.configuracion) {
                                listConfig.add(new mainConfiguracion(c.conf_id,c.conf_tasafinanciamiento,c.conf_plazomaximo,c.conf_porcentajeenganche));
                            }

                            Utilidades.tieneConfig = true;
                            Utilidades.plazo = listConfig.get(0).getPlazoMaximo();
                            Utilidades.tasa = listConfig.get(0).getTasaFinanciamiento();
                            Utilidades.enganche = listConfig.get(0).getPorcentajeEnganche();

                        } else {
                            Utilidades.tieneConfig = false;
                        }
                    }
                }

                @Override
                public void onFailure(Call<apiConfiguraciones> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Error 2:" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    //recycler.setVisibility(View.VISIBLE);
                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            retro = new Retrofit.Builder()
                    .baseUrl(service.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retro.create(Servicios.class);
            requestConfig = service.obtenerConfiguracion();//"1".toString());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
