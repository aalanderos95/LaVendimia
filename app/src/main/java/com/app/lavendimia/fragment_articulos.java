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
import com.app.lavendimia.articulo.apiArticulo;
import com.app.lavendimia.articulo.apiArticulos;
import com.app.lavendimia.articulo.mainArticulos;
import com.app.lavendimia.articulo.mainArticulosAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_articulos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_articulos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_articulos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View vista;

    private TextView btnNuevoArticulo;
    private RecyclerView rvArticulos;
    private Retrofit retro;
    private Servicios service;
    private Call<apiArticulos> requestArticulos;

    private List<mainArticulos> listaArticulos;

    private mainArticulosAdapter adapter;


    public fragment_articulos() {
        // Required empty public constructor
    }

    public static fragment_articulos newInstance(String param1, String param2) {
        fragment_articulos fragment = new fragment_articulos();
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
        vista = inflater.inflate(R.layout.fragment_fragment_articulos, container, false);
        getActivity().setTitle(getResources().getString(R.string.app_name));
        initViews();
        return vista;
    }

    private void initViews() {
        listaArticulos = new ArrayList<>();
        btnNuevoArticulo = vista.findViewById(R.id.btnnart);
        rvArticulos = vista.findViewById(R.id.rvArticulos);


        btnNuevoArticulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newid = 1;
                int tamañolista = listaArticulos.size();
                if(tamañolista > 0) {
                    newid = listaArticulos.get(tamañolista-1).getAr_id() + 1;
                }
                Utilidades.puedeRegresar = false;
                FragmentManager manager = getFragmentManager();
                fragment_editadarticulo fragment_nuevo = new fragment_editadarticulo();
                Bundle bundle = new Bundle();
                bundle.putString("opcion","0");
                bundle.putString("idnew",String.valueOf(newid));
                fragment_nuevo.setArguments(bundle);
                manager.beginTransaction()
                        .replace(R.id.idu_contenedor, fragment_nuevo).commit();
            }
        });
        rvArticulos.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvArticulos.setLayoutManager(layoutManager);
        rvArticulos.setAdapter(new mainArticulosAdapter(getActivity(), listaArticulos));

        ConnectivityManager con = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = con.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            obtenerArticulos obtenerArticulos = new obtenerArticulos();
            obtenerArticulos.execute();
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

    //ASSYNCRONOS
    class obtenerArticulos extends AsyncTask<Void,Void,Void>
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
                            for (apiArticulo c : articulos.Articulos) {
                                listaArticulos.add(new mainArticulos(c.ar_id,c.ar_descripcion,c.ar_modelo,c.ar_precio,c.ar_existencia));
                            }

                            adapter = new mainArticulosAdapter(getActivity(), listaArticulos);
                            //String strVistas = Integer.toString(vistas);
                            rvArticulos.setClickable(true);
                            rvArticulos.setAdapter(adapter);
                            rvArticulos.setClickable(true);
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
            requestArticulos = service.obtenerArticulos();//"1".toString());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
