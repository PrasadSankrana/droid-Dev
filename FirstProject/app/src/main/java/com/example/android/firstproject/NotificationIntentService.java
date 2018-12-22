package com.example.android.firstproject;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;

public class NotificationIntentService extends IntentService {
    public NotificationIntentService(){
        super("notificationIntentService");
    }
   static NotificationManager nmr = null;

    @Override
    protected void onHandleIntent(final Intent intent) {

        nmr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

     switch (intent.getAction()){
         case "dismissAlarm" :
             android.os.Handler daHandler = new android.os.Handler(getMainLooper());
             daHandler.post(new Runnable() {
                 @Override
                 public void run() {
                     MediaPlayer ringtone =AlarmReciever.ringtone;
                     if(ringtone!= null && ringtone.isPlaying() ) {
                         AlarmReciever.stopRingtone();
                         nmr.cancel(14);
                         MainActivity.setAlarm.setEnabled(true);
                         MainActivity.cancelAlarm.setEnabled(false);
                         Log.i("Alarm","Stop ringtone");
                         Toast.makeText(getApplicationContext(), "Running Alarm stopped",Toast.LENGTH_LONG).show();
                     }
                 }
             });
        break;
         case "snoozeAlarm" :
              android.os.Handler snHandler = new android.os.Handler(getMainLooper());
              snHandler.post(new Runnable() {
                  @Override
                  public void run() {
                      Context mainActivityContext =MainActivity.c;// (Context) extras.getSerializable("context");
                      Intent mainActivityIntent = MainActivity.intent;//(Intent) extras.getSerializable("intent");
                      PendingIntent pendingIntent = PendingIntent.getBroadcast(mainActivityContext,234324243,mainActivityIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                      AlarmManager alarmManager = (AlarmManager)mainActivityContext.getSystemService(Context.ALARM_SERVICE);
                      alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+300000,pendingIntent);
                      nmr.cancel(14);
                      AlarmReciever.stopRingtone();
                      Log.i("Alarm","snooze alarm successful");
                      Toast.makeText(getApplicationContext(), "Snooze alarm for 5 mins",Toast.LENGTH_LONG).show();
                  }
              });
     }
    }
}
