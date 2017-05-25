package com.amcaicedo.sena.complaciente;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.amcaicedo.sena.complaciente.Util.AppUtil;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {

    boolean login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash);

        TimerTask task = new TimerTask() {
            SharedPreferences preferences = getSharedPreferences(AppUtil.PREFERENCES_NAME, MODE_PRIVATE);
            public boolean login = preferences.getBoolean(AppUtil.KEY_LOGIN, false);
            Intent intent = null;

            @Override
            public void run() {
                if (login)
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                else
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);

                finish();
            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, 4000);
    }
}