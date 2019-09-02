package com.app.lavendimia;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.lavendimia.Utilidades.Servicios;
import com.app.lavendimia.Utilidades.Utilidades;
import com.app.lavendimia.clientes.apiCliente;
import com.app.lavendimia.clientes.apiClientes;
import com.app.lavendimia.clientes.mainClientes;
import com.app.lavendimia.clientes.mainClientesAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class fragment_clientes extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View vista;

    private TextView btnNuevoCliente;
    private RecyclerView rvClientes;
    private Retrofit retro;
    private Servicios service;
    private Call<apiClientes> requestClientes;

    private List<mainClientes> listaClientes;

    private mainClientesAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public fragment_clientes() {
        // Required empty public constructor
    }


    public static fragment_clientes newInstance(String param1, String param2) {
        fragment_clientes fragment = new fragment_clientes();
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
        vista = inflater.inflate(R.layout.fragment_fragment_clientes, container, false);
        getActivity().setTitle(getResources().getString(R.string.app_name));
        initViews();
        return vista;
    }

    private void initViews() {
        listaClientes = new ArrayList<>();
        btnNuevoCliente = vista.findViewById(R.id.btnnuevoc);
        rvClientes = vista.findViewById(R.id.rvClientes);


        btnNuevoCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newid = 1;
                int tamañolista = listaClientes.size();
                if(tamañolista > 0) {
                    newid = listaClientes.get(tamañolista-1).getC_id() + 1;
                }
                Utilidades.puedeRegresar = false;
                FragmentManager manager = getFragmentManager();
                fragment_editadcliente fragment_nuevo = new fragment_editadcliente();
                Bundle bundle = new Bundle();
                bundle.putString("opcion","0");
                bundle.putString("idnew",String.valueOf(newid));
                fragment_nuevo.setArguments(bundle);
                manager.beginTransaction()
                        .replace(R.id.idu_contenedor, fragment_nuevo).commit();
            }
        });
        rvClientes.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvClientes.setLayoutManager(layoutManager);
        rvClientes.setAdapter(new mainClientesAdapter(getActivity(), listaClientes));

        ConnectivityManager con = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = con.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            obtenerClientes obtenerClientes = new obtenerClientes();
            obtenerClientes.execute();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.nosepudoconectar), Toast.LENGTH_SHORT).show();
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

    //ASSYNNCRONOS

    class obtenerClientes extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            requestClientes.enqueue(new Callback<apiClientes>() {
                @Override
                public void onResponse(Call<apiClientes> call, Response<apiClientes> response) {
                    if (!response.isSuccessful()) {
                        //    Toast.makeText(MainActivity.this, "" + response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        apiClientes clientes = response.body();
                        //Toast.makeText(getActivity(), ""+animes.Animes, Toast.LENGTH_SHORT).show();
                        if (clientes.estado== 1) {
                            for (apiCliente c : clientes.Clientes) {
                                listaClientes.add(new mainClientes(c.c_id, c.c_nombre, c.c_apaterno, c.c_amaterno, c.c_rfc));
                            }

                            adapter = new mainClientesAdapter(getActivity(), listaClientes);
                            //String strVistas = Integer.toString(vistas);
                            rvClientes.setClickable(true);
                            rvClientes.setAdapter(adapter);
                            rvClientes.setClickable(true);
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
            requestClientes = service.obtenerClientes();//"1".toString());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
