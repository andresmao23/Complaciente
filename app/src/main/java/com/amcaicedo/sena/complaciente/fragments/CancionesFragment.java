package com.amcaicedo.sena.complaciente.fragments;


import android.app.ProgressDialog;
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
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

    ProgressDialog progressDialog;

    private ArrayList<String> titulosCanciones = new ArrayList<>();
    private ArrayList<String> titulosAutores = new ArrayList<>();

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

        //Progress dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Lista de canciones...");
        progressDialog.setMessage("Cargando lista de canciones");
        progressDialog.setCancelable(false);
        progressDialog.show();

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
                progressDialog.dismiss();
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

        /*reciclerCancionesFragment.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0)
                    // Puedes ocultarlo simplemente
                    //fab.hide();
                    // o añadir la animación deseada
                    fabAgregarCancionFragment.animate().translationY(fabAgregarCancionFragment.getHeight() +
                            getResources().getDimension(R.dimen.fab_margin))
                            .setInterpolator(new LinearInterpolator())
                            .setDuration(1000); // Cambiar al tiempo deseado
                else if (dy < 0)
                    //fab.show();
                    fabAgregarCancionFragment.animate().translationY(0)
                            .setInterpolator(new LinearInterpolator())
                            .setDuration(1000); // Cambiar al tiempo deseado
            }
        });*/

        return v;
    }

    private void addingNewCancionDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Agregar nueva canción");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setPadding(10, 10, 10, 10);
        layout.setOrientation(LinearLayout.VERTICAL);

        final AutoCompleteTextView nombreCancion = new AutoCompleteTextView(getActivity());
        nombreCancion.setHint("Digite canción");
        nombreCancion.setThreshold(2);
        ArrayAdapter<String> titulosCancionesAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, titulosCanciones);
        nombreCancion.setAdapter(titulosCancionesAdapter);
        layout.addView(nombreCancion);

        final AutoCompleteTextView nombreAutor = new AutoCompleteTextView(getActivity());
        nombreAutor.setHint("Digite Autor");
        nombreAutor.setThreshold(2);
        ArrayAdapter<String> titulosAutoresAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, titulosAutores);
        nombreAutor.setAdapter(titulosAutoresAdapter);
        layout.addView(nombreAutor);

        /*final EditText nombreCancion = new EditText(getActivity());
        nombreCancion.setHint("Digite canción");
        layout.addView(nombreCancion);

        final EditText nombreAutor = new EditText(getActivity());
        nombreAutor.setHint("Digite Autor");
        layout.addView(nombreAutor);*/

        alertDialog.setView(layout);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Cancion cancion = new Cancion();
                cancion.setNombre(nombreCancion.getText().toString());
                if (!titulosCanciones.contains(nombreCancion.getText().toString())){
                    titulosCanciones.add(nombreCancion.getText().toString());
                    System.out.print("TITULOS CANCIONES: " + titulosCanciones);
                }
                cancion.setAutor(nombreAutor.getText().toString());
                if (!titulosAutores.contains(nombreAutor.getText().toString())){
                    titulosAutores.add(nombreAutor.getText().toString());
                    System.out.print("TITULOS AUTORES: " + titulosAutores);
                }
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
