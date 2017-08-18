package com.amcaicedo.sena.complaciente;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by asus on 28/06/2017.
 */

public class BeaconNotification extends Application{

    private BeaconManager beaconManager;

    @Override
    public void onCreate() {
        super.onCreate();

        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                showInNotification("Bienvenido a CHILANGO bar", "Conoce nuestros productos y precios");
            }
            @Override
            public void onExitedRegion(Region region) {
                // could add an "exit" notification too if you want (-:
                showOutNotification("Gracias por visitar CHILANGO bar", "Te esperamos pronto!");
            }
        });
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region("monitored region",
                        //UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 22504, 48827));
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 18810, 35425));
            }
        });
    }

    public void showInNotification(String title, String message) {

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.carta)).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent(this, CartaActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent piResult = PendingIntent.getActivities(this, (int) Calendar.getInstance().getTimeInMillis(), new Intent[]{resultIntent}, 0);

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_pander_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(bigPictureStyle)
                .setAutoCancel(true)
                .setContentIntent(piResult)
                .addAction(R.mipmap.ic_launcher, "Ver carta", piResult);

        notificationManager.notify(0, builder.build());

    }

    public void showOutNotification(String title, String message) {

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.bye)).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent(this, CartaActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent piResult = PendingIntent.getActivities(this, (int) Calendar.getInstance().getTimeInMillis(), new Intent[]{resultIntent}, 0);

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_pander_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(bigPictureStyle);

        notificationManager.notify(0, builder.build());
    }

}
