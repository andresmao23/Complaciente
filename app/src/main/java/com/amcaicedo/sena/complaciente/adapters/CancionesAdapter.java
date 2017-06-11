package com.amcaicedo.sena.complaciente.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amcaicedo.sena.complaciente.R;
import com.amcaicedo.sena.complaciente.models.Cancion;

import java.util.List;

/**
 * Created by asus on 10/06/2017.
 */

public class CancionesAdapter extends RecyclerView.Adapter<CancionesAdapter.CancionesViewHolder>{

    List<Cancion> canciones;

    public CancionesAdapter(List<Cancion> canciones) {
        this.canciones = canciones;
    }

    @Override
    public CancionesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_canciones, parent, false);
        CancionesViewHolder holder = new CancionesViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(CancionesViewHolder holder, int position) {
        Cancion cancion = canciones.get(position);
        holder.tvNombreCancion.setText(cancion.getNombre());
        holder.tvNombreAutor.setText(cancion.getAutor());
    }

    @Override
    public int getItemCount() {
        return canciones.size();
    }

    public static class CancionesViewHolder extends RecyclerView.ViewHolder{

        TextView tvNombreCancion, tvNombreAutor;

        public CancionesViewHolder(View itemView) {
            super(itemView);

            tvNombreCancion = (TextView) itemView.findViewById(R.id.tvNombreCancion);
            tvNombreAutor = (TextView) itemView.findViewById(R.id.tvNombreAutor);

        }
    }

}
