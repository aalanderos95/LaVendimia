package com.app.lavendimia.ventas;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.lavendimia.R;

import java.util.List;

public class mainVentasAdapter
        extends RecyclerView.Adapter<mainVentasAdapter.VentasViewHolder>
        implements View.OnClickListener{
    private View.OnClickListener listener;
    private List<mainVentas> items;

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


    public static class VentasViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item

        public TextView txtvid;
        public TextView txtuserid;
        public TextView txtnombre;
        public TextView txttotal;
        public TextView txtfecha;
        public VentasViewHolder(View v) {
            super(v);
            txtvid = v.findViewById(R.id.txtventaid);
            txtuserid = v.findViewById(R.id.txtusuarioid);
            txtnombre = v.findViewById(R.id.txtusuarionombre);
            txttotal = v.findViewById(R.id.txttotalventa);
            txtfecha = v.findViewById(R.id.txtfechaventa);
        }
    }

    public mainVentasAdapter(Context contexto, List<mainVentas> items)
    {
        this.items = items;
        this.mContext = contexto;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public mainVentasAdapter.VentasViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_ventas, viewGroup, false);
        v.setOnClickListener(this);
        return new mainVentasAdapter.VentasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(mainVentasAdapter.VentasViewHolder viewHolder, final int i) {
        //viewHolder.imagen.setImageResource(items.get(i).getImagen());
        viewHolder.txtvid.setText(String.valueOf(items.get(i).getV_folio()));
        viewHolder.txtuserid.setText(String.valueOf(items.get(i).getV_cid()));
        String nombreCompleto = items.get(i).getNombre() + " " + items.get(i).getApaterno() +
                " " + items.get(i).getAmaterno();

        viewHolder.txtnombre.setText(nombreCompleto);
        viewHolder.txttotal.setText(String.valueOf(items.get(i).getV_total()));
        viewHolder.txtfecha.setText(String.valueOf(items.get(i).getV_fecha()));
    }
}
