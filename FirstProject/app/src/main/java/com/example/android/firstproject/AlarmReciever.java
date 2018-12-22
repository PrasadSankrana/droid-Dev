package com.example.android.firstproject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmReciever extends BroadcastReceiver {

    private NotificationManager notificationManager;
    private Notification notification;
    PendingIntent pendingIntent;
    Uri alarmUri;
   static MediaPlayer ringtone=null;
   //static Ringtone ringtone = null;
    @Override
    public void onReceive(Context context, Intent intent) {
         alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        try {
            loopRingtoneForMinute(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Alarm","Ringtone triggered");
        sendNotifcation(context,intent);
            }

     public void sendNotifcation(Context context, Intent intent){

        Intent dismissAlarm = new Intent(context,NotificationIntentService.class);
        dismissAlarm.setAction("dismissAlarm");
       // dismissAlarm.putExtra("ringtone",(Serializable) ringtone);
        PendingIntent dismissAlarmService = PendingIntent.getService(context,0,dismissAlarm,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent snoozeAlarm = new Intent(context,NotificationIntentService.class);
        snoozeAlarm.setAction("snoozeAlarm");
       // snoozeAlarm.putExtra("context",(Serializable)context);
       // snoozeAlarm.putExtra("intent",(Serializable)intent);
        PendingIntent snoozeAlarmService = PendingIntent.getService(context,1,snoozeAlarm,PendingIntent.FLAG_UPDATE_CURRENT);

         NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,"Alarm1");
         Intent openMainActivity = new Intent(context,MainActivity.class);
          pendingIntent = PendingIntent.getActivity(context,0,openMainActivity,PendingIntent.FLAG_UPDATE_CURRENT);
         notification = notificationBuilder.setContentIntent(pendingIntent)
                                .setTicker("ticker value")
                                .setAutoCancel(true)
                                .setPriority(1)
                                .setContentTitle("Alarm")
                                .setContentText("To cancel Click Dismiss,To snooze 5 mins click snooze")
                                .setContentInfo("ContentInfo")
                                .setSound(alarmUri)
                                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                                .setSmallIcon(R.drawable.alarm_notification_icon)
                                .addAction(0,"Dismiss",dismissAlarmService)
                                .addAction(0,"Snooze",snoozeAlarmService)
                                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND).build();
         notificationManager  =  (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
         notificationManager.notify(14,notification);
         Log.i("Alarm","Alarm Notification triggered");
         /*Notification notification = new Notification(R.drawable.alarm_notification_icon, null,
                 System.currentTimeMillis());
         RemoteViews rmNotificationView = new RemoteViews("com.example.android.firstproject",R.layout.alarm_notification);
         notification.contentView = rmNotificationView;
         notification.contentIntent = pendingIntent;
         notification.flags |= Notification.FLAG_NO_CLEAR;
             notificationManager  =  (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
             notificationManager.notify(1,notification);
           try {
               rmNotificationView.setOnClickPendingIntent(R.id.DismissAlarm, stopRingtone());
               rmNotificationView.setOnClickPendingIntent(R.id.SnoozeAlarm, snoozeAlarm());
           }catch (Exception e) {Log.i("notification","clicked on dismiss alarm");}*/
     }

     protected  void loopRingtoneForMinute(Context context) throws IOException {

         ringtone = new MediaPlayer();
         ringtone.setDataSource(context,alarmUri);
        final AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if(audioManager.getStreamVolume(AudioManager.STREAM_RING)!=0){
            ringtone.setAudioStreamType(AudioManager.STREAM_RING);
            ringtone.setLooping(true);
            ringtone.prepare();
            ringtone.start();
        }
        Timer tm = new Timer();
        tm.schedule(new TimerTask() {
            @Override
            public void run() {
                if(ringtone.isPlaying() && ringtone!=null)
                    ringtone.stop();
                notificationManager.cancel(14);
            }
        },60000);
     }

     public static PendingIntent stopRingtone(){
        PendingIntent pi=null;
        if(ringtone!= null && ringtone.isPlaying() ) {
            ringtone.stop();
            Log.i("Alarm","Stop ringtone");
        }
        return pi;
     }

    public static PendingIntent snoozeAlarm(Context context,Intent intent){
        //PendingIntent pi=null;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,234324243,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+300000,pendingIntent);
        Log.i("Alarm","snooze alarm successful");
        Toast.makeText(context, "Snooze alarm for 5 mins",Toast.LENGTH_LONG).show();
        return pendingIntent;
    }
}
