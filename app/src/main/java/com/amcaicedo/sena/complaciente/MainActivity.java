package com.amcaicedo.sena.complaciente;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amcaicedo.sena.complaciente.Util.AppUtil;
import com.amcaicedo.sena.complaciente.adapters.PromoAdapter;
import com.amcaicedo.sena.complaciente.models.Promocion;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener, PromoAdapter.OnItemClick, SwipeRefreshLayout.OnRefreshListener {
    ImageView usrImg, bannerImg;
    TextView usrTxt;

    NavigationView nav;
    DrawerLayout drawer;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    ActionBarDrawerToggle toggle;

    RecyclerView list;
    PromoAdapter adapter;

    SwipeRefreshLayout refresh;
    List<Promocion> data;

    FloatingActionButton fab;




    private static final Map<String, List<String>> PLACES_BY_BEACONS;

    // TODO: replace "<major>:<minor>" strings to match your own beacons.
    static {
        Map<String, List<String>> placesByBeacons = new HashMap<>();
        //placesByBeacons.put("22504:48827", new ArrayList<String>() {{
        placesByBeacons.put("18810:35425", new ArrayList<String>() {{
            add("Heavenly Sandwiches");
            // read as: "Heavenly Sandwiches" is closest
            // to the beacon with major 22504 and minor 48827
            add("Green & Green Salads");
            // "Green & Green Salads" is the next closest
            add("Mini Panini");
            // "Mini Panini" is the furthest away
        }});
        placesByBeacons.put("648:12", new ArrayList<String>() {{
            add("Mini Panini");
            add("Green & Green Salads");
            add("Heavenly Sandwiches");
        }});
        PLACES_BY_BEACONS = Collections.unmodifiableMap(placesByBeacons);
    }

    private List<String> placesNearBeacon(Beacon beacon) {
        String beaconKey = String.format("%d:%d", beacon.getMajor(), beacon.getMinor());
        if (PLACES_BY_BEACONS.containsKey(beaconKey)) {
            return PLACES_BY_BEACONS.get(beaconKey);
        }
        return Collections.emptyList();
    }

    private BeaconManager beaconManager;
    private Region region;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Promocion.init(this);


        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark
                , R.color.colorAccent);
        refresh.setOnRefreshListener(this);


        list = (RecyclerView) findViewById(R.id.list);
        data = Promocion.listAll(Promocion.class);
        adapter = new PromoAdapter(this, data);
        adapter.setOnItemClick(list, this);

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));


        preferences = getSharedPreferences(AppUtil.PREFERENCES_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.setDrawerListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, R.string.open_drawer, R.string.close_drawer);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nav = (NavigationView) findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(this);
        View header = nav.getHeaderView(0);

        usrImg = (ImageView) header.findViewById(R.id.img_usr);
        bannerImg = (ImageView) header.findViewById(R.id.img);

        usrTxt = (TextView) header.findViewById(R.id.txt_usr);
        usrTxt.setText(preferences.getString(AppUtil.KEY_USR_NAME, ""));

        Uri uriUsr = Uri.parse(preferences.getString(AppUtil.KEY_USR_IMG, ""));
        Uri uriBanner = Uri.parse(preferences.getString(AppUtil.KEY_USR_IMG_BANNER, ""));

        Transformation rounded = new RoundedTransformationBuilder().oval(true).build();

        Picasso.with(this).load(uriUsr).transform(rounded).into(usrImg);
        Picasso.with(this).load(uriBanner).into(bannerImg);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Contenido en construcción", Snackbar.LENGTH_LONG).show();
            }
        });





        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    List<String> places = placesNearBeacon(nearestBeacon);
                    // TODO: update the UI here
                    Log.d("Airport", "Nearest places: " + places);
                }
            }
        });
        region = new Region("ranged region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_cerrar_sesion:
                editor.putBoolean(AppUtil.KEY_LOGIN, false);
                editor.commit();

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);

                finish();
                break;
            case R.id.nav_lista_canciones:
                /*intent = new Intent(this, CancionesActivity.class);
                startActivity(intent);*/
                break;
        }
        drawer.closeDrawers();
        return false;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        toggle.onDrawerSlide(drawerView, slideOffset);
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        toggle.onDrawerOpened(drawerView);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        toggle.onDrawerClosed(drawerView);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        toggle.onDrawerStateChanged(newState);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, PromoDetailActivity.class);
        intent.putExtra(PromoDetailActivity.KEY_ID, data.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, "Cargando nuevo contenido",Toast.LENGTH_SHORT).show();
    }






    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });

    }

    @Override
    protected void onPause() {

        beaconManager.stopRanging(region);

        super.onPause();
    }

}
