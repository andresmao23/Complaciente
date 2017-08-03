package com.amcaicedo.sena.complaciente.fragments;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amcaicedo.sena.complaciente.BuildConfig;
import com.amcaicedo.sena.complaciente.R;
import com.amcaicedo.sena.complaciente.adapters.CancionesAdapter;
import com.amcaicedo.sena.complaciente.adapters.SaludoAdapter;
import com.amcaicedo.sena.complaciente.models.Cancion;
import com.amcaicedo.sena.complaciente.models.Saludo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaludosFragment extends Fragment {

    ProgressDialog progressDialog;

    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference storage;

    RecyclerView reciclerSaludosFragment;
    List<Saludo> saludos;

    SaludoAdapter adapter;

    FloatingActionButton fabAgregarSaludoFragment;

    Bundle extras;
    String firebase_reference;

    // Segmento para captura de fotos
    private final int SELECT_PICTURE = 300;
    private String mPath;
    private final int PHOTO_CODE = 200;
    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";
    private final int MY_PERMISSIONS = 100;
    private ImageView imgFoto;
    //private Uri imgDescarga;

    Uri path;


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

        adapter = new SaludoAdapter(saludos, getActivity());
        reciclerSaludosFragment.setAdapter(adapter);

        progressDialog = new ProgressDialog(getActivity());

        storage = FirebaseStorage.getInstance().getReference();

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

        if(mayRequestStoragePermission())
            Toast.makeText(getActivity(), "Permisos autorizados", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(), "Permisos NO autorizados", Toast.LENGTH_SHORT).show();

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

        imgFoto = new ImageView(getActivity());
        imgFoto.setMaxHeight(100);
        imgFoto.setMaxWidth(200);
        layout.addView(imgFoto);

        final TextView tvFoto = new TextView(getActivity());
        tvFoto.setText("Subir foto");
        tvFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptions();
            }
        });
        layout.addView(tvFoto);

        alertDialog.setView(layout);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Uri[] imgDescarga = new Uri[1];
                /*saludo = new Saludo();
                saludo.setEmisor(emisor.getText().toString());
                saludo.setReceptor(receptor.getText().toString());
                saludo.setDetalle(detalleMensaje.getText().toString());*/
                final Saludo saludo = new Saludo();

                progressDialog.setTitle("Enviando...");
                progressDialog.setMessage("Enviando saludo al sistema");
                progressDialog.setCancelable(false);
                progressDialog.show();

                StorageReference filePath = storage.child("fotos").child(path.getLastPathSegment());
                filePath.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //imgDescarga[0] = taskSnapshot.getDownloadUrl();
                        //Log.e("URI A SUBIR", imgDescarga[0].toString());


                        saludo.setEmisor(emisor.getText().toString());
                        saludo.setReceptor(receptor.getText().toString());
                        saludo.setDetalle(detalleMensaje.getText().toString());
                        saludo.setUrl(taskSnapshot.getDownloadUrl().toString());

                        Toast.makeText(getActivity(), "Foto subida exitosamente", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        String id = myRef.child("canciones").push().getKey();
                        myRef.child("saludos").child(id).setValue(saludo);
                    }
                });

                /*saludo.setUrl(String.valueOf(imgDescarga[0]));

                String id = myRef.child("canciones").push().getKey();
                myRef.child("saludos").child(id).setValue(saludo);*/
            }
        });

        alertDialog.setNegativeButton("Cancelar", null);

        //show alert
        alertDialog.show();
    }


    private boolean mayRequestStoragePermission() {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if((getActivity().checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (getActivity().checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(getView(), "Los permisos son necesarios para poder usar la aplicación",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            });
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }

    private void showOptions() {
        final CharSequence[] option = {"Tomar foto", "Elegir de galeria", "Cancelar"};
        //final CharSequence[] option = {"Elegir de galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Eleige una opción");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(option[which] == "Tomar foto"){
                    openCamera();
                    //dispatchTakePictureIntent();
                    //Toast.makeText(getActivity(), "Elemento en construcción", Toast.LENGTH_SHORT).show();
                }else if(option[which] == "Elegir de galeria"){
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                }else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    /*private void openCamera() {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){
            Long timestamp = System.currentTimeMillis() / 1000;
            String imageName = timestamp.toString() + ".jpg";

            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
                    + File.separator + imageName;

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
            startActivityForResult(intent, PHOTO_CODE);
        }
    }*/

    private void openCamera() {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";


        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  // prefix
                    ".jpg",         // suffix
                    storageDir      // directory
            );
        } catch (IOException e) {
            Log.e("ERROR DE CAMARA", e.getMessage());
        }

        // Save a file: path for use with ACTION_VIEW intents
        mPath = image.getAbsolutePath();

        Log.e("PATH CAMARA", mPath);
        File newFile = new File(mPath);
        Log.e("PATH CAMARA FILE", newFile.toString());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity(), "com.amcaicedo.sena.complaciente.FragmentContentActivity", newFile));
            startActivityForResult(intent, PHOTO_CODE);
        }catch (Exception e){
            Log.e("Excepcion de camara", e.getMessage());
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
    }


    /*@Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mPath = savedInstanceState.getString("file_path");
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("RESULT CODE", ""+resultCode);
        Log.e("REQUEST CODE", ""+requestCode);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(getActivity(),
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });


                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    imgFoto.setImageBitmap(bitmap);
                    //path = Uri.parse(mPath);
                    File newFile = new File(mPath);
                    path = FileProvider.getUriForFile(getActivity(), "com.amcaicedo.sena.complaciente.FragmentContentActivity", newFile);
                    Log.e("Ruta de la foto CAMARA", String.valueOf(path));
                    break;
                case SELECT_PICTURE:
                    path = data.getData();
                    Log.e("Ruta de la foto", String.valueOf(path));
                    imgFoto.setImageURI(path);
                    break;

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(), "Permisos aceptados", Toast.LENGTH_SHORT).show();
            }
        }else{
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().finish();
            }
        });

        builder.show();
    }

    /*String mCurrentPhotoPath;

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("Excepcion de camara", ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.amcaicedo.sena.complaciente",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        //File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }*/

}
