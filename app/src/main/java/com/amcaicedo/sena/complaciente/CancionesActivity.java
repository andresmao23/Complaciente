package com.amcaicedo.sena.complaciente;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

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

    FirebaseDatabase database;
    DatabaseReference myRef;

    RecyclerView reciclerCanciones;
    List<Cancion> canciones;

    CancionesAdapter adapter;

    FloatingActionButton fabAgregarCancion;

    Bundle extras;
    String firebase_reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canciones);

        extras = getIntent().getExtras();
        firebase_reference = extras.getString("FIREBASE_REFERENCE");

        fabAgregarCancion = (FloatingActionButton) findViewById(R.id.fabAgregarCanciones);

        reciclerCanciones = (RecyclerView) findViewById(R.id.reciclerCanciones);

        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);

        reciclerCanciones.setLayoutManager(mManager);

        canciones = new ArrayList<>();

        database = FirebaseDatabase.getInstance();

        adapter = new CancionesAdapter(canciones);
        reciclerCanciones.setAdapter(adapter);

        myRef = database.getReference(firebase_reference);

        myRef.child("canciones").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("CANCION", dataSnapshot.toString());
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

        fabAgregarCancion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingNewCancionDialog();
            }
        });

    }

    private void addingNewCancionDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CancionesActivity.this);
        alertDialog.setTitle("Agregar nueva cancion");

        LinearLayout layout = new LinearLayout(this);
        layout.setPadding(10, 10, 10, 10);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText nombreCancion = new EditText(this);
        nombreCancion.setHint("Digite cancion");
        layout.addView(nombreCancion);

        final EditText nombreAutor = new EditText(this);
        nombreAutor.setHint("Digite Autor");
        layout.addView(nombreAutor);

        alertDialog.setView(layout);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Cancion cancion = new Cancion();
                cancion.setNombre(nombreCancion.getText().toString());
                cancion.setAutor(nombreAutor.getText().toString());
                myRef.child("canciones").push().setValue(cancion);
            }
        });

        alertDialog.setNegativeButton("Cancelar", null);

        //show alert
        alertDialog.show();
    }

}
