package com.app.lavendimia.articulo;

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
import com.app.lavendimia.fragment_editadarticulo;

import java.util.List;

public class mainArticulosAdapter extends RecyclerView.Adapter<mainArticulosAdapter.ArticulosViewHolder>
        implements View.OnClickListener{
    private View.OnClickListener listener;
    private List<mainArticulos> items;

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


    public static class ArticulosViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item

        public TextView id;
        public TextView nombre;
        public ImageView editar;
        public ArticulosViewHolder(View v) {
            super(v);
            id = v.findViewById(R.id.txtclientid);
            nombre = v.findViewById(R.id.txtclientNombre);
            editar = v.findViewById(R.id.editCliente);
        }
    }

    public mainArticulosAdapter(Context contexto, List<mainArticulos> items)
    {
        this.items = items;
        this.mContext = contexto;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public mainArticulosAdapter.ArticulosViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_cliente, viewGroup, false);
        v.setOnClickListener(this);
        return new mainArticulosAdapter.ArticulosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(mainArticulosAdapter.ArticulosViewHolder viewHolder, final int i) {
        //viewHolder.imagen.setImageResource(items.get(i).getImagen());
        viewHolder.id.setText(String.valueOf(items.get(i).getAr_id()));
        viewHolder.nombre.setText(items.get(i).getAr_descripcion());
        viewHolder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidades.puedeRegresar = false;
                FragmentManager manager = ((AppCompatActivity)mContext).getSupportFragmentManager();
                fragment_editadarticulo fragment_editar = new fragment_editadarticulo();
                Bundle bundle = new Bundle();
                bundle.putString("opcion","1");
                bundle.putString("id",String.valueOf(items.get(i).getAr_id()));
                bundle.putString("descripcion",String.valueOf(items.get(i).getAr_descripcion()));
                bundle.putString("modelo",String.valueOf(items.get(i).getAr_Modelo()));
                bundle.putString("precio",String.valueOf(items.get(i).getAr_precio()));
                bundle.putString("existencia",String.valueOf(items.get(i).getAr_existencia()));
                fragment_editar.setArguments(bundle);
                manager.beginTransaction()
                        .replace(R.id.idu_contenedor, fragment_editar).commit();
            }
        });
        //viewHolder.imagen.setImageURI(Uri.parse(items.get(i).getImagen()));
        //Rect rect = new Rect(viewHolder.imagen.getLeft(),viewHolder.imagen.getTop(),viewHolder.imagen.getRight(),viewHolder.imagen.getBottom());
    }
}

