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
import com.app.lavendimia.articulo.mainArticulosAdapter;
import com.app.lavendimia.ventas.apiVenta;
import com.app.lavendimia.ventas.apiVentas;
import com.app.lavendimia.ventas.mainVentas;
import com.app.lavendimia.ventas.mainVentasAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class fragment_ventas extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View vista;

    private TextView btnNuevaVenta;
    private RecyclerView rvVentas;
    private Retrofit retro;
    private Servicios service;
    private Call<apiVentas> requestVentas;

    private List<mainVentas> listaVentas;
    private mainVentasAdapter adapter;


    private OnFragmentInteractionListener mListener;

    public fragment_ventas() {
        // Required empty public constructor
    }

    public static fragment_ventas newInstance(String param1, String param2) {
        fragment_ventas fragment = new fragment_ventas();
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
        vista = inflater.inflate(R.layout.fragment_ventas, container, false);
        getActivity().setTitle(getResources().getString(R.string.app_name));
        initViews();
        return vista;
    }

    private void initViews() {
        listaVentas = new ArrayList<>();
        btnNuevaVenta = vista.findViewById(R.id.btnnuevav);
        rvVentas = vista.findViewById(R.id.rvVentas);

        btnNuevaVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newid = 1;
                int tamañolista = listaVentas.size();
                if(tamañolista > 0) {
                    newid = listaVentas.get(tamañolista-1).getV_folio() + 1;
                }
                Utilidades.puedeRegresar = false;
                FragmentManager manager = getFragmentManager();
                fragment_nuevaVenta fragment_nuevo = new fragment_nuevaVenta();
                Bundle bundle = new Bundle();
                bundle.putString("newID",""+newid);
                fragment_nuevo.setArguments(bundle);
                manager.beginTransaction()
                        .replace(R.id.idu_contenedor, fragment_nuevo).commit();
            }
        });
        rvVentas.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvVentas.setLayoutManager(layoutManager);
        rvVentas.setAdapter(new mainVentasAdapter(getActivity(), listaVentas));

        ConnectivityManager con = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = con.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            obtenerVentas obtenerVentas = new obtenerVentas();
            obtenerVentas.execute();
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

    //ASSyNCRONOS
    class obtenerVentas extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            requestVentas.enqueue(new Callback<apiVentas>() {
                @Override
                public void onResponse(Call<apiVentas> call, Response<apiVentas> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getActivity(), "" + response.code(), Toast.LENGTH_SHORT).show();
                    } else {
                        apiVentas ventas = response.body();
                        //Toast.makeText(getActivity(), ""+animes.Animes, Toast.LENGTH_SHORT).show();
                        if (ventas.estado== 1) {
                            for (apiVenta c : ventas.Ventas) {
                                listaVentas.add(new mainVentas(c.v_folio,c.v_uid,c.c_nombre,c.c_apaterno,c.c_amaterno,c.v_fecha,c.v_plazo,c.v_total));
                            }

                            adapter = new mainVentasAdapter(getActivity(), listaVentas);
                            //String strVistas = Integer.toString(vistas);
                            rvVentas.setClickable(true);
                            rvVentas.setAdapter(adapter);
                            rvVentas.setClickable(true);
                        }
                    }
                }

                @Override
                public void onFailure(Call<apiVentas> call, Throwable t) {
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
            requestVentas = service.obtenerVentas();//"1".toString());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


}
