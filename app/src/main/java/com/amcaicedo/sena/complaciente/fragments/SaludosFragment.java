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

import com.amcaicedo.sena.complaciente.R;
import com.amcaicedo.sena.complaciente.adapters.CancionesAdapter;
import com.amcaicedo.sena.complaciente.adapters.SaludoAdapter;
import com.amcaicedo.sena.complaciente.models.Cancion;
import com.amcaicedo.sena.complaciente.models.Saludo;
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
public class SaludosFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference myRef;

    RecyclerView reciclerSaludosFragment;
    List<Saludo> saludos;

    SaludoAdapter adapter;

    FloatingActionButton fabAgregarSaludoFragment;

    Bundle extras;
    String firebase_reference;


    public SaludosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_saludos, container, false);


        extras = getActivity().getIntent().getExtras();
        firebase_reference = extras.getString("FIREBASE_REFERENCE");

        fabAgregarSaludoFragment = (FloatingActionButton) v.findViewById(R.id.fabAgregarSaludosFragment);

        reciclerSaludosFragment = (RecyclerView) v.findViewById(R.id.reciclerSaludosFragment);

        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);

        reciclerSaludosFragment.setLayoutManager(mManager);

        saludos = new ArrayList<>();

        database = FirebaseDatabase.getInstance();

        adapter = new SaludoAdapter(saludos);
        reciclerSaludosFragment.setAdapter(adapter);

        myRef = database.getReference(firebase_reference);

        myRef.child("saludos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("SALUDO", dataSnapshot.toString());
                saludos.removeAll(saludos);
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Saludo saludo = snapshot.getValue(Saludo.class);
                    Log.i("PRUEBA", saludo.getEmisor());
                    saludos.add(saludo);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fabAgregarSaludoFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingNewSaludoDialog();
            }
        });

        return v;
    }


    private void addingNewSaludoDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Agregar nuevo saludo");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setPadding(10, 10, 10, 10);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText emisor = new EditText(getActivity());
        emisor.setHint("Persona que envía el saludo");
        layout.addView(emisor);

        final EditText receptor = new EditText(getActivity());
        receptor.setHint("Persona que recibe el saludo");
        layout.addView(receptor);

        final EditText detalleMensaje = new EditText(getActivity());
        detalleMensaje.setHint("Mensaje");
        layout.addView(detalleMensaje);

        alertDialog.setView(layout);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Saludo saludo = new Saludo();
                saludo.setEmisor(emisor.getText().toString());
                saludo.setReceptor(receptor.getText().toString());
                saludo.setDetalle(detalleMensaje.getText().toString());
                String id = myRef.child("canciones").push().getKey();
                myRef.child("saludos").child(id).setValue(saludo);
            }
        });

        alertDialog.setNegativeButton("Cancelar", null);

        //show alert
        alertDialog.show();
    }

}
