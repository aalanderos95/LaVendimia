package com.app.lavendimia.clientes;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.lavendimia.R;
import com.app.lavendimia.Utilidades.Utilidades;
import com.app.lavendimia.fragment_editadcliente;
import com.app.lavendimia.fragment_principal;

import java.util.List;

public class mainClientesAdapter extends RecyclerView.Adapter<mainClientesAdapter.ClientesViewHolder>
        implements View.OnClickListener{
    private View.OnClickListener listener;
    private List<mainClientes> items;

    private AlertDialog dialog;
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


    public static class ClientesViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item

        public TextView id;
        public TextView nombre;
        public ImageView editar;
        public ClientesViewHolder(View v) {
            super(v);
            id = v.findViewById(R.id.txtclientid);
            nombre = v.findViewById(R.id.txtclientNombre);
            editar = v.findViewById(R.id.editCliente);
        }
    }

    public mainClientesAdapter(Context contexto, List<mainClientes> items)
    {
        this.items = items;
        this.mContext = contexto;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public mainClientesAdapter.ClientesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_cliente, viewGroup, false);
        v.setOnClickListener(this);
        return new mainClientesAdapter.ClientesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(mainClientesAdapter.ClientesViewHolder viewHolder, final int i) {
        //viewHolder.imagen.setImageResource(items.get(i).getImagen());
        viewHolder.id.setText(String.valueOf(items.get(i).getC_id()));
        String nombrecompleto = items.get(i).getC_nombre() + " " + items.get(i).getC_apaterno() +
                " " + items.get(i).getC_amaterno();

        viewHolder.nombre.setText(nombrecompleto);

        viewHolder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidades.puedeRegresar = false;
                FragmentManager manager = ((AppCompatActivity)mContext).getSupportFragmentManager();
                fragment_editadcliente fragment_editar = new fragment_editadcliente();
                Bundle bundle = new Bundle();
                bundle.putString("opcion","1");
                bundle.putString("id",String.valueOf(items.get(i).getC_id()));
                bundle.putString("nombre",String.valueOf(items.get(i).getC_nombre()));
                bundle.putString("apaterno",String.valueOf(items.get(i).getC_apaterno()));
                bundle.putString("amaterno",String.valueOf(items.get(i).getC_amaterno()));
                bundle.putString("rfc",String.valueOf(items.get(i).getC_rfc()));
                fragment_editar.setArguments(bundle);
                manager.beginTransaction()
                        .replace(R.id.idu_contenedor, fragment_editar).commit();
            }
        });
        //viewHolder.imagen.setImageURI(Uri.parse(items.get(i).getImagen()));
        //Rect rect = new Rect(viewHolder.imagen.getLeft(),viewHolder.imagen.getTop(),viewHolder.imagen.getRight(),viewHolder.imagen.getBottom());
    }
}
