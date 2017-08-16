package com.amcaicedo.sena.complaciente.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amcaicedo.sena.complaciente.R;
import com.amcaicedo.sena.complaciente.Util.AppUtil;
import com.amcaicedo.sena.complaciente.models.Saludo;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by asus on 19/07/2017.
 */

public class SaludoAdapter extends RecyclerView.Adapter<SaludoAdapter.SaludoViewHolder> {

    List<Saludo> saludos;
    Context context;

    public SaludoAdapter(List<Saludo> saludos, Context context) {
        this.saludos = saludos;
        this.context = context;
    }

    @Override
    public SaludoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_saludos, parent, false);
        SaludoAdapter.SaludoViewHolder holder = new SaludoAdapter.SaludoViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SaludoViewHolder holder, int position) {
        final Saludo saludo = saludos.get(position);
        holder.tvNombreEmisor.setText("De: " + saludo.getEmisor());
        holder.tvNombreReceptor.setText("Para: " + saludo.getReceptor());
        holder.tvDetalleMensaje.setText("Mensaje: " + saludo.getDetalle());
        //Log.e("URI DESCARGA", saludo.getUrl());
        //holder.img.setImageURI(Uri.parse(saludo.getUri()));
        if(saludo.getUrl() == null)
            Glide.with(context).load(R.drawable.discoteca).fitCenter().centerCrop().into(holder.img);
        else
            Glide.with(context).load(saludo.getUrl()).fitCenter().centerCrop().into(holder.img);
    }

    @Override
    public int getItemCount() {
        return saludos.size();
    }

    public static class SaludoViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombreEmisor;
        TextView tvNombreReceptor;
        TextView tvDetalleMensaje;
        ImageView img;

        public SaludoViewHolder(View itemView) {
            super(itemView);
            tvNombreEmisor = (TextView) itemView.findViewById(R.id.tvNommbreEmisor);
            tvNombreReceptor = (TextView) itemView.findViewById(R.id.tvNommbreReceptor);
            tvDetalleMensaje = (TextView) itemView.findViewById(R.id.tvDetalleMensaje);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }

}
