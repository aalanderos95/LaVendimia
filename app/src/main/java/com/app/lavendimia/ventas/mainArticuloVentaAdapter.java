package com.app.lavendimia.ventas;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.lavendimia.R;
import com.app.lavendimia.Utilidades.Utilidades;
import com.app.lavendimia.articulo.mainArticulos;

import java.text.DecimalFormat;
import java.util.List;

import static com.app.lavendimia.Utilidades.Utilidades.listArtAgregados;

public class mainArticuloVentaAdapter
        extends RecyclerView.Adapter<mainArticuloVentaAdapter.VentasViewHolder>
        implements View.OnClickListener{
    private View.OnClickListener listener;

    private int cantidadActual;
    private Context mContext;
    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener!= null)
        {
            listener.onClick(v);
        }
    }



    public interface miInterface {
        void Resultado(String resultado, int id, int posicion);
    }

    public static class VentasViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item

        public TextView txtdesArt;
        public TextView txtmodart;
        public EditText etcantArt;
        public TextView txtprecioart;
        public TextView txtimpart;
        public ImageView btndelArt;
        public VentasViewHolder(View v) {
            super(v);
            txtdesArt = v.findViewById(R.id.txtdesArt);
            txtmodart = v.findViewById(R.id.txtmodart);
            etcantArt = v.findViewById(R.id.etcantArt);
            txtprecioart = v.findViewById(R.id.txtprecioart);
            txtimpart = v.findViewById(R.id.txtimpart);
            btndelArt = v.findViewById(R.id.btndelArt);
        }
    }
    miInterface listenerInterface;
    public mainArticuloVentaAdapter(Context contexto, miInterface listener)
    {
        this.listenerInterface = listener;
        this.mContext = contexto;
    }

    @Override
    public int getItemCount() {
        return listArtAgregados.size();
    }

    @Override
    public mainArticuloVentaAdapter.VentasViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_articulosagregados, viewGroup, false);
        v.setOnClickListener(this);
        return new mainArticuloVentaAdapter.VentasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final mainArticuloVentaAdapter.VentasViewHolder viewHolder, final int i) {
        //viewHolder.imagen.setImageResource(items.get(i).getImagen());
        final DecimalFormat format = new DecimalFormat("#.00");
        viewHolder.txtdesArt.setText(listArtAgregados.get(i).getDescripcion());
        viewHolder.txtmodart.setText(listArtAgregados.get(i).getModelo());
        viewHolder.txtprecioart.setText(String.valueOf(format.format(listArtAgregados.get(i).getPrecio())));
        viewHolder.etcantArt.setText(String.valueOf(listArtAgregados.get(i).getCantidad()));

        double importe = listArtAgregados.get(i).getPrecio() * listArtAgregados.get(i).getCantidad();

        listArtAgregados.set(i,new mainarticuloAgregado(
                listArtAgregados.get(i).getId(),
                listArtAgregados.get(i).getDescripcion(),
                listArtAgregados.get(i).getModelo(),
                listArtAgregados.get(i).getCantidad(),
                listArtAgregados.get(i).getPrecio(),
                importe,
                listArtAgregados.get(i).getExistencia()));

        viewHolder.txtimpart.setText(String.valueOf(format.format(listArtAgregados.get(i).getImporte())));
        viewHolder.etcantArt.requestFocus();

        cantidadActual = listArtAgregados.get(i).getCantidad();

        viewHolder.etcantArt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String dato = s.toString();
                Log.e("CAntidadActual:",""+cantidadActual);
                if(dato.length() > 0) {
                    int cant = Integer.valueOf(dato);
                    if (cant <= listArtAgregados.get(i).getExistencia()) {
                        if(cant == 0) {
                            String cambio = "3";
                            int id = listArtAgregados.get(i).getId();
                            double importe = listArtAgregados.get(i).getPrecio() * 0;
                            listArtAgregados.set(i,new mainarticuloAgregado(
                                    listArtAgregados.get(i).getId(),
                                    listArtAgregados.get(i).getDescripcion(),
                                    listArtAgregados.get(i).getModelo(),
                                    0,
                                    listArtAgregados.get(i).getPrecio(),
                                    importe,
                                    listArtAgregados.get(i).getExistencia()));
                            listenerInterface.Resultado(cambio,id,i);
                        } else {
                           /* if(cantidadActual < cant) {
                                //VA Restar
                                Utilidades.cantidadSumaResta = cant-cantidadActual;
                                String cambio = "5";
                                int id = listArtAgregados.get(i).getId();
                                listenerInterface.Resultado(cambio,id,i);
                            } else {
                                //VA sumar
                                String cambio = "6";
                                Utilidades.cantidadSumaResta = cantidadActual - cant;
                                int id = listArtAgregados.get(i).getId();
                                listenerInterface.Resultado(cambio,id,i);
                            }*/
                            double importe = listArtAgregados.get(i).getPrecio() * cant;
                            listArtAgregados.set(i,new mainarticuloAgregado(
                                    listArtAgregados.get(i).getId(),
                                    listArtAgregados.get(i).getDescripcion(),
                                    listArtAgregados.get(i).getModelo(),
                                    cant,
                                    listArtAgregados.get(i).getPrecio(),
                                    importe,
                                    listArtAgregados.get(i).getExistencia()));
                            viewHolder.txtimpart.setText(String.valueOf(format.format(listArtAgregados.get(i).getImporte())));
                            String cambio = "1";
                            int id = listArtAgregados.get(i).getId();
                            listenerInterface.Resultado(cambio, id,i);
                        }
                        cantidadActual = cant;
                    } else {
                        String cambio = "4";
                        int id = listArtAgregados.get(i).getId();
                        listenerInterface.Resultado(cambio, id, i);
                    }

                }
                viewHolder.etcantArt.requestFocus();
            }
        });

        viewHolder.btndelArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eliminar = "2";
                int position = i;
                listenerInterface.Resultado(eliminar, position,i);
            }
        });
    }
}

