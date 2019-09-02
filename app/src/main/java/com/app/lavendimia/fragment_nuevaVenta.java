package com.app.lavendimia;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.lavendimia.Utilidades.Servicios;
import com.app.lavendimia.Utilidades.Utilidades;
import com.app.lavendimia.articulo.apiArticulo;
import com.app.lavendimia.articulo.apiArticulos;
import com.app.lavendimia.articulo.mainArticulos;
import com.app.lavendimia.clientes.apiCliente;
import com.app.lavendimia.clientes.apiClientes;
import com.app.lavendimia.clientes.mainClientes;
import com.app.lavendimia.ventas.apiVentas;
import com.app.lavendimia.ventas.mainArticuloVentaAdapter;
import com.app.lavendimia.ventas.mainBuscarArticulosAdapter;
import com.app.lavendimia.ventas.mainBuscarClientesAdapter;
import com.app.lavendimia.ventas.mainarticuloAgregado;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.app.lavendimia.Utilidades.Utilidades.existenciaActual;
import static com.app.lavendimia.Utilidades.Utilidades.listArtAgregados;
import static com.app.lavendimia.Utilidades.Utilidades.listaExistenciaEmpezo;
import static com.app.lavendimia.Utilidades.Utilidades.obtenerPrecio;


public class fragment_nuevaVenta extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    //RETRO
    private Retrofit retro;
    private Servicios service;
    private Call<apiClientes> requestClientes;
    private Call<apiArticulos> requestArticulos;
    private Call<apiVentas> requestVentas;



    //VARIABLES
    private boolean buscaArt,buscarClien,clicArt,clicClien;
    private int plazo;
    private double enganche, bonificacion, total;
    private double preciocontado, totalPaga3, totalPaga6, totalPaga9, totalPaga12, importeAbono3,
            importeAbono6, importeAbono9, importeAbono12, importeAhorro3, importeAhorro6, importeAhorro9,
            importeAhorro12;
    private boolean etapa2 = false;
    private String nuevoID;
    private int idClienteSelect;
    private List<mainArticulos> listaArticulos;
    private List<mainArticulos> listaArticulosRespaldo;
    private List<mainClientes> listaClientes;
    private List<mainClientes> listaClientesRespaldo;

    private List<mainClientes> newlistCliente;

    //Adaptadores
    private mainBuscarClientesAdapter adapterBuscarClientes;
    private mainBuscarArticulosAdapter adapterBuscarArticulos;
    public mainArticuloVentaAdapter adapterarticuloVenta;

    //VISTAMADRE
    private View vista;
    //VIEWS
    private AlertDialog dialog;
    private TextView txtFolVta;
    private EditText etBuscarCliente;
    private TextView txtRfc;
    private RecyclerView rvBuscarCliente;
    private EditText etBuscarArticulo;
    private ImageView btnAggArticulo;
    private RecyclerView rvArticulosAgregados;
    private RecyclerView rvBuscarArticulo;
    private LinearLayout llengbontot;
    private TextView txtEnganche;
    private TextView txtboniEnganche;
    private TextView txttotal;
    private LinearLayout llabonosmensuales;
    private LinearLayout llbotones;

    private TextView txt3ab;
    private TextView txt3totalapa;
    private TextView txt3seah;
    private RadioButton rb3ab;

    private TextView txt6ab;
    private TextView txt6totalapa;
    private TextView txt6seah;
    private RadioButton rb6ab;

    private TextView txt9ab;
    private TextView txt9totalapa;
    private TextView txt9seah;
    private RadioButton rb9ab;

    private TextView txt12ab;
    private TextView txt12totalapa;
    private TextView txt12seah;
    private RadioButton rb12ab;

    private Button btnCancelarnv;
    private Button btnGuardarnv;


    private mainArticuloVentaAdapter.miInterface listenerLEctura;




    private OnFragmentInteractionListener mListener;

    public fragment_nuevaVenta() {
        // Required empty public constructor
    }

    public static fragment_nuevaVenta newInstance(String param1, String param2) {
        fragment_nuevaVenta fragment = new fragment_nuevaVenta();
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
            nuevoID = getArguments().getString("newID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_fragment_nueva_venta, container, false);
        getActivity().setTitle(getResources().getString(R.string.registroVentas));
        initViews();
        proceso();
        return vista;
    }


    private void proceso() {
        obtieneArticulos();
        obtieneClientes();
        txtFolVta.setText(nuevoID);
        rvBuscarCliente.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvBuscarCliente.setLayoutManager(layoutManager);
        final List<mainClientes> clientes = new ArrayList<>();
        rvBuscarCliente.setAdapter(new mainBuscarClientesAdapter(getActivity(), clientes));

        rvBuscarArticulo.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        rvBuscarArticulo.setLayoutManager(layoutManager1);
        List<mainArticulos> articulos = new ArrayList<>();
        rvBuscarArticulo.setAdapter(new mainBuscarArticulosAdapter(getActivity(), articulos));

        rvArticulosAgregados.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        rvArticulosAgregados.setLayoutManager(layoutManager2);
        listenerLEctura = new mainArticuloVentaAdapter.miInterface() {
            @Override
            public void Resultado(String resultado,int id, int posicion) {
                if(resultado.equals("1")) {
                    Log.e("INTERFACE","RESULTADO INTERFACE: Cambio cantidad");
                    Log.e("INTERFACE","RESULTADO ID: "+id);
                    actualizaDatos();
                    llenaabonosmensuales();
                } else if(resultado.equals("2")) {
                    Log.e("INTERFACE","RESULTADO INTERFACE: Eliminara");
                    Log.e("INTERFACE","RESULTADO position: "+id);
                    listArtAgregados.remove(id);
                    int existencia = listaExistenciaEmpezo.get(posicion);
                    Log.e("INTERFACE","Quiere Actualizar: "+existencia);
                    if(listArtAgregados.size() == 0) {
                        listaVacia();
                    } else {
                        actualizaDatos();
                    }
                } else if(resultado.equals("3")) {
                    Log.e("INTERFACE","RESULTADO INTERFACE: escondeddatos");
                    Log.e("INTERFACE","RESULTADO position: "+id);
                    llabonosmensuales.setVisibility(View.GONE);
                    actualizaDatos();
                    etapa2 = false;
                    btnGuardarnv.setText(getResources().getString(R.string.btnsiguiente));
                } else if(resultado.equals("4")) {
                    llabonosmensuales.setVisibility(View.GONE);
                    btnGuardarnv.setText(getResources().getString(R.string.btnsiguiente));
                    etapa2 = false;
                    Toast.makeText(getActivity(), getResources().getString(R.string.elartnotieneexistencia), Toast.LENGTH_SHORT).show();
                } else if(resultado.equals("5")) {
                    Log.e("INTERFACE","OPCION 5");
                    Log.e("INTERFACE","Quiere Actualizar: "+Utilidades.cantidadSumaResta);
                    actualizaExistencia(id,Utilidades.cantidadSumaResta,0);
                } else if(resultado.equals("6")) {
                    Log.e("INTERFACE","OPCION 6");
                    Log.e("INTERFACE","Quiere Actualizar: "+Utilidades.cantidadSumaResta);
                    actualizaExistencia(id,Utilidades.cantidadSumaResta,1);
                }

            }
        };
        rvArticulosAgregados.setAdapter(new mainArticuloVentaAdapter(getActivity(),listenerLEctura));

        etBuscarCliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String dato = s.toString();
                if(dato.length() >= 3 ){
                    if(listaClientes.size() != 0) {
                        for(mainClientes c : listaClientes) {
                            String nombreCompleto = c.getC_nombre() +
                                    " " + c.getC_apaterno() +
                                    " " + c.getC_amaterno();
                            if(nombreCompleto.toLowerCase().contains(dato.toLowerCase())) {
                                adapterBuscarClientes = new mainBuscarClientesAdapter(getActivity(), listaClientes);

                                rvBuscarCliente.setClickable(true);
                                adapterBuscarClientes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String nombreMostrara = listaClientes.get(rvBuscarCliente.getChildAdapterPosition(v)).getC_id() +
                                                " - " + listaClientes.get(rvBuscarCliente.getChildAdapterPosition(v)).getC_nombre() +
                                                " " + listaClientes.get(rvBuscarCliente.getChildAdapterPosition(v)).getC_apaterno() +
                                                " " + listaClientes.get(rvBuscarCliente.getChildAdapterPosition(v)).getC_amaterno();
                                        etBuscarCliente.setText(nombreMostrara);

                                        idClienteSelect = listaClientes.get(rvBuscarCliente.getChildAdapterPosition(v)).getC_id();
                                        txtRfc.setText("RFC: " +listaClientes.get(rvBuscarCliente.getChildAdapterPosition(v)).getC_rfc());
                                        listaClientes = new ArrayList<>();
                                        rvBuscarCliente.setVisibility(View.GONE);
                                        txtRfc.setVisibility(View.VISIBLE);
                                            }
                                });
                                //String strVistas = Integer.toString(vistas);
                                rvBuscarCliente.setAdapter(adapterBuscarClientes);
                                rvBuscarCliente.setClickable(true);
                                adapterBuscarClientes.notifyDataSetChanged();
                            } else {
                                obtieneNombre(dato);
                            }
                        }

                    } else {
                        obtieneNombre(dato);
                    }
                }
                else {
                    listaClientes = new ArrayList<>();
                    rvBuscarCliente.setVisibility(View.GONE);
                    idClienteSelect = 0;
                    txtRfc.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etBuscarArticulo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String dato = s.toString();
                if(dato.length() >= 3) {


                    obtieneArticulo(dato);

                }
                else {
                    listaArticulos = new ArrayList<>();
                    rvBuscarArticulo.setVisibility(View.GONE);
                    listaArticulosRespaldo = new ArrayList<>();
                }
            }
        });

        btnAggArticulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listaArticulosRespaldo.size() != 0) {
                    int id = listaArticulosRespaldo.get(0).getAr_id();
                    String des = listaArticulosRespaldo.get(0).getAr_descripcion();
                    String mod = listaArticulosRespaldo.get(0).getAr_Modelo();
                    int existencia = listaArticulosRespaldo.get(0).getAr_existencia();
                    double precio = obtenerPrecio(listaArticulosRespaldo.get(0).getAr_precio());
                    double importe = precio * 1;
                    Log.e("Precio" , ""+precio);
                    if (existencia > 1) {
                        rvArticulosAgregados.setVisibility(View.VISIBLE);
                        boolean agrega = true;
                        for(mainarticuloAgregado c : listArtAgregados){
                            if(id == c.getId()) {
                                agrega = false;
                            }
                        }
                        if(agrega) {
                            listArtAgregados.add(new mainarticuloAgregado(id, des, mod, 1, precio, importe, existencia));
                            actualizaDatos();
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.elarticuloyaestaagregado), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.notieneexistencia), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnGuardarnv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etapa2) {
                    boolean continuar = true;
                    if(idClienteSelect == 0) {
                        Log.e("LOGBTNGDR:" , "idCliente = 0");
                        Toast.makeText(getActivity(), getResources().getString(R.string.losdatosingresadosnosoncorrectos), Toast.LENGTH_SHORT).show();
                        continuar = false;
                    }
                    if(listArtAgregados.size() == 0) {
                        Log.e("LOGBTNGDR:" , "lista = 0");
                        Toast.makeText(getActivity(), getResources().getString(R.string.losdatosingresadosnosoncorrectos), Toast.LENGTH_SHORT).show();
                        continuar = false;
                    } else {
                        for(mainarticuloAgregado art : listArtAgregados) {
                            if (art.getCantidad() == 0 || art.getCantidad() > art.getExistencia()) {
                                Log.e("LOGBTNGDR:" , "cantidad = 0");
                                Toast.makeText(getActivity(), getResources().getString(R.string.losdatosingresadosnosoncorrectos), Toast.LENGTH_SHORT).show();
                                continuar = false;
                            };
                        }
                    }

                    if(continuar) {
                        llenaabonosmensuales();

                        btnGuardarnv.setText(getResources().getString(R.string.btnGuardar));
                        llabonosmensuales.setVisibility(View.VISIBLE);
                        etapa2 = true;
                    }

                } else {
                    if(plazo==0) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.debeselecunplazo), Toast.LENGTH_SHORT).show();
                    } else {
                        insertaVenta();

                        for(int x = 0; x < listArtAgregados.size(); x++) {

                            actualizaExistencia(listArtAgregados.get(x).getId(),
                                    listArtAgregados.get(x).getCantidad(),0);
                            if(x==listArtAgregados.size()-1) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        FragmentManager manager = getFragmentManager();
                                        fragment_ventas fragment_principal = new fragment_ventas();
                                        manager.beginTransaction()
                                                .replace(R.id.idu_contenedor, fragment_principal).commit();
                                    }
                                },2500);

                            }
                        }
                    }
                }
            }
        });

        btnCancelarnv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        fragment_ventas fragment_principal = new fragment_ventas();
                        manager.beginTransaction()
                                .replace(R.id.idu_contenedor, fragment_principal).commit();
                        dialog.dismiss();
                    }
                });

                mBuilder.setView(mview);
                dialog = mBuilder.create();
                dialog.show();
            }
        });

        rb3ab.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    rb6ab.setChecked(false);
                    rb9ab.setChecked(false);
                    rb12ab.setChecked(false);
                    plazo = 3;
                }
            }
        });

        rb6ab.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    rb3ab.setChecked(false);
                    rb9ab.setChecked(false);
                    rb12ab.setChecked(false);
                    plazo = 6;
                }
            }
        });

        rb9ab.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    rb3ab.setChecked(false);
                    rb6ab.setChecked(false);
                    rb12ab.setChecked(false);
                    plazo = 9;
                }
            }
        });

        rb12ab.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    rb3ab.setChecked(false);
                    rb6ab.setChecked(false);
                    rb9ab.setChecked(false);
                    plazo = 12;
                }
            }
        });




    }

    private void cambiaValoresCliente() {
        clicClien = true;
        buscarClien = false;
    }

    private void llenaabonosmensuales() {
        DecimalFormat format = new DecimalFormat("#.00");
        preciocontado = Utilidades.obtenerPrecioContado(total);
        totalPaga3 = Utilidades.obtenerTotalPagar(preciocontado, 3);
        totalPaga6 = Utilidades.obtenerTotalPagar(preciocontado, 6);
        totalPaga9 = Utilidades.obtenerTotalPagar(preciocontado, 9);
        totalPaga12 = Utilidades.obtenerTotalPagar(preciocontado, 12);

        txt3totalapa.setText(getResources().getString(R.string.txttotalapa) + " $"+String.valueOf(format.format(totalPaga3)));
        txt6totalapa.setText(getResources().getString(R.string.txttotalapa) + " $"+String.valueOf(format.format(totalPaga6)));
        txt9totalapa.setText(getResources().getString(R.string.txttotalapa) + " $"+String.valueOf(format.format(totalPaga9)));
        txt12totalapa.setText(getResources().getString(R.string.txttotalapa) + " $"+String.valueOf(format.format(totalPaga12)));

        importeAbono3 = Utilidades.obtenerImporteAbono(totalPaga3,3);
        importeAbono6 = Utilidades.obtenerImporteAbono(totalPaga6,6);
        importeAbono9 = Utilidades.obtenerImporteAbono(totalPaga9,9);
        importeAbono12 = Utilidades.obtenerImporteAbono(totalPaga12,12);

        txt3ab.setText("$"+ String.valueOf(format.format(importeAbono3)));
        txt6ab.setText("$"+ String.valueOf(format.format(importeAbono6)));
        txt9ab.setText("$"+ String.valueOf(format.format(importeAbono9)));
        txt12ab.setText("$"+ String.valueOf(format.format(importeAbono12)));

        importeAhorro3 = Utilidades.obtenerImporteAhorro(total,totalPaga3);
        importeAhorro6 = Utilidades.obtenerImporteAhorro(total,totalPaga6);
        importeAhorro9 = Utilidades.obtenerImporteAhorro(total,totalPaga9);
        importeAhorro12 = Utilidades.obtenerImporteAhorro(total,totalPaga12);

        txt3seah.setText(getResources().getString(R.string.txt3seah) +" $"+String.valueOf(format.format(importeAhorro3)));
        txt6seah.setText(getResources().getString(R.string.txt3seah) +" $"+String.valueOf(format.format(importeAhorro6)));
        txt9seah.setText(getResources().getString(R.string.txt3seah) +" $"+String.valueOf(format.format(importeAhorro9)));
        txt12seah.setText(getResources().getString(R.string.txt3seah) +" $"+String.valueOf(format.format(importeAhorro12)));

    }

    public void listaVacia() {
        etapa2 = false;
        btnGuardarnv.setText(getResources().getString(R.string.btnsiguiente));
        rvArticulosAgregados.setVisibility(View.GONE);
        txtEnganche.setText("0.00");
        txtboniEnganche.setText("0.00");
        txttotal.setText("0.00");
        llbotones.setVisibility(View.GONE);
        llabonosmensuales.setVisibility(View.GONE);
    };


    public void actualizaDatos() {
        adapterarticuloVenta = new mainArticuloVentaAdapter(getActivity(),listenerLEctura);
        rvArticulosAgregados.setClickable(true);
        rvArticulosAgregados.setAdapter(adapterarticuloVenta);
        rvArticulosAgregados.setClickable(true);
        adapterarticuloVenta.notifyDataSetChanged();

        double importes = 0.00;
        for(mainarticuloAgregado ar : listArtAgregados) {
            importes = importes + ar.getImporte();
        }

        enganche = Utilidades.obtenerEnganche(importes);
        bonificacion = Utilidades.obtenerBonificacionEnganche(enganche);
        total = importes - enganche - bonificacion;

        DecimalFormat format = new DecimalFormat("#.00");
        txtEnganche.setText(String.valueOf(format.format(enganche)));
        txtboniEnganche.setText(String.valueOf(format.format(bonificacion)));
        txttotal.setText(String.valueOf(format.format(total)));

        llengbontot.setVisibility(View.VISIBLE);
        llbotones.setVisibility(View.VISIBLE);
    }


    private void initViews() {
        plazo = 0;
        listaArticulos = new ArrayList<>();
        listArtAgregados = new ArrayList<>();
        listaClientes = new ArrayList<>();
        listaArticulosRespaldo = new ArrayList<>();
        txtFolVta = vista.findViewById(R.id.txtFolVta);
        etBuscarCliente = vista.findViewById(R.id.etBuscarCliente);
        txtRfc = vista.findViewById(R.id.txtRfc);
        rvBuscarCliente = vista.findViewById(R.id.rvBuscarCliente);
        etBuscarArticulo = vista.findViewById(R.id.etBuscarArticulo);
        btnAggArticulo = vista.findViewById(R.id.btnAggArticulo);
        rvArticulosAgregados = vista.findViewById(R.id.rvArticulosAgregados);
        rvBuscarArticulo = vista.findViewById(R.id.rvBuscarArticulo);
        llengbontot = vista.findViewById(R.id.llengbontot);
        txtEnganche = vista.findViewById(R.id.txtEnganche);
        txtboniEnganche = vista.findViewById(R.id.txtboniEnganche);
        txttotal = vista.findViewById(R.id.txttotal);
        llabonosmensuales = vista.findViewById(R.id.llabonosmensuales);
        llbotones = vista.findViewById(R.id.llbotones);

        txt3ab = vista.findViewById(R.id.txt3ab);
        txt3totalapa = vista.findViewById(R.id.txt3totalapa);
        txt3seah = vista.findViewById(R.id.txt3seah);
        rb3ab = vista.findViewById(R.id.rb3ab);

        txt6ab = vista.findViewById(R.id.txt6ab);
        txt6totalapa = vista.findViewById(R.id.txt6totalapa);
        txt6seah = vista.findViewById(R.id.txt6seah);
        rb6ab = vista.findViewById(R.id.rb6ab);

        txt9ab = vista.findViewById(R.id.txt9ab);
        txt9totalapa = vista.findViewById(R.id.txt9totalapa);
        txt9seah = vista.findViewById(R.id.txt9seah);
        rb9ab = vista.findViewById(R.id.rb9ab);

        txt12ab = vista.findViewById(R.id.txt12ab);
        txt12totalapa = vista.findViewById(R.id.txt12totalapa);
        txt12seah = vista.findViewById(R.id.txt12seah);
        rb12ab = vista.findViewById(R.id.rb12ab);

        btnCancelarnv = vista.findViewById(R.id.btnCancelarnv);
        btnGuardarnv = vista.findViewById(R.id.btnGuardarnv);

        llabonosmensuales.setVisibility(View.GONE);
        llengbontot.setVisibility(View.GONE);
        llbotones.setVisibility(View.GONE);
        txtRfc.setVisibility(View.GONE);
        rvArticulosAgregados.setVisibility(View.VISIBLE);
        rvBuscarArticulo.setVisibility(View.VISIBLE);
        rvBuscarCliente.setVisibility(View.VISIBLE);

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

    private void inicializaretro() {
        if(retro == null) {
            retro = new Retrofit.Builder()
                    .baseUrl(service.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

    private void obtieneArticulos() {
        rvBuscarArticulo.setVisibility(View.VISIBLE);
        inicializaretro();

        service = retro.create(Servicios.class);
        requestArticulos = service.obtenerArticulos();//service.obListAnime(catid);//"1".toString());


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
                                listaArticulos.add(new mainArticulos(c.ar_id, c.ar_descripcion, c.ar_modelo, c.ar_precio, c.ar_existencia));
                        }
                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<apiArticulos> call, Throwable t) {
                Toast.makeText(getActivity(), "Error 2:" + t.getMessage(), Toast.LENGTH_SHORT).show();
                //recycler.setVisibility(View.VISIBLE);
            }
        });

    }

    private void obtieneClientes() {
        rvBuscarCliente.setVisibility(View.VISIBLE);
        inicializaretro();
        service = retro.create(Servicios.class);
        requestClientes = service.obtenerClientes();//service.obListAnime(catid);//"1".toString());


        requestClientes.enqueue(new Callback<apiClientes>() {
            @Override
            public void onResponse(Call<apiClientes> call, Response<apiClientes> response) {
                if (!response.isSuccessful()) {
                    //    Toast.makeText(MainActivity.this, "" + response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    apiClientes clientes = response.body();
                    //Toast.makeText(getActivity(), ""+animes.Animes, Toast.LENGTH_SHORT).show();
                    if (clientes.estado == 1) {
                        for (apiCliente c : clientes.Clientes) {
                            listaClientes.add(new mainClientes(c.c_id, c.c_nombre, c.c_apaterno, c.c_amaterno, c.c_rfc));
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
    }

    private void obtieneArticulo(final String articulo) {
        rvBuscarArticulo.setVisibility(View.VISIBLE);
        inicializaretro();

        service = retro.create(Servicios.class);
        requestArticulos = service.obtenerArticulosconDato(articulo);//service.obListAnime(catid);//"1".toString());


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
                            if(listaArticulos.size() != 0){
                                boolean agrega = true;
                                for (mainArticulos c1 : listaArticulos)
                                {
                                    if(c1.getAr_id() == c.ar_id) {
                                        agrega = false;
                                    }
                                }
                                if (agrega) {
                                    listaArticulos.add(new mainArticulos(c.ar_id, c.ar_descripcion, c.ar_modelo, c.ar_precio, c.ar_existencia));
                                }
                            } else {
                                listaArticulos.add(new mainArticulos(c.ar_id, c.ar_descripcion, c.ar_modelo, c.ar_precio, c.ar_existencia));
                            }
                        }

                        adapterBuscarArticulos = new mainBuscarArticulosAdapter(getActivity(),listaArticulos);
                        rvBuscarArticulo.setClickable(true);
                        adapterBuscarArticulos .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listaArticulosRespaldo = new ArrayList<>();
                                etBuscarArticulo.setText(listaArticulos.get(rvBuscarArticulo.getChildAdapterPosition(v)).getAr_descripcion());
                                listaArticulosRespaldo.add(new mainArticulos(
                                        listaArticulos.get(rvBuscarArticulo.getChildAdapterPosition(v)).getAr_id(),
                                        listaArticulos.get(rvBuscarArticulo.getChildAdapterPosition(v)).getAr_descripcion(),
                                        listaArticulos.get(rvBuscarArticulo.getChildAdapterPosition(v)).getAr_Modelo(),
                                        listaArticulos.get(rvBuscarArticulo.getChildAdapterPosition(v)).getAr_precio(),
                                        listaArticulos.get(rvBuscarArticulo.getChildAdapterPosition(v)).getAr_existencia()));
                                listaExistenciaEmpezo.add(listaArticulos.get(rvBuscarArticulo.getChildAdapterPosition(v)).getAr_existencia());
                                listaArticulos = new ArrayList<>();
                                rvBuscarArticulo.setVisibility(View.GONE);
                            }
                        });
                        //String strVistas = Integer.toString(vistas);
                        rvBuscarArticulo.setClickable(true);
                        rvBuscarArticulo.setAdapter(adapterBuscarArticulos);
                        rvBuscarArticulo.setClickable(true);
                        adapterBuscarArticulos .notifyDataSetChanged();
                    } else {
                        adapterBuscarArticulos = new mainBuscarArticulosAdapter(getActivity(),listaArticulos);
                        rvBuscarArticulo.setClickable(true);
                        adapterBuscarArticulos .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                etBuscarArticulo.setText(listaArticulos.get(rvBuscarArticulo.getChildAdapterPosition(v)).getAr_descripcion());
                                listaArticulosRespaldo.add(new mainArticulos(
                                        listaArticulos.get(rvBuscarArticulo.getChildAdapterPosition(v)).getAr_id(),
                                        listaArticulos.get(rvBuscarArticulo.getChildAdapterPosition(v)).getAr_descripcion(),
                                        listaArticulos.get(rvBuscarArticulo.getChildAdapterPosition(v)).getAr_Modelo(),
                                        listaArticulos.get(rvBuscarArticulo.getChildAdapterPosition(v)).getAr_precio(),
                                        listaArticulos.get(rvBuscarArticulo.getChildAdapterPosition(v)).getAr_existencia()));
                                rvBuscarArticulo.setVisibility(View.GONE);
                            }
                        });
                        //String strVistas = Integer.toString(vistas);

                        rvBuscarArticulo.setAdapter(adapterBuscarArticulos);
                        rvBuscarArticulo.setClickable(true);
                        adapterBuscarArticulos.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<apiArticulos> call, Throwable t) {
                Toast.makeText(getActivity(), "Error 2:" + t.getMessage(), Toast.LENGTH_SHORT).show();
                //recycler.setVisibility(View.VISIBLE);
            }
        });

    }

    private void obtieneNombre(String nombre) {
        rvBuscarCliente.setVisibility(View.VISIBLE);
        inicializaretro();
        service = retro.create(Servicios.class);
        requestClientes = service.obtenerClientesconDato(nombre);//service.obListAnime(catid);//"1".toString());


        requestClientes.enqueue(new Callback<apiClientes>() {
            @Override
            public void onResponse(Call<apiClientes> call, Response<apiClientes> response) {
                if (!response.isSuccessful()) {
                    //    Toast.makeText(MainActivity.this, "" + response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    apiClientes clientes = response.body();
                    //Toast.makeText(getActivity(), ""+animes.Animes, Toast.LENGTH_SHORT).show();
                    if (clientes.estado == 1) {
                        for (apiCliente c : clientes.Clientes) {
                            if(listaClientes.size() != 0) {
                                boolean agrega = true;
                                for (mainClientes c1 : listaClientes)
                                {
                                    if(c1.getC_id() == c.c_id) {
                                        agrega = false;
                                    }
                                }
                                if (agrega) {
                                    listaClientes.add(new mainClientes(c.c_id, c.c_nombre, c.c_apaterno, c.c_amaterno, c.c_rfc));
                                }
                            } else {
                                listaClientes.add(new mainClientes(c.c_id, c.c_nombre, c.c_apaterno, c.c_amaterno, c.c_rfc));
                            }
                        }

                        adapterBuscarClientes = new mainBuscarClientesAdapter(getActivity(), listaClientes);

                        rvBuscarCliente.setClickable(true);
                        adapterBuscarClientes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String nombreMostrara = listaClientes.get(rvBuscarCliente.getChildAdapterPosition(v)).getC_id() +
                                        " - " + listaClientes.get(rvBuscarCliente.getChildAdapterPosition(v)).getC_nombre() +
                                        " " + listaClientes.get(rvBuscarCliente.getChildAdapterPosition(v)).getC_apaterno() +
                                        " " + listaClientes.get(rvBuscarCliente.getChildAdapterPosition(v)).getC_amaterno();
                                etBuscarCliente.setText(nombreMostrara);

                                idClienteSelect = listaClientes.get(rvBuscarCliente.getChildAdapterPosition(v)).getC_id();
                                txtRfc.setText("RFC: " +listaClientes.get(rvBuscarCliente.getChildAdapterPosition(v)).getC_rfc());
                                listaClientes = new ArrayList<>();
                                rvBuscarCliente.setVisibility(View.GONE);
                                txtRfc.setVisibility(View.VISIBLE);
                            }
                        });
                        //String strVistas = Integer.toString(vistas);
                        rvBuscarCliente.setAdapter(adapterBuscarClientes);
                        rvBuscarCliente.setClickable(true);
                        adapterBuscarClientes.notifyDataSetChanged();
                    } else {
                        adapterBuscarClientes = new mainBuscarClientesAdapter(getActivity(), listaClientes);

                        adapterBuscarClientes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String nombreMostrara = listaClientes.get(rvBuscarCliente.getChildAdapterPosition(v)).getC_id() +
                                        " - " + listaClientes.get(rvBuscarCliente.getChildAdapterPosition(v)).getC_nombre() +
                                        " " + listaClientes.get(rvBuscarCliente.getChildAdapterPosition(v)).getC_apaterno() +
                                        " " + listaClientes.get(rvBuscarCliente.getChildAdapterPosition(v)).getC_amaterno();
                                etBuscarCliente.setText(nombreMostrara);
                                idClienteSelect = listaClientes.get(rvBuscarCliente.getChildAdapterPosition(v)).getC_id();
                                txtRfc.setText("RFC: " +listaClientes.get(rvBuscarCliente.getChildAdapterPosition(v)).getC_rfc());
                                rvBuscarCliente.setVisibility(View.GONE);
                            }
                        });
                        //String strVistas = Integer.toString(vistas);
                        rvBuscarCliente.setClickable(true);
                        rvBuscarCliente.setAdapter(adapterBuscarClientes);
                        rvBuscarCliente.setClickable(true);
                        //adapterBuscar.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<apiClientes> call, Throwable t) {
                Toast.makeText(getActivity(), "Error 2:" + t.getMessage(), Toast.LENGTH_SHORT).show();
                //recycler.setVisibility(View.VISIBLE);
            }
        });
    }

    private void actualizaExistencia(final int id, final int cantidad, final int opcion) {
        inicializaretro();

        service = retro.create(Servicios.class);
        requestArticulos = service.actualizaExistencia(id,cantidad,opcion);//service.obListAnime(catid);//"1".toString());


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
                            existenciaActual = c.ar_existencia;
                        }

                    } else if(articulos.estado == 3) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.notieneexistencia), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<apiArticulos> call, Throwable t) {
                Toast.makeText(getActivity(), "Error 2:" + t.getMessage(), Toast.LENGTH_SHORT).show();
                //recycler.setVisibility(View.VISIBLE);
            }
        });

    }


    private void insertaVenta() {
        inicializaretro();

        service = retro.create(Servicios.class);
        requestVentas = service.insertarVentas(idClienteSelect,plazo,enganche,bonificacion,total);//service.obListAnime(catid);//"1".toString());


        requestVentas.enqueue(new Callback<apiVentas>() {
            @Override
            public void onResponse(Call<apiVentas> call, Response<apiVentas> response) {
                if (!response.isSuccessful()) {
                    //    Toast.makeText(MainActivity.this, "" + response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    apiVentas ventas = response.body();
                    //Toast.makeText(getActivity(), ""+animes.Animes, Toast.LENGTH_SHORT).show();
                    if (ventas.estado== 1) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.ventaregistrada), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<apiVentas> call, Throwable t) {
                Toast.makeText(getActivity(), "Error 2:" + t.getMessage(), Toast.LENGTH_SHORT).show();
                //recycler.setVisibility(View.VISIBLE);
            }
        });

    }

}
