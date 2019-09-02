package com.app.lavendimia;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class fragment_configuracion extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    servicioConfiguracion serviceConfiguracion;
    obtieneConfiguracion obtieneConfiguracion;
    private View vista;
    private Button btnGuardar;
    private Button btnCancelar;
    private EditText etTasa;
    private EditText etEnganche;
    private EditText etplazo;
    private ProgressBar progressBar;
    private List<mainConfiguracion> listaConfig;
    private AlertDialog dialog;

    private int plazo;
    private int enganche;
    private double tasa;
    private int opcion;

    private Retrofit retro;
    private Servicios service;
    private Call<apiConfiguraciones> requestConfig;


    private OnFragmentInteractionListener mListener;

    public fragment_configuracion() {
        // Required empty public constructor
    }

    public static fragment_configuracion newInstance(String param1, String param2) {
        fragment_configuracion fragment = new fragment_configuracion();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_fragment_configuracion, container, false);
        getActivity().setTitle(getResources().getString(R.string.app_name));
        serviceConfiguracion = new servicioConfiguracion();
        obtieneConfiguracion = new obtieneConfiguracion();
        iniciarViews();
        return vista;
    }

    private void iniciarViews() {
        btnCancelar = vista.findViewById(R.id.btnCancelar);
        btnGuardar = vista.findViewById(R.id.btnGuardar);
        etEnganche = vista.findViewById(R.id.etenganche);
        etTasa = vista.findViewById(R.id.etTasa);
        etplazo = vista.findViewById(R.id.etplazo);
        progressBar = vista.findViewById(R.id.progressConfiguracion);

        listaConfig = new ArrayList<>();
        btnGuardar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        if(Utilidades.tieneConfig) {
            progressBar.setVisibility(View.GONE);
            plazo = Utilidades.plazo;
            enganche = Utilidades.enganche;
            tasa = Utilidades.tasa;

            asignaValores();
        } else {
            obtieneConfiguracion = new obtieneConfiguracion();
            obtieneConfiguracion.execute();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGuardar:

                if (!etTasa.getText().toString().equals("")) {
                    tasa = Double.valueOf(etTasa.getText().toString());
                } else {
                    tasa = 0.00;
                }

                if(!etplazo.getText().toString().equals("")) {
                    plazo = Integer.valueOf(etplazo.getText().toString());
                } else {
                    plazo = 0;
                }

                if(!etEnganche.getText().toString().equals("")) {
                    enganche = Integer.valueOf(etEnganche.getText().toString());
                } else {
                    enganche = 0;
                }

                if(Utilidades.tieneConfig) {
                    opcion = 1;
                } else {
                    opcion = 0;
                }
                serviceConfiguracion = new servicioConfiguracion();
                serviceConfiguracion.execute();

                break;
            case R.id.btnCancelar:
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mview = getLayoutInflater().inflate(R.layout.dialog_cancelacion, null);

                Button btnNo = mview.findViewById(R.id.btnno);
                Button btnSi = mview.findViewById(R.id.btnsi);

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnSi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager manager = getFragmentManager();
                        fragment_principal fragment_principal = new fragment_principal();
                        manager.beginTransaction()
                                .replace(R.id.idu_contenedor, fragment_principal).commit();
                        dialog.dismiss();
                    }
                });

                mBuilder.setView(mview);
                dialog = mBuilder.create();
                dialog.show();
                break;
        }
    }

    private void asignaValores() {
        etplazo.setText(String.valueOf(plazo));
        etEnganche.setText(String.valueOf(enganche));
        etTasa.setText(String.valueOf(tasa));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
                            for(apiConfiguracion c : config.configuracion) {
                                listaConfig.add(new mainConfiguracion(c.conf_id,c.conf_tasafinanciamiento,c.conf_plazomaximo,c.conf_porcentajeenganche));
                            }

                            tasa = listaConfig.get(0).getTasaFinanciamiento();
                            plazo = listaConfig.get(0).getPlazoMaximo();
                            enganche =listaConfig.get(0).getPorcentajeEnganche();

                            Utilidades.tieneConfig = true;
                            Utilidades.plazo = plazo;
                            Utilidades.tasa = tasa;
                            Utilidades.enganche = enganche;

                            asignaValores();
                        } else {
                            Utilidades.tieneConfig = false;
                        }
                    }
                }

                @Override
                public void onFailure(Call<apiConfiguraciones> call, Throwable t) {
                    Toast.makeText(getActivity(), "Error 2:" + t.getMessage(), Toast.LENGTH_SHORT).show();
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
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
        }
    }


    class servicioConfiguracion extends AsyncTask<Void,Void,Void>
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
                            Toast.makeText(getActivity(), getResources().getString(R.string.mgConfiguracion), Toast.LENGTH_SHORT).show();
                            obtieneConfiguracion obtieneConfiguracion = new obtieneConfiguracion();
                            obtieneConfiguracion.execute();
                        }
                    }
                }

                @Override
                public void onFailure(Call<apiConfiguraciones> call, Throwable t) {
                    Toast.makeText(getActivity(), "Error 2:" + t.getMessage(), Toast.LENGTH_SHORT).show();
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
            requestConfig = service.serviceConfiguracion(tasa,plazo,enganche,opcion);//"1".toString());
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
        }
    }
}
