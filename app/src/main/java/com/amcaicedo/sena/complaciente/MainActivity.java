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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amcaicedo.sena.complaciente.Util.AppUtil;
import com.amcaicedo.sena.complaciente.adapters.PromoAdapter;
import com.amcaicedo.sena.complaciente.models.Promocion;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

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
}
