package com.amcaicedo.sena.complaciente;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.amcaicedo.sena.complaciente.fragments.CancionesFragment;
import com.amcaicedo.sena.complaciente.fragments.HomeFragment;
import com.amcaicedo.sena.complaciente.fragments.SaludosFragment;
import com.amcaicedo.sena.complaciente.models.Promocion;

public class FragmentContentNavigationActivity extends AppCompatActivity {

    public static final String KEY_ID="id";

    public static String nombreBar = "bar";
    Bundle extras;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentManager = getSupportFragmentManager();
            transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navInicio:
                    transaction.replace(R.id.content, new HomeFragment()).commit();
                    return true;
                case R.id.navCanciones:
                    transaction.replace(R.id.content, new CancionesFragment()).commit();
                    return true;
                case R.id.navSaludos:
                    transaction.replace(R.id.content, new SaludosFragment()).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_content_navigation);
        extras = getIntent().getExtras();
        nombreBar = extras.getString("FIREBASE_REFERENCE");
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, new HomeFragment()).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
