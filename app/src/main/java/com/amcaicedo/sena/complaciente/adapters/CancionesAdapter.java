package com.amcaicedo.sena.complaciente.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.amcaicedo.sena.complaciente.FragmentContentNavigationActivity;
import com.amcaicedo.sena.complaciente.R;
import com.amcaicedo.sena.complaciente.Util.AppUtil;
import com.amcaicedo.sena.complaciente.firebase.FirebaseReferences;
import com.amcaicedo.sena.complaciente.models.Cancion;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by asus on 10/06/2017.
 */

public class CancionesAdapter extends RecyclerView.Adapter<CancionesAdapter.CancionesViewHolder> {

    List<Cancion> canciones;

    FirebaseDatabase database = FirebaseDatabase.getInstance();;
    DatabaseReference myRef;
    SharedPreferences preference;

    ChildEventListener cel;

    Cancion c;

    public CancionesAdapter(List<Cancion> canciones, Context context) {
        this.canciones = canciones;
        preference = context.getSharedPreferences("preference", MODE_PRIVATE);
        //String firebase_reference = preference.getString(AppUtil.KEY_BAR, "");
        myRef = database.getReference(FragmentContentNavigationActivity.nombreBar);
        Log.i("REFERENCIA", myRef.toString());

        cel = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.i("CANCION_KEY", dataSnapshot.getKey());
                c = new Cancion();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRef.child("canciones").addChildEventListener(cel);

    }

    @Override
    public CancionesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_canciones, parent, false);
        CancionesViewHolder holder = new CancionesViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CancionesViewHolder holder, int position) {
        final Cancion cancion = canciones.get(position);
        holder.tvNombreCancion.setText(cancion.getNombre());
        holder.tvNombreAutor.setText(cancion.getAutor());
        holder.tvVotos.setText("Votos: " + String.valueOf(cancion.getVotos()));
        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("canciones").child(cancion.getId()).child("votos").setValue(cancion.getVotos()+1);
            }
        });
        holder.animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.animationView.playAnimation();
            }
        });
    }

    @Override
    public int getItemCount() {
        return canciones.size();
    }

    public static class CancionesViewHolder extends RecyclerView.ViewHolder{

        SharedPreferences pref;
        TextView tvNombreCancion, tvNombreAutor;
        TextView tvVotos;
        ImageView imgLike;
        LottieAnimationView animationView;


        public CancionesViewHolder(View itemView) {
            super(itemView);

            tvNombreCancion = (TextView) itemView.findViewById(R.id.tvNombreCancion);
            tvNombreAutor = (TextView) itemView.findViewById(R.id.tvNombreAutor);
            tvVotos = (TextView) itemView.findViewById(R.id.tvVotos);
            imgLike = (ImageView) itemView.findViewById(R.id.imgLike);
            animationView = (LottieAnimationView) itemView.findViewById(R.id.animation_view);

        }
    }

}
