package com.example.fred_liu.pulsr;

import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;


public class GCMService extends GcmListenerService {
    public static final String TAG = GCMService.class.getSimpleName();

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Intent intent = new Intent(MainActivity.MESSAGE_RECEIVED);
        intent.putExtra("message",message);
        LocalBroadcastManager.getInstance(GCMService.this).sendBroadcast(intent);
        if(MainActivity.getNotification_on_off() == 1) {
            sendNotification(message);
        }
        Log.d(TAG, "notification_on_off: "+MainActivity.getNotification_on_off());

    }

    private void sendNotification(String message) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setAction(MainActivity.MESSAGE_RECEIVED);
        intent.putExtra("message",message);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        PendingIntent dismissIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .addAction(R.drawable.ic_play_arrow_black_24dp, "Play",
                        pendingIntent)
                .addAction(R.drawable.ic_do_not_disturb_black_24dp, "Dismiss",
                        dismissIntent)
                .setSmallIcon(R.drawable.ic_message_black_24dp)
                .setContentTitle("PulsR Message Received")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }
}