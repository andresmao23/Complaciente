package com.amcaicedo.sena.complaciente;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amcaicedo.sena.complaciente.Util.AppUtil;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView usrImg, bannerImg;
    TextView usrTxt;

    NavigationView nav;
    DrawerLayout drawer;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(AppUtil.PREFERENCES_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        nav = (NavigationView) findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(this);
        View header = nav.getHeaderView(0);

        usrImg = (ImageView) header.findViewById(R.id.img_usr);
        bannerImg = (ImageView) header.findViewById(R.id.img);

        usrTxt = (TextView) header.findViewById(R.id.txt_usr);
        usrTxt.setText(preferences.getString(AppUtil.KEY_USR_NAME, ""));

        Uri uriUsr = Uri.parse(preferences.getString(AppUtil.KEY_USR_IMG, ""));
        Uri uriBanner = Uri.parse(preferences.getString(AppUtil.KEY_USR_IMG_BANNER, ""));

        Picasso.with(this).load(uriUsr).into(usrImg);
        Picasso.with(this).load(uriBanner).into(bannerImg);

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
        }
        drawer.closeDrawers();
        return false;
    }
}
