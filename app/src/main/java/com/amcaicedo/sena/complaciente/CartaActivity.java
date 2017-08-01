package com.amcaicedo.sena.complaciente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

public class CartaActivity extends AppCompatActivity {

    Button btnEntrar;
    ImageView imgCarta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carta);

        imgCarta = (ImageView) findViewById(R.id.imgCarta);
        PhotoViewAttacher photoView = new PhotoViewAttacher(imgCarta);
        photoView.update();

        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartaActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
