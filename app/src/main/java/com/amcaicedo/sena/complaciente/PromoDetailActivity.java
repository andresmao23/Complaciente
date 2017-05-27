package com.amcaicedo.sena.complaciente;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.amcaicedo.sena.complaciente.models.Promocion;
import com.squareup.picasso.Picasso;

public class PromoDetailActivity extends AppCompatActivity {

    public static final String KEY_ID="id";

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    ImageView img;
    TextView titulo, contenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);

        img = (ImageView) findViewById(R.id.img);
        titulo = (TextView) findViewById(R.id.titulo);
        contenido = (TextView) findViewById(R.id.content);

        long id = getIntent().getExtras().getLong(KEY_ID);

        Promocion promo = Promocion.findById(Promocion.class, id);

        /*collapsingToolbarLayout.setTitle(promo.getTitulo());
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
        titulo.setText(promo.getTitulo());
        contenido.setText(promo.getDescripcion());*/

        Uri uri = Uri.parse(promo.getImagen());
        Picasso.with(this).load(uri).into(img);

        BitmapDrawable bD = (BitmapDrawable) img.getDrawable();
        Palette p = Palette.from(bD.getBitmap()).generate();

        collapsingToolbarLayout.setTitle(promo.getTitulo());
        collapsingToolbarLayout.setContentScrimColor(p.getVibrantColor(0));
        titulo.setText(promo.getTitulo());
        contenido.setText(promo.getDescripcion());

    }
}
