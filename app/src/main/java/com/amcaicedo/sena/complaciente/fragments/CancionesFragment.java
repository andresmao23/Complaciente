package com.amcaicedo.sena.complaciente.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.amcaicedo.sena.complaciente.CancionesActivity;
import com.amcaicedo.sena.complaciente.R;
import com.amcaicedo.sena.complaciente.adapters.CancionesAdapter;
import com.amcaicedo.sena.complaciente.models.Cancion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CancionesFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference myRef;

    RecyclerView reciclerCancionesFragment;
    List<Cancion> canciones;

    CancionesAdapter adapter;

    FloatingActionButton fabAgregarCancionFragment;

    Bundle extras;
    String firebase_reference;

    public CancionesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_canciones, container, false);

        extras = getActivity().getIntent().getExtras();
        firebase_reference = extras.getString("FIREBASE_REFERENCE");

        fabAgregarCancionFragment = (FloatingActionButton) v.findViewById(R.id.fabAgregarCancionesFragment);

        reciclerCancionesFragment = (RecyclerView) v.findViewById(R.id.reciclerCancionesFragment);

        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);

        reciclerCancionesFragment.setLayoutManager(mManager);

        canciones = new ArrayList<>();

        database = FirebaseDatabase.getInstance();

        adapter = new CancionesAdapter(canciones, getActivity());
        reciclerCancionesFragment.setAdapter(adapter);

        myRef = database.getReference(firebase_reference);

        myRef.child("canciones").orderByChild("votos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("CANCION", dataSnapshot.toString());
                canciones.removeAll(canciones);
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Cancion cancion = snapshot.getValue(Cancion.class);
                    cancion.setId(snapshot.getKey());
                    canciones.add(cancion);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fabAgregarCancionFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingNewCancionDialog();
            }
        });

        return v;
    }

    private void addingNewCancionDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Agregar nueva cancion");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setPadding(10, 10, 10, 10);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText nombreCancion = new EditText(getActivity());
        nombreCancion.setHint("Digite cancion");
        layout.addView(nombreCancion);

        final EditText nombreAutor = new EditText(getActivity());
        nombreAutor.setHint("Digite Autor");
        layout.addView(nombreAutor);

        alertDialog.setView(layout);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Cancion cancion = new Cancion();
                cancion.setNombre(nombreCancion.getText().toString());
                cancion.setAutor(nombreAutor.getText().toString());
                cancion.setVotos(0);
                String id = myRef.child("canciones").push().getKey();
                cancion.setId(id);
                myRef.child("canciones").child(id).setValue(cancion);
            }
        });

        alertDialog.setNegativeButton("Cancelar", null);

        //show alert
        alertDialog.show();
    }

}
