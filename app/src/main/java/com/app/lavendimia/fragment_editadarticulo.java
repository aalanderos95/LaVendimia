package com.app.lavendimia;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.lavendimia.Utilidades.Servicios;
import com.app.lavendimia.Utilidades.Utilidades;
import com.app.lavendimia.articulo.apiArticulos;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class fragment_editadarticulo extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View vista;
    private Button btnCancelar;
    private Button btnGuardar;
    private TextView txtclave;
    private EditText etDescripcion;
    private EditText etModelo;
    private EditText etPrecio;
    private EditText etExistencia;
    private AlertDialog dialog;

    private String numIDNEW;
    private String descripcion;
    private String modelo;
    private String precio;
    private String existencia;
    private String opcion;

    private Double doublePrecio;
    private int intExistencia;

    private Retrofit retro;
    private Servicios service;
    private Call<apiArticulos> requestArticulos;



    private OnFragmentInteractionListener mListener;

    public fragment_editadarticulo() {
        // Required empty public constructor
    }

    public static fragment_editadarticulo newInstance(String param1, String param2) {
        fragment_editadarticulo fragment = new fragment_editadarticulo();
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
            opcion = getArguments().getString("opcion");
            if(opcion.equals("1")) {
                descripcion = getArguments().getString("descripcion");
                modelo = getArguments().getString("modelo");
                precio = getArguments().getString("precio");
                existencia = getArguments().getString("existencia");
                numIDNEW = getArguments().getString("id");
            } else {
                numIDNEW = getArguments().getString("idnew");
                descripcion = "";
                modelo = "";
                precio = "";
                existencia = "";
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_fragment_editadarticulo, container, false);
        getActivity().setTitle(getResources().getString(R.string.registroArticulos));
        initviews();
        return vista;
    }

    private void initviews() {
        etDescripcion = vista.findViewById(R.id.etDescripcion);
        etModelo = vista.findViewById(R.id.etmodelo);
        etPrecio = vista.findViewById(R.id.etprecio);
        etExistencia = vista.findViewById(R.id.etexistencia);
        btnCancelar = vista.findViewById(R.id.btnCancelarArticulo);
        btnGuardar = vista.findViewById(R.id.btnGuardarArticulo);
        txtclave = vista.findViewById(R.id.txtClaveArt);

        txtclave.setText(numIDNEW);
        etDescripcion.setText(descripcion);
        etModelo.setText(modelo);
        etPrecio.setText(precio);
        etExistencia.setText(existencia);

        btnCancelar.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancelarArticulo:
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
                        Utilidades.puedeRegresar = false;
                        FragmentManager manager = getFragmentManager();
                        fragment_articulos fragment_articulos = new fragment_articulos();
                        manager.beginTransaction()
                                .replace(R.id.idu_contenedor, fragment_articulos).commit();
                        dialog.dismiss();
                    }
                });

                mBuilder.setView(mview);
                dialog = mBuilder.create();
                dialog.show();
                break;
            case R.id.btnGuardarArticulo:
                descripcion = etDescripcion.getText().toString();
                modelo = etModelo.getText().toString();
                precio = etPrecio.getText().toString();
                existencia = etExistencia.getText().toString();

                Log.e("modelo",modelo);
                boolean continua = true;
                if(descripcion.equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.faltadescripcion), Toast.LENGTH_SHORT).show();
                    continua = false;
                }

                if(precio.equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.faltaprecio), Toast.LENGTH_SHORT).show();
                    continua = false;
                }

                if (existencia.equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.faltaexistencia), Toast.LENGTH_SHORT).show();
                    continua = false;
                }

                if(continua == true){
                    doublePrecio = Double.valueOf(etPrecio.getText().toString());
                    intExistencia = Integer.valueOf(etExistencia.getText().toString());
                    serviceArticulos serviceArticulos = new serviceArticulos();
                    serviceArticulos.execute();
                }

                break;
        }
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

    //Assyncronos

    class serviceArticulos extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            requestArticulos.enqueue(new Callback<apiArticulos>() {
                @Override
                public void onResponse(Call<apiArticulos> call, Response<apiArticulos> response) {
                    if (!response.isSuccessful()) {
                        //    Toast.makeText(MainActivity.this, "" + response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        apiArticulos articulos = response.body();
                        //Toast.makeText(getActivity(), ""+animes.Animes, Toast.LENGTH_SHORT).show();
                        if (articulos.estado== 1) {
                            if(opcion.equals("0")) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.mgArticulo), Toast.LENGTH_SHORT).show();
                                Utilidades.puedeRegresar = false;
                                FragmentManager manager = getFragmentManager();
                                fragment_articulos fragment_articulos = new fragment_articulos();
                                manager.beginTransaction()
                                        .replace(R.id.idu_contenedor, fragment_articulos).commit();
                            } else if(opcion.equals("1")) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.mgArticuloupdate), Toast.LENGTH_SHORT).show();
                                Utilidades.puedeRegresar = false;
                                FragmentManager manager = getFragmentManager();
                                fragment_articulos  fragment_articulos = new fragment_articulos();
                                manager.beginTransaction()
                                        .replace(R.id.idu_contenedor, fragment_articulos).commit();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<apiArticulos> call, Throwable t) {
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
            requestArticulos = service.serviceArticulos(descripcion,modelo,doublePrecio,intExistencia,numIDNEW,opcion);//"1".toString());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
