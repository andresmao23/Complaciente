package com.amcaicedo.sena.complaciente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.amcaicedo.sena.complaciente.models.Promocion;

import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

public class CartaActivity extends AppCompatActivity {

    Button btnEntrar;
    ImageView imgCarta;
    List<Promocion> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carta);
        Promocion.init(this);
        data = Promocion.listAll(Promocion.class);

        imgCarta = (ImageView) findViewById(R.id.imgCarta);
        PhotoViewAttacher photoView = new PhotoViewAttacher(imgCarta);
        photoView.update();

        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartaActivity.this, FragmentContentNavigationActivity.class);
                intent.putExtra(FragmentContentNavigationActivity.KEY_ID, data.get(0).getId());
                intent.putExtra("FIREBASE_REFERENCE", "chilango");
                startActivity(intent);
            }
        });

    }
}
