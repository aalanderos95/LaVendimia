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
import android.widget.TextView;
import android.widget.Toast;

import com.app.lavendimia.Utilidades.Servicios;
import com.app.lavendimia.Utilidades.Utilidades;
import com.app.lavendimia.clientes.apiClientes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class fragment_editadcliente extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View vista;
    private Button btnCancelar;
    private Button btnGuardar;
    private TextView txtclave;
    private EditText etNombre;
    private EditText etAPaterno;
    private EditText etAMaterno;
    private EditText etRfc;
    private AlertDialog dialog;

    private String numIDNEW;
    private String nombre;
    private String apaterno;
    private String amaterno;
    private String rfc;
    private String opcion;

    private Retrofit retro;
    private Servicios service;
    private Call<apiClientes> requestClientes;

    public fragment_editadcliente() {

    }

    public static fragment_editadcliente newInstance(String param1, String param2) {
        fragment_editadcliente fragment = new fragment_editadcliente();
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
                nombre = getArguments().getString("nombre");
                apaterno = getArguments().getString("apaterno");
                amaterno = getArguments().getString("amaterno");
                rfc = getArguments().getString("rfc");
                numIDNEW = getArguments().getString("id");
            } else {
                numIDNEW = getArguments().getString("idnew");
                nombre = "";
                apaterno = "";
                amaterno = "";
                rfc = "";
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_fragment_editadcliente, container, false);
        getActivity().setTitle(getResources().getString(R.string.registroClientes));
        initViews();
        return vista;
    }

    private void initViews() {
        etNombre = vista.findViewById(R.id.etNombre);
        etAPaterno = vista.findViewById(R.id.etapaterno);
        etAMaterno = vista.findViewById(R.id.etamaterno);
        etRfc = vista.findViewById(R.id.etrfc);
        btnCancelar = vista.findViewById(R.id.btnCancelarCliente);
        btnGuardar = vista.findViewById(R.id.btnGuardarCliente);
        txtclave = vista.findViewById(R.id.txtClave);

        txtclave.setText(numIDNEW);
        etNombre.setText(nombre);
        etAPaterno.setText(apaterno);
        etAMaterno.setText(amaterno);
        etRfc.setText(rfc);

        btnCancelar.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancelarCliente:
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
                        fragment_clientes fragment_principal = new fragment_clientes();
                        manager.beginTransaction()
                                .replace(R.id.idu_contenedor, fragment_principal).commit();
                        dialog.dismiss();
                    }
                });

                mBuilder.setView(mview);
                dialog = mBuilder.create();
                dialog.show();
                break;
            case R.id.btnGuardarCliente:
                nombre = etNombre.getText().toString();
                apaterno = etAPaterno.getText().toString();
                amaterno = etAMaterno.getText().toString();
                rfc = etRfc.getText().toString();

                boolean continua = true;
                if(nombre.equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.faltanombre), Toast.LENGTH_SHORT).show();
                    continua = false;
                }

                if(apaterno.equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.faltaapaterno), Toast.LENGTH_SHORT).show();
                    continua = false;
                }

                if (rfc.equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.faltarfc), Toast.LENGTH_SHORT).show();
                    continua = false;
                }

                if (continua == true){
                    serviceClientes serviceClientes = new serviceClientes();
                    serviceClientes.execute();
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


    //ASSYNCRONOS
    class serviceClientes extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            requestClientes.enqueue(new Callback<apiClientes>() {
                @Override
                public void onResponse(Call<apiClientes> call, Response<apiClientes> response) {
                    if (!response.isSuccessful()) {
                        //    Toast.makeText(MainActivity.this, "" + response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        apiClientes config = response.body();
                        //Toast.makeText(getActivity(), ""+animes.Animes, Toast.LENGTH_SHORT).show();
                        if (config.estado== 1) {
                            if(opcion.equals("0")) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.mgCliente), Toast.LENGTH_SHORT).show();
                                Utilidades.puedeRegresar = false;
                                FragmentManager manager = getFragmentManager();
                                fragment_clientes fragment_principal = new fragment_clientes();
                                manager.beginTransaction()
                                        .replace(R.id.idu_contenedor, fragment_principal).commit();
                            } else if(opcion.equals("1")) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.mgClienteupdate), Toast.LENGTH_SHORT).show();
                                Utilidades.puedeRegresar = false;
                                FragmentManager manager = getFragmentManager();
                                fragment_clientes fragment_principal = new fragment_clientes();
                                manager.beginTransaction()
                                        .replace(R.id.idu_contenedor, fragment_principal).commit();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<apiClientes> call, Throwable t) {
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
            requestClientes = service.serviceClientes(nombre,apaterno,amaterno,rfc,numIDNEW,opcion);//"1".toString());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
