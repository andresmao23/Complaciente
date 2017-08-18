package com.amcaicedo.sena.complaciente.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amcaicedo.sena.complaciente.FragmentContentActivity;
import com.amcaicedo.sena.complaciente.R;
import com.amcaicedo.sena.complaciente.Util.AppUtil;
import com.amcaicedo.sena.complaciente.models.Promocion;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public static final String KEY_ID="id";

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    ImageView img;
    TextView titulo, bar, contenido;

    AppCompatButton btnIngresar;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    Promocion promo;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        preferences = getActivity().getSharedPreferences(AppUtil.PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

        /*toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) v.findViewById(R.id.collapsingToolbar);*/

        img = (ImageView) v.findViewById(R.id.img);
        titulo = (TextView) v.findViewById(R.id.titulo);
        bar = (TextView) v.findViewById(R.id.bar);
        contenido = (TextView) v.findViewById(R.id.content);


        long id = getActivity().getIntent().getExtras().getLong(KEY_ID);
        Log.e("Fragment", String.valueOf(id));
        promo = Promocion.findById(Promocion.class, id);

        /*collapsingToolbarLayout.setTitle(promo.getTitulo());
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
        titulo.setText(promo.getTitulo());
        contenido.setText(promo.getDescripcion());*/

        Uri uri = Uri.parse(promo.getImagen());
        Picasso.with(getActivity()).load(uri).into(img);

        /*BitmapDrawable bD = (BitmapDrawable) img.getDrawable();
        Palette p = Palette.from(bD.getBitmap()).generate();*/

        //collapsingToolbarLayout.setTitle(promo.getTitulo());
        //collapsingToolbarLayout.setContentScrimColor(p.getVibrantColor(0));
        titulo.setText(promo.getTitulo());
        bar.setText(promo.getBar());
        contenido.setText(promo.getDescripcion());

        /*btnIngresar = (AppCompatButton) v.findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(PromoDetailActivity.this, CancionesActivity.class);
                Intent intent = new Intent(getContext(), FragmentContentActivity.class);
                intent.putExtra("FIREBASE_REFERENCE", bar.getText());

                editor.putString(AppUtil.KEY_BAR, bar.getText().toString());
                editor.commit();

                startActivity(intent);
            }
        });*/

        return v;
    }

}
