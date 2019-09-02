package com.app.lavendimia.ventas;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.lavendimia.R;
import com.app.lavendimia.clientes.mainClientes;

import java.util.List;

public class mainBuscarClientesAdapter extends RecyclerView.Adapter<mainBuscarClientesAdapter.BuscarViewHolder>
        implements View.OnClickListener{
    private View.OnClickListener listener;
    private List<mainClientes> itemsClientes;
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

    public mainBuscarClientesAdapter(Context contexto, List<mainClientes> items)
    {
        this.itemsClientes = items;
        this.mContext = contexto;
    }

    @Override
    public mainBuscarClientesAdapter.BuscarViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_buscar, viewGroup, false);
        v.setOnClickListener(this);
        return new mainBuscarClientesAdapter.BuscarViewHolder(v);
    }

    @Override
    public void onBindViewHolder(mainBuscarClientesAdapter.BuscarViewHolder viewHolder, final int i) {
        //viewHolder.imagen.setImageResource(items.get(i).getImagen());
            String nombreCompleto = itemsClientes.get(i).getC_nombre() + " " + itemsClientes.get(i).getC_apaterno() +
                    " " + itemsClientes.get(i).getC_amaterno();
            viewHolder.txtdato.setText(nombreCompleto);
    }

    @Override
    public int getItemCount() {
        return itemsClientes.size();
    }
}
