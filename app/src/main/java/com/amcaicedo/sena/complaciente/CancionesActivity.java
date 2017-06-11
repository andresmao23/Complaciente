package com.amcaicedo.sena.complaciente;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.amcaicedo.sena.complaciente.adapters.CancionesAdapter;
import com.amcaicedo.sena.complaciente.firebase.FirebaseReferences;
import com.amcaicedo.sena.complaciente.models.Cancion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CancionesActivity extends AppCompatActivity {

    RecyclerView reciclerCanciones;
    List<Cancion> canciones;

    CancionesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canciones);

        reciclerCanciones = (RecyclerView) findViewById(R.id.reciclerCanciones);

        reciclerCanciones.setLayoutManager(new LinearLayoutManager(this));

        canciones = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        adapter = new CancionesAdapter(canciones);
        reciclerCanciones.setAdapter(adapter);

        DatabaseReference myRef = database.getReference(FirebaseReferences.CANCIONES_REFERENCE);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("CANCION", dataSnapshot.toString());
                canciones.removeAll(canciones);
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Cancion cancion = snapshot.getValue(Cancion.class);
                    canciones.add(cancion);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
