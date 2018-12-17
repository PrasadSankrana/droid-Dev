package com.example.android.firstproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

public class AlarmReciever extends BroadcastReceiver {

    private NotificationManager notificationManager;
    private Notification notification;
    PendingIntent pendingIntent;
    Uri alarmUri;

    @Override
    public void onReceive(Context context, Intent intent) {
         alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        sendNotifcation(context,intent);

            }

     public void sendNotifcation(Context context, Intent intent){

         NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,"Alarm1");
         Intent openMainActivity = new Intent(context,MainActivity.class);
          pendingIntent = PendingIntent.getActivity(context,0,openMainActivity,PendingIntent.FLAG_UPDATE_CURRENT);
             notificationBuilder.setContentIntent(pendingIntent)
                                .setTicker("ticker value")
                                .setAutoCancel(true)
                                .setPriority(1)
                                .setContentTitle("Noti Title")
                                .setContentText("ContextText")
                                .setSound(alarmUri).build();

     }
}
