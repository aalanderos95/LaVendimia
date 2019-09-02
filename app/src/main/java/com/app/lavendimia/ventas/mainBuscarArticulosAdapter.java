package com.app.lavendimia.ventas;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.lavendimia.R;
import com.app.lavendimia.articulo.mainArticulos;
import com.app.lavendimia.clientes.mainClientes;

import java.util.List;

public class mainBuscarArticulosAdapter extends RecyclerView.Adapter<mainBuscarArticulosAdapter.BuscarViewHolder>
        implements View.OnClickListener{
    private View.OnClickListener listener;
    private List<mainArticulos> itemsArticulos;
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


    public static class BuscarViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item

        public TextView txtdato;
        public BuscarViewHolder (View v) {
            super(v);
            txtdato = v.findViewById(R.id.txtdatoobtenido);

        }
    }

    public mainBuscarArticulosAdapter(Context contexto, List<mainArticulos> items)
    {
        this.itemsArticulos = items;
        this.mContext = contexto;
    }

    @Override
    public mainBuscarArticulosAdapter.BuscarViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_buscar, viewGroup, false);
        v.setOnClickListener(this);
        return new mainBuscarArticulosAdapter.BuscarViewHolder(v);
    }

    @Override
    public void onBindViewHolder(mainBuscarArticulosAdapter.BuscarViewHolder viewHolder, final int i) {
        //viewHolder.imagen.setImageResource(items.get(i).getImagen());
        viewHolder.txtdato.setText(itemsArticulos.get(i).getAr_descripcion());
    }

    @Override
    public int getItemCount() {
        return itemsArticulos.size();
    }
}
